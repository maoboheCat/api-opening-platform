package com.cola.apiopeningplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cola.apiopeningplatform.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author cola
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-03-08 10:48:06
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {


    /**
     * 查询调用前limit 的接口信息
     *
     * @param limit
     * @return
     */
    List<UserInterfaceInfo> listGetTopInvokeInterfaceInfo(int limit);

}




