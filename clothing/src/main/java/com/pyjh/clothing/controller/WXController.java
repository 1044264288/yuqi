package com.pyjh.clothing.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pyjh.clothing.util.*;
import com.pyjh.clothing.wxpay.util.ConstantUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.pyjh.clothing.entity.PageData;

import com.pyjh.clothing.service.impl.CustomServiceImpl;

import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller("WXController")
@RequestMapping("wxInfo")
@Api(value = "WXController", description = "扫码授权接口")
public class WXController {

    Logger logger = Logger.getLogger(this.getClass());
    @Resource
    private CustomServiceImpl csi;// 量体信息

    private final String APP_ID = "wxef46d52c146b94f1";  //服务号的ID
    private final String APP_SECRET = "b94ad8eb752cd2aa190b6ce179e94164";  //服务号的密钥
    private OpenIDAccessToken openIDAccessToken = null;

     @ApiOperation("授权获取用户unionid")
    @RequestMapping(value = "/getUserInfo", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json;charset=UTF-8")
    public String getUserInfo(@RequestParam("code") String code,
                              @RequestParam("custom_id") Integer custom_id, ModelMap map,
                              HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("==code=" + code+"\n,custom_id="+custom_id);
        PageData pUserInfo = new PageData();
        String str= "6666666666";
        try {
            openIDAccessToken = UserInfoUtil.getOpenID(APP_ID, APP_SECRET, code);
            System.out.println("openIDAccessToken="+openIDAccessToken);
            if (openIDAccessToken != null) {
                System.out.println("openIDAccessToken!=null");
                // 获取到授权的信息
                pUserInfo = UserInfoUtil.getUserInfo(openIDAccessToken.getAccessToken(), openIDAccessToken.getOpenid());
                System.out.println("pUserInfo"+pUserInfo.getString("unionid"));
                if(!pUserInfo.isEmpty()){
                    pUserInfo.put("id",custom_id);
                    // 给量体信息添加上用户的唯一标识符unionid
                    int row = csi.updateUnionid(pUserInfo);
                    System.out.println("===row==="+row);
                    // 参数拼接,页面跳转
                    String url=""+custom_id;
                    map.put("custom_id", url);
                    System.out.println(str);
                }
            }
            System.out.println("jump");
            return "jump";
        } catch (Exception e) {
            System.out.println("555555555555");
            return "jump";
        }
    }


    @ApiOperation("授权获取用户unionid")
    @RequestMapping(value = "/getUserInfoLogin", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json;charset=UTF-8")
    public String getUserInfoLogin(@RequestParam("code") String code,
                              @RequestParam("member_id") Integer member_id, ModelMap map,
                              HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("==code=" + code+"\n,member_id="+member_id);
        PageData pUserInfo = new PageData();
        String str= "6666666666";
        try {
            openIDAccessToken = UserInfoUtil.getOpenID(APP_ID, APP_SECRET, code);
            System.out.println("openIDAccessToken="+openIDAccessToken);
            if (openIDAccessToken != null) {
                System.out.println("openIDAccessToken!=null");
                // 获取到授权的信息
                pUserInfo = UserInfoUtil.getUserInfo(openIDAccessToken.getAccessToken(), openIDAccessToken.getOpenid());
                System.out.println("pUserInfo"+pUserInfo.getString("unionid"));
                if(!pUserInfo.isEmpty()){
                    pUserInfo.put("member_id",member_id);
                    // 给量体信息添加上用户的唯一标识符unionid
                    int row = csi.updateUnionid(pUserInfo);
                    System.out.println("===row==="+row);
                    // 参数拼接,页面跳转
                    String url=""+member_id;
                    map.put("member_id", url);
                    System.out.println(str);
                }
            }
            System.out.println("jump");
            return "jump";
        } catch (Exception e) {
            System.out.println("555555555555");
            return "jump";
        }
    }


    @ApiOperation("通过量体信息id获取二维码")
    @RequestMapping(value = "/customQrCode", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object customQR_code(@RequestParam("custom_id") Integer custom_id,
                                HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (CommonUtil.paramIsNull(custom_id)) {
            return ResponseEnum.ARGUMENT_IS_NULL.toString();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        PageData pd = new PageData();
        pd.put("id", custom_id);
        try {
            pd = csi.findforEntity(pd);
            if (pd==null) {
                return ResponseEnum.SERVER_DATA_NOTEXIST.toString();
            }else{
                // 生成二维码
                String url = ZXingCode.qrCodeAddress(
                        "http://cdn.pengyoujuhui.com/dfydz/dfydz.jpg",
                        "http://www.yuqibest.com/clothing/authorization.html?custom_id="+custom_id,
                        "东方云定制"
                );
                data.put("url",url);
                //  http://dns.pengyoujuhui.com/vote/QrCode/e1cda2cd-7682-46a7-a127-9012e45c510c
            }
        } catch (Exception e) {
            logger.warn(e);
            return ResponseEnum.SERVER_SQL_ERROR.toString();
        }
        return ResponseEnum.SUCCESS.appendObject(data);
    }

    @ApiOperation("获取所有无用户认证的量体信息")
    @RequestMapping(value = "/queryAllCustom", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object queryAllCustom(HttpServletResponse response,String type) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> data = new HashMap<String, Object>();
        PageData pd = new PageData();
        pd.put("type",type);
        try {
            List<PageData> listCustom= csi.findforList(pd);
            if (listCustom==null) {
                return ResponseEnum.SERVER_DATA_NOTEXIST.toString();
            }else{
                data.put("listCustom",listCustom);
            }
        } catch (Exception e) {
            logger.warn(e);
            return ResponseEnum.SERVER_SQL_ERROR.toString();
        }
        return ResponseEnum.SUCCESS.appendObject(data);
    }





    @ApiOperation("获取openid")
    @RequestMapping(value = "/getOpenId", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getOpenId(String code) {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session"; // 请求地址
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", "wx6c7308efbdc7abc5"); // 开发者设置中的appId
        requestUrlParam.put("secret", "6a0941f8df151c39ddb8c9afd04a434f"); // 开发者设置中的appSecret
        requestUrlParam.put("js_code", code); // 小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code"); // 默认参数
        /**
         * 发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session
         * 接口获取openid用户唯一标识
         */
        JSONObject jsonObject = JSON.parseObject(sendPost(requestUrl, requestUrlParam));
        System.out.println("openId" + jsonObject);
        return jsonObject;
    }

    /**
     * 解密并且获取用户手机号码
     */
    @RequestMapping(value = "/deciphering", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object deciphering(String encrypdata, String ivdata, String sessionkey) {
        try {
            String result = AesCbcUtil.decrypt(encrypdata, sessionkey, ivdata, "UTF-8");
            if (CommonUtil.paramIsNull(result)) {
                return ResponseEnum.SERVER_DATA_NOTEXIST.toString();
            }
            PageData pd = KdniaoTrackQueryAPI.json2map(result);
            return ResponseEnum.SUCCESS.appendObject(pd);
        } catch (Exception e) {
            logger.warn(e);
            return ResponseEnum.SERVER_SQL_ERROR.toString();
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * @param url
     * @param paramMap
     * @return
     */
    public String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.warn(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }









}
