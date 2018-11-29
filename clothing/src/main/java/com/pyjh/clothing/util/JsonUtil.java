package com.pyjh.clothing.util;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;

/**
 * @author LiQian
 */
public class JsonUtil {
    public static String getJsonPojo(String json) {
        // 提取JOSN格式化
        JSONObject jsonResult = JSONObject.parseObject(json);
        Object r = jsonResult.get("data");
        if (r == null) {
            return null;
        }
        String dataJson = r.toString();
        return dataJson;
    }

    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
