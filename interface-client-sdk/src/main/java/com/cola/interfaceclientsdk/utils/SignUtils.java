package com.cola.interfaceclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author Maobohe
 * @createData 2024/3/3 16:27
 */
public class SignUtils {
    public static String getSisn(String body, String secretKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return digester.digestHex(content);
    }
}
