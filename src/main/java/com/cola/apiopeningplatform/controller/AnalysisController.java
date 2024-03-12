package com.cola.apiopeningplatform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cola.apiopeningplatform.annotation.AuthCheck;
import com.cola.apiopeningplatform.common.BaseResponse;
import com.cola.apiopeningplatform.common.ErrorCode;
import com.cola.apiopeningplatform.common.ResultUtils;
import com.cola.apiopeningplatform.constant.UserConstant;
import com.cola.apiopeningplatform.exception.BusinessException;
import com.cola.apiopeningplatform.exception.ThrowUtils;
import com.cola.apiopeningplatform.mapper.UserInterfaceInfoMapper;
import com.cola.apiopeningplatform.model.entity.InterfaceInfo;
import com.cola.apiopeningplatform.model.entity.UserInterfaceInfo;
import com.cola.apiopeningplatform.model.vo.InterfaceInfoVO;
import com.cola.apiopeningplatform.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cola.apiopeningplatform.constant.CommonConstant.LIMIT_NUM;

/**
 * 分析控制器
 * @author Maobohe
 * @createData 2024/3/12 19:59
 */

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listGetTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listGetTopInvokeInterfaceInfo(LIMIT_NUM);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        ThrowUtils.throwIf(CollectionUtils.isEmpty(interfaceInfoList), new BusinessException(ErrorCode.SYSTEM_ERROR));
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
            int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);
    }
}
