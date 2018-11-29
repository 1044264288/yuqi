package com.pyjh.clothing.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @ClassName: SmsClientSend
 * @Description: 短信发送
 */
public class SmsClientSend {

    public static void main(String[] args) {
//		sendSms("18270928466", "【彭友聚汇】您本次操作的订单信息如下：\n 666，会在24小时内为您发货。\n（彭友聚汇提供技术支持）");
        //String resultString = SmsClientSend.sendSms("18270928466", "【彭友聚汇】您本次操作的订单信息如下：\n 666，会在24小时内为您发货。\n（彭友聚汇提供技术支持）");
        String resultString = SmsClientSend.sendSms("13661734852", "【彭友聚汇】您本次操作的验证码是" + 121386 + "。如非本人操作，请忽略。\n（彭友聚汇提供技术支持）");
        int Rspcode = Tools.SmsClientSendCode(resultString);
        if (Rspcode == 0) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }


    public static String sendSms(String mobile, String msg) {
        System.out.println("mobile:" + mobile + ",\nmsg:" + msg);
        String url = "https://sapi.appsms.cn/msgHttp/json/mt";
        String account = "yuqi106";
        String password = "yuqi1234";
        String content = msg;
        String resultContent = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
        long timestamps = System.currentTimeMillis();

        formparams.add(new BasicNameValuePair("account", account));
        formparams.add(new BasicNameValuePair("password", SecurityUtil.getMD532Str(password + mobile + timestamps)));
        formparams.add(new BasicNameValuePair("mobile", mobile));
        formparams.add(new BasicNameValuePair("content", content));
        formparams.add(new BasicNameValuePair("timestamps", timestamps + ""));

        UrlEncodedFormEntity uefEntity;
        try {
            long start = System.currentTimeMillis();
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                resultContent = EntityUtils.toString(entity, "UTF-8");
            }
            long end = System.currentTimeMillis();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultContent;
    }
}
