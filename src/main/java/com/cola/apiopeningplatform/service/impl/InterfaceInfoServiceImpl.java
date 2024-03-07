package com.cola.apiopeningplatform.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cola.apiopeningplatform.common.ErrorCode;
import com.cola.apiopeningplatform.common.IdRequest;
import com.cola.apiopeningplatform.constant.CommonConstant;
import com.cola.apiopeningplatform.exception.BusinessException;
import com.cola.apiopeningplatform.exception.ThrowUtils;
import com.cola.apiopeningplatform.mapper.InterfaceInfoMapper;
import com.cola.apiopeningplatform.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.cola.apiopeningplatform.model.entity.InterfaceInfo;
import com.cola.apiopeningplatform.model.enums.InterfaceInfoStatusEnum;
import com.cola.apiopeningplatform.model.vo.InterfaceInfoVO;
import com.cola.apiopeningplatform.service.InterfaceInfoService;
import com.cola.apiopeningplatform.utils.SqlUtils;
import com.cola.interfaceclientsdk.client.ApiClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author cola
* @description 针对表【interface_info(api接口)】的数据库操作Service实现
* @createDate 2024-03-01 11:33:32
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Resource
    private ApiClient apiClient;

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        String method = interfaceInfoQueryRequest.getMethod();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        Integer status = interfaceInfoQueryRequest.getStatus();
        Long userId = interfaceInfoQueryRequest.getUserId();
        // 拼接条件
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.eq(StringUtils.isNotBlank(method), "method", method);
        queryWrapper.eq(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
        queryWrapper.eq(StringUtils.isNotBlank(responseHeader), "responseHeader", responseHeader);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.PARAMS_ERROR);
        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();
        Integer status = interfaceInfo.getStatus();
        InterfaceInfoStatusEnum enumByValue = InterfaceInfoStatusEnum.getEnumByValue(status);
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, method, url), ErrorCode.PARAMS_ERROR);
        } else {
            ThrowUtils.throwIf(id <= 0 , ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isNotBlank(description) && description.length() > 150) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口描述过长");
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
        if (ObjectUtils.isNotEmpty(enumByValue) && !InterfaceInfoStatusEnum.OPEN.equals(enumByValue) && !InterfaceInfoStatusEnum.CLOSE.equals(enumByValue)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口状态错误");
        }
    }

    @Override
    public boolean updateInterfaceInfoStatus(IdRequest idRequest, InterfaceInfoStatusEnum interfaceInfoStatusEnum) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ThrowUtils.throwIf(interfaceInfoStatusEnum == null, ErrorCode.PARAMS_ERROR);
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 判断接口是否可以调用
        com.cola.interfaceclientsdk.model.User user = new com.cola.interfaceclientsdk.model.User();

        user.setUsername("cola");
        String username = apiClient.getUsernameByPost(user);
        ThrowUtils.throwIf(StringUtils.isBlank(username), new BusinessException(ErrorCode.SYSTEM_ERROR, "接口认证失败"));
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(interfaceInfoStatusEnum.getValue());
        return this.updateById(interfaceInfo);
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage) {
        ThrowUtils.throwIf(interfaceInfoPage == null, new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(),
                interfaceInfoPage.getTotal());
        if (CollUtil.isEmpty(interfaceInfoList)) {
            return interfaceInfoVOPage;
        }
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(InterfaceInfoVO::objToVo).collect(Collectors.toList());
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }
}




