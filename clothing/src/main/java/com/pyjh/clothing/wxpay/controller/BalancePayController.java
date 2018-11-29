package com.pyjh.clothing.wxpay.controller;

import com.pyjh.clothing.wxpay.util.MD5;
import com.pyjh.clothing.wxpay.util.MD5Util;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.*;
import com.pyjh.clothing.util.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.logging.Logger;

@Controller("WXbalance")
@RequestMapping("/wx/balance")
public class BalancePayController {

    @Resource
    MemberService memberService;

    @Resource
    ProductService productService;

    @Resource
    OrderService orderService;

    @Resource
    Product_order_detailService product_order_detailService;

    @Resource
    AddressService addressService;

    @Resource
    ShopCartService shopCartService;
    @Resource
    CouponsService couponsService;// 优惠券
    @Resource
    ExchangeService exchangeService;
    private static HttpSession session = null;// 忘记密码，修改密码


    /**
     * 余额支付，生成订单，订单详情，如果成功，
     * 1：返回用户（密码支付）
     */
    @RequestMapping(value = "/balancePay", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object BalancePay(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "member_id", required = false) Integer member_id,
                             @RequestParam(value = "address_id", required = false) Integer address_id,
                             @RequestParam(value = "payWay", required = false) String pay_way,
                             @RequestParam(value = "order_note", required = false) String order_note,
                             @RequestParam(value = "product_id", required = false) String product_id,
                             @RequestParam(value = "amount", required = false) String amount,
                             @RequestParam(value = "shopCartId", required = false) String shopCartId,
                             @RequestParam(value = "ec_id", required = false) Integer ec_id,
                             @RequestParam(value = "skuId", required = false) String skuId,
                             @RequestParam(value = "brede_context", required = false)String brede_context) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (CommonUtil.paramIsNull(member_id)
                || CommonUtil.paramIsNull(address_id)
                || CommonUtil.paramIsNull(pay_way)
                || CommonUtil.paramIsNull(order_note)
                || CommonUtil.paramIsNull(product_id)
                || CommonUtil.paramIsNull(amount)) {
            return ResponseEnum.ARGUMENT_IS_NULL.toString();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        // 用“，”分割
        String[] product_ids = product_id.split(",");
        String[] sku = skuId.split(",");
        String[] amounts = amount.split(",");
        String[] brede_contexts = brede_context.split(",");
        String out_trade_no = "";
        try {
            // 支付账户
            PageData pMember = new PageData();
            pMember.put("member_id", member_id);
            pMember = memberService.findForEntity1(pMember);
            String payMember = pMember.getString("mobile");
            // 订单
            PageData pOrder = new PageData();
            double order_price = 0;
            // 随机字符串，不长于32位。推荐随机数生成算法

            // 3.1封装数据
            double[] prices = new double[amounts.length];
            String[] names = new String[amounts.length];
            for (int i = 0; i < amounts.length; i++) {
                PageData pProduct = new PageData();
                pProduct.put("product_id", Integer.parseInt(product_ids[i]));
                PageData product = productService.findforEntity(pProduct);
                prices[i] = product.getDouble("product_price");
                names[i] = product.getString("product_name");
                // 单个产品总价，遍历之后就是所有总价
                order_price = order_price + product.getDouble("product_price") * Integer.parseInt(amounts[i]);
            }

            /*
             * 生成订单号，全数据库唯一,不唯一的时候，支付回调不了 例：201807021607465eaa88330
             */
            out_trade_no = OrderNoUtil.getOrderNo();
            PageData pOne = new PageData();

            while (true) {
                pOne.put("order_code", out_trade_no);
                PageData order = orderService.findfor(pOne);
                // 为空时跳出循环.getOrder(pOne);
                if (CommonUtil.paramIsNull(order)) {
                    break;
                }
                out_trade_no = OrderNoUtil.getOrderNo();
            }

            // 生成订单
            pOrder = new PageData();

            /**
             *  1.生成价格时，判断是否有优惠券
             *  2.判断优惠券类型（代金，打折）
             *  3.生成折扣后的金额
             */
            PageData pCoupons = new PageData();
            if (CommonUtil.isEmptyObject(ec_id)){
                pCoupons.put("member_id", member_id);
                pCoupons.put("ec_id", ec_id);
                pCoupons = couponsService.listExchange(pCoupons).get(0);
                System.out.println("================order_priceOld===" + order_price);
                System.out.println("type_id=" + pCoupons.getInteger("type_id"));
                if (pCoupons.getInteger("type_id") == 3) {
                    // 优惠券
                    order_price = order_price - pCoupons.getDouble("discount_amount");
                    pOrder.put("ec_id",ec_id);
                }
            }
            System.out.println("================order_priceNew===" + order_price);

            pOrder.put("address_id", address_id);

            pOrder.put("member_id", member_id);
            pOrder.put("pay_way", pay_way);
            pOrder.put("status", "1");
            if (order_price <= 0) {
                order_price = 0.01;
            }
            pOrder.put("order_price", order_price);
            pOrder.put("pay_account", payMember);
            pOrder.put("create_time", CommonUtil.getDateTimeString(new Date()));
            pOrder.put("order_code", out_trade_no);
            pOrder.put("order_note", order_note);
            orderService.addOrder(pOrder);
            // 订单详情
            for (int i = 0; i < amounts.length; i++) {
             //   String[] skuid = sku[i].split(",");
                PageData pDetail = new PageData();
                pDetail.put("order_code", out_trade_no);
                pDetail.put("product_id", Integer.parseInt(product_ids[i]));
                pDetail.put("product_name", names[i]);
                pDetail.put("price", prices[i]);
                pDetail.put("amount", Integer.parseInt(amounts[i]));
                pDetail.put("brede_context", brede_contexts[i]);

                int row = product_order_detailService.insertOrderDetail(pDetail);

                PageData pd = new PageData();
                if (row > 0) {
                    pd.put("orderId", pDetail.getInteger("detail_id"));
                    //  shopCartService.updateShopChartSku(pd);
                    if (shopCartId != null) {
                        shopCartService.updateShopChartSku(pd);
                    } else {
                        for (int j = 0; sku.length > j; j++) {
                            // 新增订单规格
                            pd.put("skuId", Integer.parseInt(sku[j]));
                            shopCartService.addShopOrderSku(pd);
                        }
                    }
                }

            }
            // 如果成功，返回订单号，密码是否存在，true：存在，false：不存在
            data.put("out_trade_no", out_trade_no);
            // 密码是否为空
            if (CommonUtil.paramIsNull(pMember.getString("pay_password"))) {
                data.put("pay_passwordIsExist", "false");
            } else {
                data.put("pay_passwordIsExist", "true");
            }
        } catch (Exception e) {
            return ResponseEnum.SERVER_SQL_ERROR.toString();
        }
        return ResponseEnum.SUCCESS.appendObject(data);
    }


