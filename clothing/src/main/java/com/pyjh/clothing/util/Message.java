package com.pyjh.clothing.util;

public class Message {

    public static String mesTrue(String mesCode, String mesdata, String mesMes) {
        String mes = "{\"code\":" + mesCode + ",\"data\": " + mesdata + ",\"message\":" + mesMes + "}";
        return mes;
    }

    public static String mesFalse(String mesCode, String mesMes) {
        String mes = "{\"code\":" + mesCode + ",\"data\": \"false\",\"message\":" + mesMes + "}";
        return mes;
    }

    public static String mesTrues(String mesCode, String mesMes) {
        String mes = "{\"code\":" + mesCode + ",\"message\":" + mesMes + "}";
        return mes;
    }
}
