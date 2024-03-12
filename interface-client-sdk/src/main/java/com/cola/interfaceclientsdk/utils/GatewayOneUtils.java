package com.cola.interfaceclientsdk.utils;


import javax.servlet.http.HttpServletRequest;

/**
 * 检查网关1，下游流量
 * @author Maobohe
 * @createData 2024/3/12 16:09
 */
public class GatewayOneUtils {

    private static final String GATEWAY_ONE_CHECK = "maobohe_one";

    public static boolean flowDyeingCheck(HttpServletRequest request) {
        String gatewayOne = request.getHeader("gateway_one");
        return GATEWAY_ONE_CHECK.equals(gatewayOne);
    }
}
