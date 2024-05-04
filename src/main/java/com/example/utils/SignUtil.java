package com.example.utils;


import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.util.HashMap;


public class SignUtil {
    public static   String getSign(HashMap<String, String> map, String secretKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA1);
        String hex = digester.digestHex(map.toString() + "." + secretKey);
        return hex;
    }
}
