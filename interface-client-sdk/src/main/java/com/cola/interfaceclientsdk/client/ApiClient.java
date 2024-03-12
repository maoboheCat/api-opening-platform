package com.cola.interfaceclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.cola.interfaceclientsdk.model.User;


import java.util.HashMap;
import java.util.Map;

import static com.cola.interfaceclientsdk.utils.SignUtils.getSisn;

/**
 * 调用第三方接口
 *
 * @author Maobohe
 * @createData 2024/3/2 15:50
 */
public class ApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8082";

    private static final String INTERFACE_HOST = "http://localhost:8081";

    private String accessKey;

    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String result3= HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result3);
        return result3;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String result3= HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);
        System.out.println(result3);
        return result3;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        map.put("nonce", RandomUtil.randomNumbers(2));
        map.put("body", body);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", getSisn(body, secretKey));
        map.put("hostAddress", INTERFACE_HOST);
        return map;
    }



    public String getUsernameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String body = httpResponse.body();
        System.out.println(body);
        return body;
    }
}
