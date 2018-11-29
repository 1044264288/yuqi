package com.pyjh.clothing.controller;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import  net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/aesc")
@Api(value = "AESC", description = "解密用户敏感数据")
public class AESController {
    protected Logger logger = Logger.getLogger(this.getClass());
  /**
   * 解密用户敏感数据
   *
   * @param encrypdata 明文,加密数据
   * @param ivdata      加密算法的初始向量
   * @param sessionkey     用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和 session_key
   * @return
           */
    @ApiOperation("微信解密")
    @ResponseBody
    @RequestMapping(value = "/decodeUser", method = RequestMethod.POST)


    public Object deciphering(String encrypdata, String ivdata, String sessionkey, HttpServletRequest request) {
        String result = null;
        PageData pd = new PageData();
        try {
            result = AesCbcUtil.decrypt(encrypdata, sessionkey, ivdata, "UTF-8");
            if (CommonUtil.paramIsNull(result)) {
                return ResponseEnum.SERVER_DATA_NOTEXIST.toString();
            }
            pd = KdniaoTrackQueryAPI.json2map(result);
        } catch (Exception e) {
            logger.warn(e);
            return ResponseEnum.SERVER_SQL_ERROR.toString();
        }
        return ResponseEnum.SUCCESS.appendObject(pd);
    }

    public Map decodeUser(String encryptedData, String iv, String code) {

        Map map = new HashMap();

        //登录凭证不能为空
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return map;
        }

        //小程序唯一标识  (在微信小程序管理后台获取)
        String wxspAppid = "wx6c7308efbdc7abc5";
        //小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "6a0941f8df151c39ddb8c9afd04a434f";
        //授权（必填）
        String grant_type = "authorization_code";


        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
        //请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
        //发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        //解析相应内容（转换成json对象）
        // json= JSON.toJSONString(sr);
        JSONObject json = JSONObject.fromObject(sr);
        //获取会话密钥（session_key）
        String session_key = json.get("session_key").toString();
        //用户的唯一标识（openid）
        String openid = (String) json.get("openid");

        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
        try {
            String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");

                JSONObject userInfoJSON = JSONObject.fromObject(result);
                Map userInfo = new HashMap();
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                userInfo.put("unionId", userInfoJSON.get("unionId"));
                map.put("userInfo", userInfo);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", 0);
        map.put("msg", "解密失败");
        return map;
    }
}
