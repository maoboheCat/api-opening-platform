package com.cola.apiopeningplatform.constant;

/**
 * @author Maobohe
 * @createData 2024/3/13 19:47
 */
public interface RedisConstant {

    /**
     * 系统名
     */
    String REDIS_SYSTEM_NAME = "api_opening_platform";

    /**
     * 调用接口统计 Redis Key
     */
    String REDIS_USERINTERFACEINFO_INVOKECOUNT = String.format("%s:userInterfaceInfo:invokeCount", REDIS_SYSTEM_NAME);

    /**
     * 记录接口调用随机数 Redis Key
     */
    String REDIS_USERINTERFACEINFO_NONCE = String.format("%s:userInterfaceInfo:nonce", REDIS_SYSTEM_NAME);
}
