package com.cola.apiopeningplatform.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cola.apiopeningplatform.common.ErrorCode;
import com.cola.apiopeningplatform.exception.BusinessException;
import com.cola.apiopeningplatform.exception.ThrowUtils;
import com.cola.apiopeningplatform.mapper.InterfaceInfoMapper;
import com.cola.paltformcommon.model.entity.InterfaceInfo;
import com.cola.paltformcommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

/**
 * @author Maobohe
 * @createData 2024/3/11 22:02
 */

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(path, method), new BusinessException(ErrorCode.PARAMS_ERROR));
        QueryWrapper<com.cola.apiopeningplatform.model.entity.InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("method", method);
        queryWrapper.eq("url", path);
        com.cola.apiopeningplatform.model.entity.InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        InterfaceInfo commentInterfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfo, commentInterfaceInfo);
        return commentInterfaceInfo;
    }
}
