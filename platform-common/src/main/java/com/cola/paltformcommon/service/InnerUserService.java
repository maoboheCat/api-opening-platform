package com.cola.paltformcommon.service;


import com.cola.paltformcommon.model.entity.User;


/**
 * 用户服务
 * @author cola
 * @createDate 2024-03-11 21:04:06
 */
public interface InnerUserService{

    /**
     * 查询是否已经分配给用户密钥
     * @param accessKey
     * @return
     */
    User getInvokeUser (String accessKey);
}
