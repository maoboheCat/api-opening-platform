package com.cola.apiopeningplatform.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口创建请求
 * @author Maobohe
 * @createData 2024/3/1 14:57
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 接口类型
     */
    private String method;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    private static final long serialVersionUID = 1L;
}
