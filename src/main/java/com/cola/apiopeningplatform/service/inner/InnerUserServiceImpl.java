package com.cola.apiopeningplatform.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cola.apiopeningplatform.common.ErrorCode;
import com.cola.apiopeningplatform.exception.BusinessException;
import com.cola.apiopeningplatform.exception.ThrowUtils;
import com.cola.apiopeningplatform.mapper.UserMapper;
import com.cola.paltformcommon.model.entity.User;
import com.cola.paltformcommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

/**
 *  * 公共服务类实现
 *
 * @author Maobohe
 * @createData 2024/3/11 22:01
 */

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(accessKey), new BusinessException(ErrorCode.PARAMS_ERROR));
        QueryWrapper<com.cola.apiopeningplatform.model.entity.User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        com.cola.apiopeningplatform.model.entity.User invokeUser = userMapper.selectOne(queryWrapper);
        User commonInvokeUser = new User();
        BeanUtils.copyProperties(invokeUser, commonInvokeUser);
        return commonInvokeUser;
    }
}
