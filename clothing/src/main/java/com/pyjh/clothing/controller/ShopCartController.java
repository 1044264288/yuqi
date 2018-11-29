package com.pyjh.clothing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.ShopCartService;
import com.pyjh.clothing.util.CodeMessage;
import com.pyjh.clothing.util.DateUtil;
import com.pyjh.clothing.util.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("shopCart")
@Api(value = "shopCart", description = "购物车接口")
public class ShopCartController extends CodeMessage {

    @Resource
    ShopCartService shopCartService;

    @ApiOperation("获取用户购物车")
    @RequestMapping(value = "/getMemberShopCart", method = RequestMethod.GET)
    public String getMemberShopCart(Integer memberId) {
        if (memberId == null) {
            return Message.mesFalse(code_400, message_400);
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        List<PageData> shopCartList = shopCartService.getMemberShopCart(memberId);
        for (PageData pageData : shopCartList) {
            pageData.put("sku", shopCartService.getCartSku(pageData.getInteger("shop_cart_id")));
        }
        if (shopCartList != null) {
            return Message.mesTrue(code_200, JSON.toJSONString(shopCartList), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("获取用户购物车")
    @RequestMapping(value = "/getShopCartSku", method = RequestMethod.GET)
    public String getShopCartSku(Integer cartId) {
        if (cartId == null) {
            return Message.mesFalse(code_400, message_400);
        }
        List<PageData> shopCartList = shopCartService.getCartSku(cartId);
        System.out.println(shopCartList+"FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        if (shopCartList != null) {
            return Message.mesTrue(code_200, JSON.toJSONString(shopCartList), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("加入购物车")
    @RequestMapping(value = "/addMemberShopCart", method = RequestMethod.POST)
    public String addMemberShopCart(String shopCart,Integer memberId,Integer productId,Integer productAmount) {

        if (memberId == null || productId == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = new PageData();
        pageData.put("memberId", memberId);
        pageData.put("productId", productId);
        pageData.put("productAmount", productAmount);
        pageData.put("create_time", DateUtil.getTime());
        Integer res = shopCartService.addMemberShopCart(pageData);

        // List skulist = (List) pageData.get("sku");
        //   String skuStr = (String) pageData.get("sku");
        String[] skulist = shopCart.split(",");

        System.out.println("str[]==" + Arrays.toString(skulist));
        if (res > 0) {
            if (skulist != null) {
                int rows = 0;
                for (int i = 0; i < skulist.length; i++) {
                    pageData.put("skuId", Integer.parseInt(skulist[i]));
                    pageData.put("cartId", shopCartService.getMaxShopCart());
                    rows += shopCartService.addShopOrderSku(pageData);
                }
                if (rows == skulist.length) {
                    String data = JSON.toJSONString(rows);
                    return Message.mesTrue(code_200, data, message_200);
                }
            }

        }
        return Message.mesFalse(code_501, message_501);
    }



    @ApiOperation("修改购物车")
    @RequestMapping(value = "/modifyMemberShopCart", method = RequestMethod.POST)
    public String modifyMemberShopCart(String shopCart) {
        if (shopCart == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = JSONObject.parseObject(shopCart, PageData.class);
        Integer res = shopCartService.editMemberShopCart(pageData);
        if (res > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(res), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("删除购物车信息")
    @RequestMapping(value = "/deleteMemberShopCart",method = {RequestMethod.POST,RequestMethod.GET})
    public String deleteMemberShopCart(Integer shopCartId) {
        if (shopCartId == null) {
            return Message.mesFalse(code_400, message_400);
        }
        Integer res = shopCartService.deleteMemChopCart(shopCartId);
        if (res > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(res), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }
}
