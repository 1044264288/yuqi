package com.pyjh.clothing.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;





public enum ResponseEnum {
	SUCCESS("200", "成功"), 
	ACCOUNTS_EXIST_NO("201", "帐号不存在"),
	ACCOUNTS_EXIST_YES("202", "该手机号已注册"),
	PASSWORD_MISTAKE("203", "密码错误"),
	FAIL("210", "失败"), 
	CAPTCHA_ERROR("204", "验证码错误"), 
	InvitationCode_EXIST_NO("205", "邀请码不存在"),
	ERROR_PASSWORD("206", "账号或密码错误"),
	ERROR_PAY_PASSWORD("208", "账号或密码错误"),
	ACCOUNTS_FORBIDDEN("207", "该账号已被禁用，请尽快联系彭友！"),
	ARGUMENT_ERROR("400","参数错误"), 
	ARGUMENT_EXCEPTION("401", "参数存在异常"), 
	ARGUMENT_TOKEN_EMPTY("409", "Token为空"), 
	ARGUMENT_TOKEN_INVALID("410", "Token无效"), 
	SERVER_ERROR("501", "服务端异常"), 
	ARGUMENT_IS_NULL("402", "参数为空"), 
	SERVER_SQL_ERROR("503","数据库操作出现异常"), 
	DATA_OPERATION_FAILED("344","数据操作失败"),
	SERVER_DATA_REPEAT("504", "服务器数据已存在"), 
	SERVER_DATA_NOTEXIST("505","数据不存在"), 
	SERVER_DATA_STATUS_ERROR("506", "数据状态错误"), 
	SERVER_SMS_SEND_FAIL("701", "短信发送失败"), 
	SEND_EMAIL_YES("702", "邮件发送成功"), 
	DELECT_SHOP_CART("121","购物车商品删除成功"),
	DELECT_SHOP_CART_ERROR("122","购物车商品删除失败"),
	UPDATE_SHOP_CARTNUMBER("123","修改商品数量成功"),
	UPDATE_SHOP_CARTNUMBER_ERROR("124","修改商品数量失败"),
	INSERT_MEMBER_REACHED("003","该邀请码的邀请人数已满"),
	SESSION_IS_NULL("909","session为空"),
	POINT_NO_ENOUGH("900","积分不足"),
	MONEY_AMOUNT_EXCEPTION("901","金额数量异常"),
	GET_PREPAYID_FILE("902","获取prepayid失败"),
	NOT_LOGIN("407","账号未登录");
	

	private String code;
	private String message;

	private ResponseEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String toString() {
		Map rsMap = new HashMap();
		rsMap.put("code", Integer.valueOf(this.code));
		rsMap.put("msg", this.message);
		JSONObject jsonNode = JSONObject.fromObject(rsMap);
		return jsonNode.toString();
	}

	/**
	 * 已过期，统一使用appendObject
	 * @param appendMap
	 * @return
	 */
	@Deprecated
	public String appendMapToString(Map<String, Object> appendMap) {
		Map rsMap = new HashMap();
		rsMap.put("data", appendMap);
		rsMap.put("code", code);
		rsMap.put("msg", this.message);
		JSONObject jsonNode = JSONObject.fromObject(rsMap);
		return jsonNode.toString();
	}
	

	public String appendObject(Object obj) {
		if(obj == null){
			obj = new HashMap();
		}

		Map rsMap = new HashMap();
		rsMap.put("code", code);
		rsMap.put("msg", this.message);
		rsMap.put("data", obj);
		
		return JsonUtil2.toJson(rsMap);
	}
	
	public String appendObject(String message, Object obj) {
		if(obj == null){
			obj = new HashMap();
		}
		
		Map rsMap = new HashMap();
		rsMap.put("code", code);
		rsMap.put("msg", message);
		rsMap.put("data", obj);
		
		return JsonUtil2.toJson(rsMap);
	}



	public String getCode() {
		return this.code;
	}
	
	public String getMsg() {
		return this.message;
	}

}