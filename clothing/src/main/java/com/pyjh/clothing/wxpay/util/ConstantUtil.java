package com.pyjh.clothing.wxpay.util;

/**
 * @version V1.0
 * @Title: ConstantUtil.java
 * @Package com.yuqi.pyjh.controller.web.pay.wxpay.util
 * @Description: 被注释的都在数据库merchants中
 * @author: LiAn
 * @date: 2018年7月24日 下午12:09:58
 */
public class ConstantUtil {
    /**
     * 微信开发平台应用ID
     */
    public static final String APP_ID = "wx6c7308efbdc7abc5";
    /**
     * 应用对应的凭证
     */
    public static final String APP_SECRET = "6a0941f8df151c39ddb8c9afd04a434f";
    /**
     * 应用对应的密钥,商家密钥
     */
    public static final String APP_KEY = "dongzfangxyuncdingazhis2018800as";
    /**
     * 微信支付商户号
     */
    public static final String MCH_ID = "1512226241";
    /**
     * 商品描述
     */
    public static final String BUY_BODY = "东方云定制商城付款";
    public static final String RECHARGE_BODY="东方云定制商城充值";
    public static final String CONSTANT_BODY="东方云定制商城付款";

    /**
     * 商户号对应的密钥
     */
//    public static final String PARTNER_key="123466";

    /**
     * 商户id
     */
    //public static final String PARTNER_ID="1495277322";
    /**
     * 常量固定值
     */
    public static final String GRANT_TYPE = "client_credential";
    /**
     * 获取预支付id的接口url
     */
    public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 微信服务器回调通知url
     */
    // public static String NOTIFY_URL="https://www.pengyoujuhui.com/wxpyjh1/pay/api/weixin/notify.action";

    public static String NOTIFY_URL = "https://www.pengyoujuhui.com/clothing/notfiy/api/weixin/notify";

}