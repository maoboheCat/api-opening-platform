package com.cola.apiopeningplatform.model.dto.userInterfacInfo;


import com.cola.apiopeningplatform.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Maobohe
 * @createData 2024/3/1 15:11
 */
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 主键id
     */
    private Long id;

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
