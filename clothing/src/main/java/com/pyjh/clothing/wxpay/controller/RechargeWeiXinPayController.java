package com.pyjh.clothing.wxpay.controller;

import com.pyjh.clothing.service.*;
import com.pyjh.clothing.wxpay.hander.PrepayIdRequestHandler;
import com.pyjh.clothing.wxpay.util.ConstantUtil;
import com.pyjh.clothing.wxpay.util.MD5Util;
import com.pyjh.clothing.wxpay.util.WXUtil;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.util.CommonUtil;
import com.pyjh.clothing.util.DateUtil;
import com.pyjh.clothing.util.OrderNoUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("RechargeWeiXinPay")
@Api(value = "recharge",description = "余额充值接口")
public class RechargeWeiXinPayController {

    @Autowired
    private OrderService orderService; // 订单
    @Autowired
    private RechargeService rechargeService;//充值记录表
    @Autowired
    private Balance_pay_amountService balance_pay_amountService;//充值选项表
    @Resource
    CouponsService couponsService;// 优惠券
    @Resource
    ExchangeService exchangeService;

    /**
     * 余额充值 微信支付
     *
     * @param member_id      用户id int
     * @param recharge_way   充值方式 String
     * @param  amount        面额
     * @param openid         用户的唯一标识 String
     * @return map
     */
    @RequestMapping(value = "/createRecharge", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> alipay(HttpServletRequest request,HttpServletResponse response,
                                      String openid,
                                      Integer id,
                                      Integer member_id,
                                      String recharge_way,
                                      Integer amount) {
        System.out.println(openid+id+member_id+recharge_way+amount);
        Map<String, Object> model = new HashMap<String, Object>();
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (CommonUtil.paramIsNull(openid) ||
                CommonUtil.paramIsNull(id) ||
                CommonUtil.paramIsNull(member_id) ||
                CommonUtil.paramIsNull(recharge_way) ||
                CommonUtil.paramIsNull(amount)) {
            model.put("msg", "参数为空");
            model.put("status", "402");
            return model;
        }
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String nonce_str = WXUtil.getNonceStr();
        try {
            /****** 1.封装你的交易订单开始 *****/ // 自己用
            // 通过充值选项表id，查询
            PageData pd = new PageData();
            pd.put("id",id);
            pd = balance_pay_amountService.findForEntity(pd);
            int priceF = 0;// 需要付的钱
            int priceD = 0;// 充值到账的钱
            if(pd.getInteger("status")==0){
                priceD = pd.getInteger("amount")+pd.getInteger("given_money");
                priceF = pd.getInteger("amount");
            }else{
                priceD=pd.getInteger("amount");
                priceF=pd.getInteger("amount")*pd.getInteger("discount");
            }
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
            pRecharge.put("amount", amount);
            pRecharge.put("recharge_number", recharge_number);
            pRecharge.put("create_time", DateUtil.getTime());
            pRecharge.put("status", 0);
            // 插入充值记录表
            int rowR =rechargeService.insertrecharge_priceByrecharge(pRecharge);
            System.out.println("插入充值记录表row:" + rowR);

            // 生成订单
            PageData pOrder = new PageData();
            pOrder.put("member_id", member_id);
            pOrder.put("address_id", -2);//地址id(>0：送货上门，0：自提,-1：联系我们，-2：我要充值)
            pOrder.put("pay_way", recharge_way);
            pOrder.put("order_code", recharge_number);
            pOrder.put("order_price", amount);
            pOrder.put("status", 1);// 未支付
            pOrder.put("create_time", DateUtil.getTime());
            // 生成订单
            int rowO = orderService.addOrder(pOrder);
            System.out.println("插入订单表row:" + rowO);

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
            prepayReqHandler.setParameter("total_fee", String.valueOf(amount * 100).split("\\.")[0]); // 订单价格
            prepayReqHandler.setParameter("spbill_create_ip", "127.0.0.1"); // 获取客户端ip
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



    /**
     * 立即购买 微信支付
     *
     * @param member_id      用户id int
     * @param recharge_way   充值方式 String
     * @param  order_price        面额
     * @param openid         用户的唯一标识 String
     * @return map
     */
    @RequestMapping(value = "/createRechargeorder", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> alipayl(HttpServletRequest request,HttpServletResponse response,
                                       String openid,
                                       Integer id,
                                       Integer member_id,
                                       String recharge_way,
                                       Double order_price,
                                       Integer address_id
    ) {
        System.out.println(openid+id+member_id+recharge_way+order_price+address_id);
        Map<String, Object> model = new HashMap<String, Object>();
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (CommonUtil.paramIsNull(openid) ||
                CommonUtil.paramIsNull(id) ||
                CommonUtil.paramIsNull(member_id) ||
                CommonUtil.paramIsNull(recharge_way) ||
                CommonUtil.paramIsNull(order_price) ||
                CommonUtil.paramIsNull(address_id)
        ) {
            model.put("msg", "参数为空");
            model.put("status", "402");
            return model;
        }
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String nonce_str = WXUtil.getNonceStr();
        try {
            /****** 1.封装你的交易订单开始 *****/ // 自己用
            // 通过充值选项表id，查询
            PageData pd = new PageData();
            pd.put("id",id);
            pd = balance_pay_amountService.findForEntity(pd);
         /*   int priceF = 0;// 需要付的钱
           int priceD = 0;// 充值到账的钱
            if(pd.getInteger("status")==0){
              priceD = pd.getInteger("amount")+pd.getInteger("given_money");
                priceF = pd.getInteger("amount");
            }else{
                priceD=pd.getInteger("amount");
                priceF=pd.getInteger("amount")*pd.getInteger("discount");
            }*/
            // 生成订单号，唯一,例：201807021607465eaa88330
            String recharge_number = OrderNoUtil.getOrderNo();
            PageData pOne = new PageData();

            /**
             *  1.生成价格时，判断是否有优惠券
             *  2.判断优惠券类型（代金，打折）
             *  3.生成折扣后的金额
             */
//                PageData pCoupons = new PageData();
//                pCoupons.put("member_id", member_id);
//                pCoupons.put("status",3);
//                pCoupons = couponsService.listExchange(pCoupons).get(0);
//                System.out.println("================order_priceOld===" + order_price);
//                System.out.println("type_id="+pCoupons.getInteger("type_id"));
//                if (pCoupons.getInteger("type_id") == 1) {
//                    // 优惠券
//                    order_price= order_price - pCoupons.getDouble("discount_amount");
//                } else if (pCoupons.getInteger("type_id") == 2) {
//                    // 折扣券,不四舍五入
//                    order_price = order_price * pCoupons.getDouble("discount_amount");
//                }
//             exchangeService.updateExchange(pCoupons);
//
//            System.out.println("================order_priceNew===" + order_price);




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
            pRecharge.put("order_price", order_price);
            pRecharge.put("recharge_number", recharge_number);
            pRecharge.put("create_time", DateUtil.getTime());
            pRecharge.put("status", 0);
            // 插入充值记录表
         /*   int rowR = orderService.addRecharge(pRecharge);
            System.out.println("插入充值记录表row:" + rowR);*/
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
            prepayReqHandler.setParameter("total_fee", String.valueOf(order_price * 100).split("\\.")[0]); // 订单价格
            prepayReqHandler.setParameter("spbill_create_ip", "127.0.0.1"); // 获取客户端ip
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
