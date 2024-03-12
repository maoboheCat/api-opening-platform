package com.cola.paltformcommon.service;

import com.cola.paltformcommon.model.entity.InterfaceInfo;


/**
* @author cola
* @description 针对表【interface_info(api接口)】的数据库操作Service
 * @createDate 2024-03-11 21:04:06
*/
public interface InnerInterfaceInfoService{

    /**
     * 查询接口信息
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
