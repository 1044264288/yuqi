package com.pyjh.clothing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.AddressService;
import com.pyjh.clothing.service.OrderService;
import com.pyjh.clothing.util.*;
import com.pyjh.clothing.wxpay.hander.PrepayIdRequestHandler;
import com.pyjh.clothing.wxpay.util.ConstantUtil;
import com.pyjh.clothing.wxpay.util.MD5Util;
import com.pyjh.clothing.wxpay.util.WXUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("address")
@Api(value = "address",description = "收货地址接口")
public class AddressController extends CodeMessage {

    @Resource
    AddressService addressService;

    @ApiOperation("获取用户购物车")
    @RequestMapping(value = "/getMemberAddress", method = RequestMethod.GET)
    public String getMemberAddress(Integer memberId) {
        if (memberId == null) {
            return Message.mesFalse(code_400, message_400);
        }
        List<PageData> shopCartList = addressService.getMemberAddress(memberId);
        if (shopCartList != null) {
            return Message.mesTrue(code_200, JSON.toJSONString(shopCartList), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("新增收获地址")
    @RequestMapping(value = "/addMemberAddress", method = RequestMethod.POST)
    public String addMemberAddress(String address) {
        if (address == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = JSONObject.parseObject(address, PageData.class);
        System.out.println(JSON.toJSONString(pageData));
        Integer res = addressService.addMemberAddress(pageData);
        if (res > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(res), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("修改购物车")
    @RequestMapping(value = "/modifyMemberShopCart", method = RequestMethod.POST)
    public String modifyMemberShopCart(String address) {
        if (address == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = JSONObject.parseObject(address, PageData.class);
        Integer res = addressService.editMemberAddress(pageData);
        if (res > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(res), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("删除收货地址")
    @RequestMapping(value = "/deleteMemberAddress", method = RequestMethod.POST)
    public String deleteMemberAddress(Integer addressId) {
        if (addressId == null) {
            return Message.mesFalse(code_400, message_400);
        }
        Integer res = addressService.deleteMemAddress(addressId);
        if (res > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(res), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @Controller("WXRechargePay")
    @RequestMapping("/wx/pay")
    public static class RechargeWeiXinPayController {

        @Autowired
        private OrderService orderService; // 订单

        /**
         * 余额充值 微信支付
         *
         * @param member_id      用户id int
         * @param recharge_way   充值方式 String
         * @param recharge_price 充值金额 Double
         * @param openid         用户的唯一标识 String
         * @return map
         */
        @RequestMapping(value = "/createRecharge", method = {RequestMethod.POST, RequestMethod.GET})
        @ResponseBody
        public Map<String, Object> alipay(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @Param("openid") String openid,
                                          @Param("member_id") Integer member_id,
                                          @Param("recharge_way") String recharge_way,
                                          @Param("recharge_price") Double recharge_price) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            Map<String, Object> model = new HashMap<String, Object>();
            if (CommonUtil.paramIsNull(member_id) ||
                    CommonUtil.paramIsNull(recharge_way) || CommonUtil.paramIsNull(openid) ||
                    CommonUtil.paramIsNull(recharge_price)) {
                model.put("msg", "参数为空");
                model.put("status", "402");
                return model;
            }

            Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
            String nonce_str = WXUtil.getNonceStr();
            try {
                /****** 1.封装你的交易订单开始 *****/ // 自己用

                // 生成订单号，唯一,例：201807021607465eaa88330
                String recharge_number = OrderNoUtil.getOrderNo();
                PageData pOne = new PageData();
                while (true) {
                    pOne.put("orderCode", recharge_number);
                    List<PageData> order = orderService.getOrder(pOne);
                    if (order.isEmpty()) {
                        break;
                    }
                    recharge_number = OrderNoUtil.getOrderNo();
                }
                PageData pRecharge = new PageData();
                pRecharge.put("create_member_id", member_id);
                pRecharge.put("recharge_way", recharge_way);
                pRecharge.put("recharge_price", recharge_price);
                pRecharge.put("recharge_number", recharge_number);
                pRecharge.put("create_time", DateUtil.getTime());
                pRecharge.put("status", 0);
                // 插入充值记录表
                int rowR = orderService.addRecharge(pRecharge);
                System.out.println("插入充值记录表row:" + rowR);
                // 生成订单
                PageData pOrder = new PageData();
                pOrder.put("memberId", member_id);
                pOrder.put("addressId", -2);//地址id(>0：送货上门，0：自提,-1：联系我们，-2：我要充值)
                pOrder.put("payWay", recharge_way);
                pOrder.put("order_code", recharge_number);
                pOrder.put("order_price", recharge_price);
                pOrder.put("status", "P");// 未支付
                pOrder.put("create_time", DateUtil.getTime());
                // 生成订单
                int rowO = orderService.addOrder(pOrder);
                // System.out.println("插入订单表row:" + rowO);

                /****** 1.封装你的交易订单结束 *****/

                // ---------------2 生成订单号 开始------------------------

                String timestamp = WXUtil.getTimeStamp(); // 超时时间

                // 统一下单开始,获取商户的常量 *****

                // ***** -----------------------------------------------------------
                // 3.获取生成预支付订单的请求类
                PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);
                prepayReqHandler.setParameter("appid", ConstantUtil.APP_ID); // 平台应用appId
                prepayReqHandler.setParameter("mch_id", ConstantUtil.MCH_ID); // 商户号
                prepayReqHandler.setParameter("nonce_str", nonce_str); // 随机字符串
                prepayReqHandler.setParameter("body", ConstantUtil.BUY_BODY); // 商品描述
                prepayReqHandler.setParameter("out_trade_no", recharge_number); // 订单号
                prepayReqHandler.setParameter("total_fee", String.valueOf(recharge_price * 100).split("\\.")[0]); // 订单价格
                prepayReqHandler.setParameter("spbill_create_ip", request.getRemoteAddr()); // 获取客户端ip
                prepayReqHandler.setParameter("notify_url", ConstantUtil.NOTIFY_URL); // 回调通知
                prepayReqHandler.setParameter("trade_type", "JSAPI"); // 支付类型
                prepayReqHandler.setParameter("openid", openid); // 用户的唯一标识
                // prepayReqHandler.setParameter("time_start", timestamp); //时间戳
                prepayReqHandler.setGateUrl(ConstantUtil.GATEURL); // 设置预支付id的接口url

                // 3.3
                // 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
                prepayReqHandler.setParameter("sign", prepayReqHandler.createMD5Sign(ConstantUtil.APP_KEY)); // sign
                // 签名

                // 3.4 提交预支付,获取prepayid
                String prepayid = prepayReqHandler.sendPrepay();
                // ---------------------------------------------- ***** 统一下单 结束
                // *****
                // --------------------------------------------------------------

                // 3.5 若获取prepayid成功，将相关信息返回客户端
                if (prepayid != null && !prepayid.equals("")) {

                    // ---------------4.封装订单数据开始 ------------------------

                    String signs = "appId=" + ConstantUtil.APP_ID + "&nonceStr=" + nonce_str + "&package=prepay_id="
                            + prepayid + "&signType=MD5" + "&timeStamp=" + timestamp + "&key=" + ConstantUtil.APP_KEY;

                    resultMap.put("appid", ConstantUtil.APP_ID);
                    resultMap.put("partnerid", ConstantUtil.MCH_ID); // 商家id
                    resultMap.put("prepayid", prepayid); // 预支付id
                    resultMap.put("package", "Sign=WXPay"); // 固定常量
                    resultMap.put("noncestr", nonce_str); // 与请求prepayId时值一致
                    resultMap.put("timestamp", timestamp); // 等于请求prepayId时的time_start
                    resultMap.put("sign", MD5Util.MD5Encode(signs, "utf8").toUpperCase());// 签名方式与上面类似
                    model.put("orderNum", recharge_number);
                    model.put("resultMap", resultMap);
                    model.put("msg", "获取prepayid成功,生成订单成功");
                    model.put("status", "200");
                } else {
                    model.put("msg", "获取prepayid失败");
                    model.put("status", "210");
                }

            } catch (Exception e) {
                //logger.warn(e);
                model.put("msg", "订单生成失败");
                model.put("status", "444");
            }
            return model;
        }

    }
}
