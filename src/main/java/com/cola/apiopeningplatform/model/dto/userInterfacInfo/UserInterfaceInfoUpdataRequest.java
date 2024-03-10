package com.cola.apiopeningplatform.model.dto.userInterfacInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口信息更新请求
 * @author Maobohe
 * @createData 2024/3/8 11:07
 */
@Data
public class UserInterfaceInfoUpdataRequest implements Serializable {
    /**
     * 主键id
     */
    private Long id;

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
