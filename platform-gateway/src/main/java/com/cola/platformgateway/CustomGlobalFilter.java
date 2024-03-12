package com.cola.platformgateway;

import com.cola.interfaceclientsdk.utils.SignUtils;
import com.cola.paltformcommon.model.entity.InterfaceInfo;
import com.cola.paltformcommon.model.entity.User;
import com.cola.paltformcommon.service.InnerInterfaceInfoService;
import com.cola.paltformcommon.service.InnerUserInterfaceInfoService;
import com.cola.paltformcommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerinterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final Long ONE_MINUTES = (long) 60;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String method = request.getMethodValue();
        // 请求日志
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + request.getURI());
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        String hostString = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        log.info("请求来源地址：" + hostString);
        log.info("请求来源地址：" + request.getRemoteAddress());
        // 黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(hostString)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        // 用户鉴权
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        String hostAddress = headers.getFirst("hostAddress");
        String url = hostAddress +  StringUtils.removeStart(request.getPath().toString(), "/api");
        // 检擦用户是否有密钥
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        // 签名验证
        if (Long.parseLong(Objects.requireNonNull(nonce)) > 1000) {
            return handleNoAuth(response);
        }
        long currentTime = System.currentTimeMillis() / 1000;
        if ((currentTime - Long.parseLong(Objects.requireNonNull(timestamp))) >= ONE_MINUTES) {
            return handleNoAuth(response);
        }
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.getSisn(body, secretKey);
        if (!serverSign.equals(sign)) {
            return handleNoAuth(response);
        }
        // 检查接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerinterfaceInfoService.getInterfaceInfo(url, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleInvokeError(response);
        }
        // 检查用户是否可用
        Long interfaceInfoId = interfaceInfo.getId();
        Long invokeUserId = invokeUser.getId();
        try {
            if (!innerUserInterfaceInfoService.invokeCheck(interfaceInfoId, invokeUserId)) {
                return handleNoAuth(response);
            }
        } catch (Exception e) {
            log.error("invokeCheck error", e);
        }
        // 请求转发，调用接口，响应日志
        return responseHandle(exchange, chain, interfaceInfoId, invokeUserId);
    }

    /**
     * 响应处理及其日志
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> responseHandle(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long invokeUserId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);//降级处理返回数据
            }
            // 装饰，增强能力
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @NotNull
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        // 往返回值写数据
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // 接口已经成功执行
                            // 接口调用统计
                            try {
                                innerUserInterfaceInfoService.invokeCount(interfaceInfoId, invokeUserId);
                            } catch (Exception e) {
                                log.error("invokeCount error", e);
                            }
                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer buff = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[buff.readableByteCount()];
                            buff.read(content);
                            //释放掉内存
                            DataBufferUtils.release(buff);
                            // 拼接字符串，构建日志
                            String result = new String(content, StandardCharsets.UTF_8);
                            List<Object> rspArgs = new ArrayList<>();
                            rspArgs.add(originalResponse.getStatusCode().value());
                            rspArgs.add(exchange.getRequest().getURI());
                            rspArgs.add(result);
                            log.info("响应状态码：{} || 响应URI：{} || 响应内容：{}", rspArgs.toArray());

                            getDelegate().getHeaders().setContentLength(result.getBytes().length);
                            return bufferFactory.wrap(result.getBytes());
                        }));
                    } else {
                        log.error("<-- {} 响应code异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());

        } catch (Exception e) {
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}