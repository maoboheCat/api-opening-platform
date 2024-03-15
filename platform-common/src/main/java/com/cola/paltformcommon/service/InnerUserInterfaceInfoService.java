package com.cola.paltformcommon.service;

/**
* @author cola
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-03-11 21:04:06
*/
public interface InnerUserInterfaceInfoService{

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 检验用户
     * @param interfaceInfoId
     * @param userId
     * @param nonce
     * @return
     */
    boolean invokeCheck(long interfaceInfoId, long userId, String nonce);
}
