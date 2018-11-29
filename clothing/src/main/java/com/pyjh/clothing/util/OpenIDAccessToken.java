package com.pyjh.clothing.util;

import java.io.Serializable;

/**
 * 第二步：通过code换取网页授权access_token 正确返回的JSON数据�?
 * Title:OpenIDAccessToken
 */
public class OpenIDAccessToken implements Serializable {
	// 接口访问凭证
	private String accessToken;
	// 凭证有效期，单位：秒
	private int expiresIn;
	private String refreshToken;
	private String openid;
	private String scope;

	public OpenIDAccessToken() {

	}

	@Override
	public String toString() {
		return "OpenIDAccessToken [accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", refreshToken="
				+ refreshToken + ", openid=" + openid + ", scope=" + scope + "]";
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
