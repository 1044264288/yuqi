package com.pyjh.clothing.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.qiniu.common.*;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.util.*;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class QiniuCloudUtil {
    // 设置需要操作的账号的AK和SK
    private static final String ACCESS_KEY = "ae225bEMVXTuoYVo8cjlPOjR0HU9sQiN6HQpPf7K";
    private static final String SECRET_KEY = "HsWKB4ZONEDtXbRUy_cg0e7UqQ4dj7OiOBKrEUUY";

    // 要上传的空间
    public static final String bucketname = "cbofer";

    // 密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    // 外链网址
    private static final String DOMAIN = "http://dns.pengyoujuhui.com";

    // private static final String style = "自定义的图片样式";

    /**
     * 获取令牌（无参数）
     */
    public static String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }

    /**
     * 获取令牌
     */
    public static String getUpToken(String saveKey) {
        return auth.uploadToken(bucketname, null, 3600,
                new StringMap().put("insertOnly", 1).putNotEmpty("saveKey", saveKey), true);
    }

    // base64方式上传
    public static String put64image(byte[] base64, String key) throws Exception {
        String file64 = Base64.encodeToString(base64, 0);
        Integer l = base64.length;
        String url = "http://upload.qiniu.com/putb64/" + l + "/key/" + UrlSafeBase64.encodeToString(key);
        // 非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken()).post(rb).build();
        // System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);
        // 如果不需要添加图片样式，使用以下方式
        return DOMAIN + "/" + key;
        // return DOMAIN + key + "?" + style;
    }

    // 普通删除
    public int delete(String key) throws IOException {
        int num = 0;
        // 实例化一个BucketManager对象
        Configuration cfg = new Configuration(Zone.zone0());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        key = key.substring(24);
        System.out.println("===================" + key);
        try {
            // 调用delete方法移动文件
            bucketManager.delete(bucketname, key);
            System.out.println("删除成功");
            num = 1;
        } catch (QiniuException e) {
            // 捕获异常信息
            Response r = e.response;
            System.out.println("删除失败");
            System.out.println(r.toString());
            num = -1;
        }
        return num;
    }

    /**
     * 实时监测
     */
    public String upload(String path, String name) throws IOException {
        try {
            // 调用put方法上传
            Configuration cfg = new Configuration(Zone.zone0());
            UploadManager uploadManager = new UploadManager(cfg);
            Response res = uploadManager.put(path, name, getUpToken());
            // 打印返回的信息
            System.out.println(res.bodyString());
            System.out.println("上传成功" + DOMAIN + "/" + name);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return DOMAIN + "/" + name;
    }

    public void down() {
        String fileName = "syncoPhoto/kavo/00c656c8-754c-4ea4-a4bf-eaa5d12e4e38";
        String domainOfBucket = "http://cdn.synconize.com";
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
            System.out.println(finalUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
