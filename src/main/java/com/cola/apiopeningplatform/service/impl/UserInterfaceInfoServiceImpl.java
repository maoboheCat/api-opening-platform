package com.cola.apiopeningplatform.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cola.apiopeningplatform.common.ErrorCode;
import com.cola.apiopeningplatform.constant.CommonConstant;
import com.cola.apiopeningplatform.exception.BusinessException;
import com.cola.apiopeningplatform.exception.ThrowUtils;
import com.cola.apiopeningplatform.mapper.UserInterfaceInfoMapper;
import com.cola.apiopeningplatform.model.dto.userInterfacInfo.UserInterfaceInfoQueryRequest;
import com.cola.apiopeningplatform.model.entity.UserInterfaceInfo;
import com.cola.apiopeningplatform.model.vo.UserInterfaceInfoVO;
import com.cola.apiopeningplatform.service.UserInterfaceInfoService;
import com.cola.apiopeningplatform.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author cola
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2024-03-08 10:48:06
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.PARAMS_ERROR);
        Long id = userInterfaceInfo.getId();
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if (add) {
            ThrowUtils.throwIf(ObjectUtils.allNull(userId, interfaceInfoId), new BusinessException(ErrorCode.PARAMS_ERROR));
        } else {
            ThrowUtils.throwIf(id <= 0, new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        }
        if (userId <= 0 || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口或用户不存在");
        }
        ThrowUtils.throwIf(leftNum <= 0, new BusinessException(ErrorCode.PARAMS_ERROR, "剩余调用次数不能小于0"));
    }

    @Override
    public Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (userInterfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        Long id = userInterfaceInfoQueryRequest.getId();
        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfoQueryRequest.getTotalNum();
        Integer leftNum = userInterfaceInfoQueryRequest.getLeftNum();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(interfaceInfoId),"interfaceInfoId", interfaceInfoId);
        queryWrapper.between(ObjectUtils.isNotEmpty(totalNum), "totalNum", 0, totalNum);
        queryWrapper.between(ObjectUtils.isNotEmpty(leftNum), "leftNum", 0, leftNum);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<UserInterfaceInfoVO> getUserInterfaceInfoVOPage(Page<UserInterfaceInfo> userInterfaceInfoPage) {
        ThrowUtils.throwIf(userInterfaceInfoPage == null, new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoPage.getRecords();
        Page<UserInterfaceInfoVO> userInterfaceInfoVOPage = new Page<>(userInterfaceInfoPage.getCurrent(),
                 userInterfaceInfoPage.getSize(), userInterfaceInfoPage.getTotal());
        if (CollUtil.isEmpty(userInterfaceInfoList)) {
            return userInterfaceInfoVOPage;
        }
        List<UserInterfaceInfoVO> userInterfaceInfoVOList = userInterfaceInfoList.stream().map(UserInterfaceInfoVO::objToVo).collect(Collectors.toList());
        userInterfaceInfoVOPage.setRecords(userInterfaceInfoVOList);
        return userInterfaceInfoVOPage;
    }


    // TODO 需要加分布式锁, 防止暴点
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("leftNum = leftNum + 1, totalNum = totalNum - 1");
        updateWrapper.gt("leftNum", 0);
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        return this.update(updateWrapper);
    }
}