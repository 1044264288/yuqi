package com.pyjh.clothing.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.pyjh.clothing.util.OpenIDAccessToken;
import com.pyjh.clothing.entity.PageData;

/**
 * 提供通过code换取网页授权的方�? 提供刷新accesstoken的方�? 提供获取用户信息的方�? 获取关注粉丝的列�?
 * 
 * @author Administrator
 *
 */
public class UserInfoUtil {
	// 第二步：通过code换取网页授权access_token
	// getopenid,refresh_token,getuserinfo,getuserlist
	private final static String GETOPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	// 第三步：刷新access_token（如果需要）
	private final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN ";
	// 第四步：拉取用户信息
	private final static String GETUSERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	// 获取帐号的关注
	private final static String GETUSERLIST_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
	
	//获取普通access_token这个值
	private final static String GETACCESS_TOKEN="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 获取普通access_token,然后获取用户基本信息
	private final static String GETUSERINFO_URLTWO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/**
	 * 第二步：通过code换取网页授权access_token
	 * 
	 * @param appid
	 * @param appsecret
	 * @param code
	 * @return
	 */
	public static OpenIDAccessToken getOpenID(String appid, String appsecret, String code) {
		String requestUrl = GETOPENID_URL.replace("APPID", appid) //
				.replace("SECRET", appsecret) //
				.replace("CODE", code);
		JsonNode rootNode = AccessTokenUtil.httpsRequest(requestUrl, "GET", null);
		OpenIDAccessToken openIDAccessToken = null;
		if (null != rootNode.get("access_token")) {
			openIDAccessToken = new OpenIDAccessToken();
			openIDAccessToken.setAccessToken(rootNode.get("access_token").textValue());
			openIDAccessToken.setExpiresIn(AccessTokenUtil.toInt(rootNode.get("expires_in").toString()));
			openIDAccessToken.setRefreshToken(rootNode.get("refresh_token").textValue());
			openIDAccessToken.setOpenid(rootNode.get("openid").textValue());
			openIDAccessToken.setScope(rootNode.get("scope").textValue());
		}
		return openIDAccessToken;
	}

	/**
	 * 第三步：刷新access_token（如果需要）
	 * 
	 * @param appid
	 * @param refresh_token
	 * @return
	 */
	public static OpenIDAccessToken refreshToken(String appid, String refresh_token) {
		String requestUrl = REFRESH_TOKEN_URL.replace("APPID", appid) //
				.replace("REFRESH_TOKEN ", refresh_token);
		JsonNode rootNode = AccessTokenUtil.httpsRequest(requestUrl, "GET", null);
		OpenIDAccessToken openIDAccessToken = null;
		if (null != rootNode.get("access_token")) {
			openIDAccessToken = new OpenIDAccessToken();
			openIDAccessToken.setAccessToken(rootNode.get("access_token").textValue());
			openIDAccessToken.setExpiresIn(AccessTokenUtil.toInt(rootNode.get("expires_in").toString()));
			openIDAccessToken.setRefreshToken(rootNode.get("refresh_token").textValue());
			openIDAccessToken.setOpenid(rootNode.get("openid").textValue());
			openIDAccessToken.setScope(rootNode.get("scope").textValue());
		}
		return openIDAccessToken;
	}

	/**
	 * 第四步：拉取用户信息(�?scope�? snsapi_userinfo)
	 *
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static PageData getUserInfo(String access_token, String openid) {
		String requestUrl = GETUSERINFO_URL.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
		System.out.println();
		JsonNode rootNode = AccessTokenUtil.httpsRequest(requestUrl, "GET", null);
		PageData pUserInfo = new PageData();
		System.out.println("======1111111111=======");
		System.out.println("======nickname=="+rootNode.get("nickname"));
		System.out.println("======2222222222=======");
		if (null != rootNode.get("nickname")) {
			pUserInfo.put("openid",rootNode.get("openid").textValue());
			System.out.println("======openid=="+rootNode.get("openid"));
/*       pUserInfo.put("nickname",rootNode.get("nickname").textValue());
         pUserInfo.put("sex",rootNode.get("sex").textValue());
         pUserInfo.put("province",rootNode.get("province").textValue());
         pUserInfo.put("city",rootNode.get("city").textValue());
         pUserInfo.put("country",rootNode.get("country").textValue());
         pUserInfo.put("headimgurl",rootNode.get("headimgurl").textValue());*/
			pUserInfo.put("unionid",rootNode.get("unionid").textValue());
			System.out.println("======unionid=="+rootNode.get("unionid"));
		}
		return pUserInfo;
	}

	/**
	 * 获取普通ACCESS_TOKEN
	 * 
	 * */
	public static String getAccess_tikenPu(String appid,String appsecret){
		String requestUrl = GETACCESS_TOKEN.replace("APPID", appid) //
				.replace("APPSECRET", appsecret); //
		JsonNode rootNode = AccessTokenUtil.httpsRequest(requestUrl, "GET", null);
		String Access_tikenPu=rootNode.get("access_token").textValue();
		return Access_tikenPu;
	}

	/**
	 * 
	 * 获取普通用户信息（用来判断用户是否关注微信公众号）
	 */
	public static PageData getUserInfoTwo(String access_token, String openid) {
		String requestUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		System.out.println("地址2==================="+requestUrl);
		JsonNode rootNode = AccessTokenUtil.httpsRequest(requestUrl, "GET", null);
		System.out.println("rootNode值==============="+rootNode);
		PageData pUserInfo = new PageData();
		System.out.println("subscribe值=========================="+rootNode.get("subscribe"));
		pUserInfo.put("Subscribe",rootNode.get("subscribe").intValue());
		return pUserInfo;
	}
}