    /**
     * 接收余额支付成功通知
     *
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/balanceNotify", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getnotify(HttpServletRequest request, HttpServletResponse response,
                            Integer member_id,
                            String out_trade_no,
                            String pay_password) {
        Map<String, Object> model = new HashMap<String, Object>();
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (CommonUtil.paramIsNull(out_trade_no)
                || CommonUtil.paramIsNull(member_id)
                || CommonUtil.paramIsNull(pay_password)) {
            model.put("msg", "参数为空");
            model.put("status", "402");
            return model;
        }
        // 4.1 修改当前订单状态为:付款成功-》代发货A
        PageData pOrder = new PageData();
        pOrder.put("order_code", out_trade_no);
        pOrder = orderService.findfor(pOrder);
        //如果订单状态已修改，说明已经回调了，防止多次回调
       if ("2".equals(pOrder.getInteger("status"))) {
            System.out.println("SSS回调接口已经被回调过了：" + pOrder.getInteger("status"));
            model.put("msg", "重复回调");
            model.put("msg", "904");
            return model;
        } else if ("1".equals(pOrder.getInteger("status"))) {
            System.out.println("SSS回调接口已经被回调过了：" + pOrder.getInteger("status"));
        }
        pOrder.put("status", "2"); // 支付完成,待发货
        int row = 0;
        try {
            //判断是否使用红包
            if (CommonUtil.isEmptyObject(pOrder.getInteger("ec_id"))) {
                PageData pCoupons = new PageData();
                pCoupons.put("status",3);
                pCoupons.put("ec_id",pOrder.getInteger("ec_id"));
                exchangeService.updateExchange(pCoupons);
            }
            //判断密码是否正确
            PageData pMember = new PageData();
            pMember.put("member_id", member_id);
            pMember.put("member_id", pOrder.getInteger("member_id"));
            pMember = memberService.findForEntity1(pMember);
            if (!pMember.getString("pay_password").equals(pay_password)) {
                model.put("msg", "密码错误");
                model.put("status", "303");
                return model;
            }
            // 更新交易表中状态
            row = orderService.updateOrder(pOrder);
            System.out.println("row:" + row);
            if (row == 1) {
                List<Integer> product_ids = new ArrayList<Integer>();
                String sex = "";
                if (CommonUtil.isEmptyObject(pMember.getInteger("sex")) && pMember.getInteger("sex") == 0) {
                    sex = "男";
                } else if (CommonUtil.isEmptyObject(pMember.getInteger("sex")) && pMember.getInteger("sex") == 1) {
                    sex = "女";
                }
                int address_id = pOrder.getInteger("address_id");
                String liuyan = pOrder.getString("order_note");
                String msg = "";
                List<PageData> proDetails = product_order_detailService.findforEntity(pOrder);
                for (PageData pDetails : proDetails) {
                    product_ids.add(pDetails.getInteger("product_id"));
                    PageData Oneproduct = new PageData();
                    Oneproduct.put("product_id", pDetails.getInteger("product_id"));
                    try {
                        Oneproduct = productService.findforEntity(Oneproduct);
                    } catch (Exception e) {
                        // logger.warn(e);
                    }
                    msg = msg + Oneproduct.getString("product_name") + "　　商品单价：" + Oneproduct.getDouble("product_price")
                            + "元　　购买数量：" + pDetails.getInteger("amount") + "\n";
                }
                    PageData pAddress = new PageData();
                    pAddress.put("address_id", address_id);
                    pAddress = addressService.findforEntity(pAddress);
                    EmailUtil.sendEmail("东方云定制客户下单通知",
                            "订单信息如下：\n用户姓名：" + pAddress.getString("consignee") + "\n手机号：" + pAddress.getString("phone")
                                    + "\n隶属公司：" + pAddress.getString("company") + "\n地址：" + pAddress.getString("area")
                                    + pAddress.getString("detail") + "\n购买商品信息如下：\n" + msg + "总价："
                                    + pOrder.getDouble("order_price") + "元\n送货方式：送货上门\n买家留言：" + liuyan + "\n下单时间："
                                    + pOrder.getString("create_time") + "\n请东方云订制管理员二十四小时内联系买家，进行备货！",
                            "156410689@qq.com");
                SmsClientSend.sendSms(pMember.getString("mobile"),
                        "【" + "东方云定制" + "】您本次操作的订单信息如下：" +
                                "\n" + msg + "订单总价:" + pOrder.getDouble("order_price") +
                                "元。" + "东方云定制会在三天内为您发货。\n【上海市服装研究所有限公司】");

                // 支付成功并且回掉后删除购物车中已购买的商品
                for (Integer product_id : product_ids) {
                    PageData pShopCart = new PageData();
                    pShopCart.put("product_id", product_id);
                    pShopCart.put("create_member_id", pOrder.getInteger("member_id"));
                    pShopCart = shopCartService.findforEntity(pShopCart);
                    if (CommonUtil.isEmptyObject(pShopCart)) {
                        shopCartService.delete(pShopCart);
                    }
                }
            } else {
                // 更新交易表中状态失败
                model.put("msg", "数据操作失败");
                model.put("status", "344");
                return model;
            }
        } catch (Exception e) {
            //logger.warn(e);
            model.put("msg", "数据库操作出现异常");
            model.put("status", "506");
            return model;
        }
        model.put("msg", "SUCCESS");
        model.put("status", "200");
        return model;
    }


    /**
     * 修改支付密码获取手机验证码
     *
     * @param mobile 手机账号 String
     * @return status 状态码 "200"发送成功 "701"短信发送失败
     */
    @RequestMapping(value = "/forgetPwd/code", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object registerCode(String mobile, HttpServletRequest request,
                               HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (CommonUtil.paramIsNull(mobile)) {
            return ResponseEnum.ARGUMENT_IS_NULL.toString();
        }
        session = request.getSession();
        PageData pd = new PageData();
        PageData pMerchants = new PageData();
        // 判断手机号是否存在
        pd.put("mobile", mobile);
        try {
            if (memberService.findForEntity2(pd) == null) {
                return ResponseEnum.ACCOUNTS_EXIST_NO.toString();
            }
        } catch (Exception e) {
            return ResponseEnum.DATA_OPERATION_FAILED.toString();
        }
        // 获取随机验证码
        String code = Tools.getRandomNum();
        String msg = "【" + "东方云定制" + "】您本次操作的验证码是" + code + "。如非本人操作，请忽略。\n（上海市服装研究所有限公司）";
        String resultString = SmsClientSend.sendSms(mobile, msg);
        int Rspcode = Tools.SmsClientSendCode(resultString);
        if (Rspcode == 0) {
            session.setAttribute("forgetPwdCode", code);
            return ResponseEnum.SUCCESS.toString();
        } else {
            return ResponseEnum.SERVER_SMS_SEND_FAIL.toString();
        }

    }

    /**
     * 支付忘记密码
     *
     * @param mobile       用户手机号
     * @param pay_password 新支付密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/forgetPwd", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object forgetPwd(String mobile,
                            String pay_password,
                            String forgetPwdCode,
                            HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (CommonUtil.paramIsNull(mobile)
                || CommonUtil.paramIsNull(pay_password)
                || CommonUtil.paramIsNull(forgetPwdCode)
                ) {
            return ResponseEnum.ARGUMENT_IS_NULL.toString();
        }
        // 判断账号是否存在
        PageData pd = new PageData();
        pd.put("mobile", mobile);
        try {
            // 账号不存在
            if (memberService.findForEntity2(pd) == null) {
                return ResponseEnum.ACCOUNTS_EXIST_NO.toString();
            }

            // 判断手机验证码是否正确
            String mobileCode = "";
            try {
                mobileCode = (String) session.getAttribute("forgetPwdCode");
                if (!forgetPwdCode.equals(mobileCode)) {
                    return ResponseEnum.CAPTCHA_ERROR.toString();
                }
            } catch (Exception e) {
                return ResponseEnum.SESSION_IS_NULL.toString();
            }

            // 修改数据
            pd.put("pay_password", pay_password);
            memberService.updateMemberpay_password(pd);
        } catch (Exception e) {
            return ResponseEnum.SERVER_SQL_ERROR.toString();
        }
        return ResponseEnum.SUCCESS.toString();
    }

}
