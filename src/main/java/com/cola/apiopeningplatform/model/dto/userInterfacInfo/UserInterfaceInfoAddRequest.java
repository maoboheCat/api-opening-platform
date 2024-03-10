package com.cola.apiopeningplatform.model.dto.userInterfacInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口创建请求
 * @author Maobohe
 * @createData 2024/3/8 11:07
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

    /**
     * 调用用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 接口状态 0 - 正常， 1 - 禁用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
