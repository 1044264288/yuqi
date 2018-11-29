/**
 * _______________ ______________ ______________ _____________ ____________ ____________ ___________ __________ ________ _______ _______ ______ _____ ____ ____ ___ ___ ___ ___ ____ _____ ______ ________
 */
package com.pyjh.clothing.wxpay.controller;

import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.*;
import com.pyjh.clothing.util.CommonUtil;
import com.pyjh.clothing.util.EmailUtil;
import com.pyjh.clothing.util.SmsClientSend;
import com.pyjh.clothing.wxpay.util.XMLUtil;
import org.jdom2.JDOMException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("notfiy")
public class NotifyController {

    @Resource
    AddressService addressService;
    @Resource
    CouponsService couponsService;// 优惠券
    @Resource
    ExchangeService exchangeService;
    @Resource
    OrderService orderService;

    @Resource
    MemberService memberService;

    @Resource
    ProductService productService;

    @Resource
    ShopCartService shopCartService;

    @Resource
    RechargeService rechargeService;//充值记录



    /**
     * 9.2 接收微信支付成功通知
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws IOException
     */
    @RequestMapping(value = "api/weixin/notify")
    public void getnotify(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // EmailUtil.sendEmail("测试pay回掉方法是否调用1111 ：", date,
        // "1311717885@qq.com");
        String line = "NULL";
        System.out.println("微信pay回调:" + date);
        // 1.创建输入输出流
        PrintWriter writer = response.getWriter();
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        line = "len:" + len + "";
        outSteam.close();
        inStream.close();
        // 2.将结果转换
        String result = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> map = null;
        try {
            // 3.解析微信通知返回的信息
            map = XMLUtil.doXMLParse(result);
            line = "map---";
        } catch (JDOMException e) {
            line = "mapJDOMException";
            e.printStackTrace();
        }
        // 4.若支付成功，则告知微信服务器收到通知
        /**
         * 返回信息 return_msg 否 String(128) 例如：签名失败 返回信息，如非空，为错误原因 签名失败 参数格式校验错误
         */
        System.out.println(map.get("return_msg"));
        if (map.get("return_code").equals("SUCCESS")) {
            line = "return_code:SUCCESS";
            if (map.get("result_code").equals("SUCCESS")) {
                line = "result_code:SUCCESS";
                // 4.1 修改当前订单状态为:》代发货A
                PageData pOrder = new PageData();
                pOrder.put("order_code", map.get("out_trade_no"));
                pOrder = orderService.getOrder(pOrder).get(0);
                // 获取地址id
                int address_id = pOrder.getInteger("address_id");
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
                    // 更新交易表中状态
                    row = orderService.updateOrder(pOrder);
                    System.out.println("row:" + row);
                    //如果是充值，更改充值的状态:待支付-》支付成功
                    if (address_id == -2) {
                        PageData pRecharge = new PageData();
                        pRecharge.put("recharge_number", map.get("out_trade_no"));
                        pRecharge.put("status", 1);
                         rechargeService.update(pRecharge);
                    }
                } catch (Exception e2) {
                    line = "363:" + e2;
                    // logger.warn(e2);
                }
                if (row == 1) {
                    line = " line:363,row == 1";
                    int member_id = pOrder.getInteger("member_id");
                    List<Integer> product_ids = new ArrayList<Integer>();
                    PageData pMember = memberService.getMemberId(member_id);
                    String sex = "";
                    if (CommonUtil.isEmptyObject(pMember.getInteger("sex")) && pMember.getInteger("sex") == 0) {
                        sex = "男";
                    } else if (CommonUtil.isEmptyObject(pMember.getInteger("sex")) && pMember.getInteger("sex") == 1) {
                        sex = "女";
                    }
                    if (CommonUtil.isEmptyObject(address_id) && address_id >= 0) {
                        // 正常下单购买
                        String liuyan = pOrder.getString("order_note");
                        String msg = "";
                        try {
                            line = " line:389,";
                            //List<PageData> proDetails = product_order_detailService.findforList(pOrder);TODO 查询订单
                           /* for (PageData pDetails : proDetails) {
                                product_ids.add(pDetails.getInteger("product_id"));
                                PageData Oneproduct = new PageData();
                                Oneproduct.put("product_id", pDetails.getInteger("product_id"));
                                try {
                                    Oneproduct = productService.getProduct(pDetails);TODO 查询商品
                                } catch (Exception e) {
                                    line = " line:399,";
                                    //logger.warn(e);
                                }
                                msg = msg + Oneproduct.getString("product_name") + "　　商品单价："
                                        + Oneproduct.getDouble("product_price") + "元　　购买数量："
                                        + pDetails.getInteger("amount") + "\n";
                            }*/
                        } catch (Exception e1) {
                            //logger.warn(e1);
                        }

                            // 送货上门，用户信息从地址表查询
                            PageData pAddress = new PageData();
                            pAddress.put("address_id", address_id);
                            try {
                                //  pAddress = addressService.getMemberAddress(address_id); TODO 根据id查询地址
                            } catch (Exception e) {
                                // logger.warn(e);
                            }
                            EmailUtil.sendEmail("东方云订制客户下单通知",
                                    "订单信息如下：\n用户姓名：" + pAddress.getString("consignee") + "\n手机号："
                                            + pAddress.getString("phone") + "\n隶属公司：" + pAddress.getString("company")
                                            + "\n地址：" + pAddress.getString("area") + pAddress.getString("detail")
                                            + "\n购买商品信息如下：\n" + msg + "总价：" + pOrder.getDouble("order_price")
                                            + "元\n送货方式：送货上门\n买家留言：" + liuyan + "\n下单时间："
                                            + pOrder.getString("create_time")
                                            + "\n请东方云订制管理员二十四小时内联系买家，进行备货！",
                                    "156410689@qq.com");

                        SmsClientSend.sendSms(pMember.getString("mobile"),
                                "【东方云订制】您本次操作的订单信息如下：\n" + msg + "订单总价:"
                                        + pOrder.getDouble("order_price") + "元。东方云定制会在三天内为您发货。\n【上海市服装研究所有限公司】");
                    } else if (CommonUtil.isEmptyObject(address_id) && address_id == -2) {
                        // 我要充值,recharge_number
                        pMember.put("balance", pMember.getDouble("balance") + pOrder.getDouble("order_price"));
                        try {
                            //  memberService.update(pMember); TODO
                        } catch (Exception e) {
                            //logger.warn(e);
                        }
                        PageData pMember2 = new PageData();
                        pMember2.put("member_id", member_id);
                        try {
                            pMember2 = memberService.getMemberId(member_id);
                        } catch (Exception e) {
                            //logger.warn(e);
                        }
                        // 账户邮箱

                        if(CommonUtil.isEmptyObject(pMember2.getString("mailbox"))){
                            EmailUtil.sendEmail(/*pOrder.getString("merchants_name")+*/"充值到账通知",
                                    "您本次成功充值"+pOrder.getDouble("order_price")+"元到个人账号余额！您当前的可用余额为:"+
                                            pMember.getDouble("balance")+
                                            "元。成功为"+pMember.getString("company_name"),pMember2.getString("mailbox"));
                        }

                        SmsClientSend.sendSms(pMember.getString("mobile"),
                                "您本次成功通过" + pOrder.getString("pay_way") + "充值" + pOrder.getDouble("order_price")
                                        + "元到个人账户余额！您当前的可用余额为" + pMember2.getDouble("balance") + "元。成功为"
                                        + pMember.getString("company_name") + "赚取" + pOrder.getDouble("order_price")
                                        + "积分。\n【彭友聚汇提供技术支持】");
                        EmailUtil.sendEmail("充值到账通知",
                                "账户" + pMember.getString("mobile") + "成功通过" + pOrder.getString("pay_way") + "充值"
                                        + pOrder.getDouble("order_price") + "元到账户余额！",
                                "");
                    }
                    PageData pCompany = new PageData();
                    pCompany.put("company_name", pMember.getString("company_name"));
                    try {
                        // pCompany = companyService.findforEntity(pCompany);TODO
                    } catch (Exception e) {
                        //logger.warn(e);
                    }
                    try {

                        // 支付成功并且回掉后删除购物车中已购买的商品
                        for (Integer product_id : product_ids) {
                            PageData pShopCart = new PageData();
                            pShopCart.put("member_id", member_id);
                            pShopCart.put("product_id", product_id);
                            pShopCart.put("create_member_id", pOrder.getInteger("member_id"));
                            pShopCart = shopCartService.findforEntity(pShopCart);
                            if (CommonUtil.isEmptyObject(pShopCart)) {
                                shopCartService.delete(pShopCart);
                            }
                        }
                    } catch (Exception e) {
                        // logger.warn(e);
                    }
                    String notifyStr = XMLUtil.setXML("SUCCESS", "");
                    writer.write(notifyStr);
                    writer.flush();
                } else {
                    line = "微信支付回调失败了";
                    System.out.println("微信支付回调失败了");
                    String notifyStr = XMLUtil.setXML("ERROR", "");
                    writer.write(notifyStr);
                    writer.flush();
                }
            }
        }
       // EmailUtil.sendEmail("测试pay回掉方法是否调用LINE：", line, "1311717885@qq.com");
    }
}
