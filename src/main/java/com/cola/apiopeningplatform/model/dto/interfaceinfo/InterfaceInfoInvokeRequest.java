package com.cola.apiopeningplatform.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 * @author Maobohe
 * @createData 2024/3/7 15:27
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;

}
