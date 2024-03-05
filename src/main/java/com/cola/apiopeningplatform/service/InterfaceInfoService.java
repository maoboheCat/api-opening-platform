package com.cola.apiopeningplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cola.apiopeningplatform.common.IdRequest;
import com.cola.apiopeningplatform.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.cola.apiopeningplatform.model.entity.InterfaceInfo;
import com.cola.apiopeningplatform.model.enums.InterfaceInfoStatusEnum;
import com.cola.apiopeningplatform.model.vo.InterfaceInfoVO;

/**
* @author cola
* @description 针对表【interface_info(api接口)】的数据库操作Service
* @createDate 2024-03-01 11:26:56
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 获取查询条件
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 参数校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 更改接口状态
     * @param idRequest
     * @param interfaceInfoStatusEnum
     * @return
     */
    boolean updateInterfaceInfoStatus(IdRequest idRequest, InterfaceInfoStatusEnum interfaceInfoStatusEnum);

    /**
     * 分页获取接口信息封装
     * @param interfaceInfoPage
     * @return
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage);
}
