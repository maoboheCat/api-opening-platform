package com.cola.apiopeningplatform.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cola.apiopeningplatform.common.ErrorCode;
import com.cola.apiopeningplatform.exception.BusinessException;
import com.cola.apiopeningplatform.model.entity.UserInterfaceInfo;
import com.cola.apiopeningplatform.model.enums.UserInterfaceInfoStatusEnum;
import com.cola.apiopeningplatform.service.UserInterfaceInfoService;
import com.cola.paltformcommon.service.InnerUserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.cola.apiopeningplatform.constant.RedisConstant.REDIS_USERINTERFACEINFO_NONCE;

/**
 * 公共服务类实现
 *
 * @author Maobohe
 * @createData 2024/3/11 21:10
 */

@DubboService
@Slf4j
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final long REDIS_TIMEOUT_MINUTES = 1;


    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public boolean invokeCheck(long interfaceInfoId, long userId, String nonce) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 查询是否可调用该接口
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        queryWrapper.eq("userId", userId);
        queryWrapper.gt("leftNum", 0);
        queryWrapper.eq("status", UserInterfaceInfoStatusEnum.NORMAL.getValue());
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        if (userInterfaceInfo == null) {
            return false;
        }
        String redisKey = String.format("%s:%s:%s:%s", REDIS_USERINTERFACEINFO_NONCE, userId, interfaceInfoId,nonce);
        // 判断key是否存在
        // 存在则不给予调用
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            return false;
        }
        // 不存在则写入
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        try {
            valueOperations.set(redisKey, "", REDIS_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("InnerUserInterfaceInfoService -----> redis set key error", e);
        }
        return true;
    }

}
