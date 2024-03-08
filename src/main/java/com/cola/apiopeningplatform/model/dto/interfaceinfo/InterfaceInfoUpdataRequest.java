package com.cola.apiopeningplatform.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口信息更新请求
 * @author Maobohe
 * @createData 2024/3/1 15:27
 */
@Data
public class InterfaceInfoUpdataRequest implements Serializable {
    /**
     * 主键id
     */
    private Long id;

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

    /**
     * 接口状态 0 - 关闭， 1 - 开启
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

}
