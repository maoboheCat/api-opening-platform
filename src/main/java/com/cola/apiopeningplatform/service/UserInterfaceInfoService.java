package com.cola.apiopeningplatform.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cola.apiopeningplatform.model.dto.userInterfacInfo.UserInterfaceInfoQueryRequest;
import com.cola.apiopeningplatform.model.entity.UserInterfaceInfo;
import com.cola.apiopeningplatform.model.vo.UserInterfaceInfoVO;


/**
* @author cola
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-03-08 10:48:06
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 参数校验
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 拼接查询条件
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 分页获取用户-接口信息封装
     * @param userInterfaceInfoPage
     * @return
     */
    Page<UserInterfaceInfoVO> getUserInterfaceInfoVOPage(Page<UserInterfaceInfo> userInterfaceInfoPage);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
