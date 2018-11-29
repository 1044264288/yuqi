package com.pyjh.clothing.wxpay.util;

import java.util.Random;

public class WXUtil {
    /**
     * 生成随机字符串:
     * 2cb6b10338a7fc4117a80da24b582060
     *
     * @return
     */
    public static String getNonceStr() {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "utf8");
    }

    /**
     * 获取时间戳:
     * 1529564491
     *
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static void main(String[] args) {
        System.out.println("1:" + getNonceStr());
        System.out.println("2:" + getTimeStamp());
        //1:aa78c3db4fc4a1a343183d6113ec46ba
        //2:1529564491
    }
}
