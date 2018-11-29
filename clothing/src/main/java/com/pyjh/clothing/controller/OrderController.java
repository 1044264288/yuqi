package com.pyjh.clothing.controller;

import com.alibaba.fastjson.JSON;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.CustomService;
import com.pyjh.clothing.service.OrderService;
import com.pyjh.clothing.util.CodeMessage;
import com.pyjh.clothing.util.DateTime;
import com.pyjh.clothing.util.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("order")
@Api(value = "order", description = "订单接口")
public class OrderController extends CodeMessage {

    @Resource
    OrderService orderService;

    @Resource
    CustomService customService;
   /* @Resource
    KdniaoSubscribeAPI kdniaoSubscribeAPI;*/


    @ApiOperation("查询全部订单")
    @RequestMapping(value = "getOrder", method = RequestMethod.GET)
    public String getOrder(Integer memberId, Integer franchiseeId, String orderCode, String status) {
        PageData pageData = new PageData();
        if (memberId != null) {
            pageData.put("memberId", memberId);
            pageData.put("franchiseeId", franchiseeId);
            pageData.put("orderCode", orderCode);
            pageData.put("status", status);
        }
        String res = JSON.toJSONString(orderService.getOrder(pageData));
        List<PageData> oder = orderService.getOrder(pageData);
        for (PageData pageData1 : oder) {
            pageData1.put("custom", customService.getCustomByMember1(pageData1.getInteger("member_id")));
            pageData1.put("sku", orderService.getOrderSku(pageData1.getInteger("order_id")));
        }
        if (res != null) {
            return Message.mesTrue(code_200, JSON.toJSONString(oder), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("修改订单")
    @PutMapping(value = "/updateOrder")
    public String updateOrderStatus(@RequestParam String pageData) {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        System.out.println(pageData1);
        pageData1.put("send_time", DateTime.GetNowDate());
        if (pageData1 != null) {
            String data = JSON.toJSONString(orderService.updateOrder(pageData1));
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }
    @ApiOperation("消息订单")
    @PostMapping(value = "/updateOrders")
    public String updateOrders(@RequestParam Integer order_id) {
      //  System.out.println(pageData1);
        PageData pageData=new PageData();
        pageData.put("order_id",order_id);
        if (order_id != null) {
            String data = JSON.toJSONString(orderService.updateOrders(pageData));
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }
    @ApiOperation("查询全部订单和快递单")
    @GetMapping(value = "/getOrderEx")
    public String getOrderEx() {
        List<PageData> pageDataList = orderService.getOrderEx();
        if (pageDataList != null) {
            String data = JSON.toJSONString(pageDataList);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查询全部快递名称")
    @GetMapping(value = "/getExpress")
    public String getExpress() {
        List<PageData> pageDataList = orderService.getExpress();
        if (pageDataList != null) {
            String data = JSON.toJSONString(pageDataList);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查询全部快递名称")
    @GetMapping(value = "/getOrdercode")
    public String getOrdercode(@RequestParam String order_code) {
        if (order_code != null) {
            String data = JSON.toJSONString(orderService.getOrdercode(order_code));
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("")
    @PostMapping(value = "/getOrders")
    public String getOrders(String pageData) {
        Map<String, String> maps = JSON.parseObject(pageData, Map.class);
        String url = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";
       /* String data = JSON.toJSONString(kdniaoSubscribeAPI.sendPost(url,maps));
        if(data != null){
            return Message.mesTrue(code_200, data, message_200);//TODO 快递信息
        }*/
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查看订单详情根据订单ID")
    @PostMapping(value = "/findDetailId")
    public String findDetailId(@RequestParam Integer order_id) {
        PageData pageData=new PageData();
        pageData.put("order_id",order_id);
        if (order_id != null) {
            String data = JSON.toJSONString(orderService.findDetailId(pageData));
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("点击发货，根据order_id，查看订单的收货信息")
    @PostMapping(value = "/findExpress")
    public String findExpress(@RequestParam Integer order_id) {
        PageData pageData=new PageData();
        pageData.put("order_id",order_id);
        if (order_id != null) {
            String data = JSON.toJSONString(orderService.findExpress(pageData));
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查看订单详情会员量体规格")
    @PostMapping(value = "/findDetailSku")
    public String findDetailSku(@RequestParam Integer order_detail_id) {
        PageData pageData=new PageData();
        PageData result = new PageData();
        pageData.put("order_detail_id",order_detail_id);
        if (order_detail_id != null) {
            List<PageData> skus=orderService.findDetailSkus(pageData);
            List<PageData> allCustom=orderService.findDetailCustomInfo(pageData);
            List<PageData> customInfos= new ArrayList<PageData>();
            String maxDate = null;
            for(int i=0;i<allCustom.size();i++){
                String temp = allCustom.get(i).get("create_time").toString();
                if(i==0) {
                    maxDate = temp;
                }
                if(temp.equals(maxDate))
                    customInfos.add(allCustom.get(i));
            }
            result.put("skus",skus);
            result.put("customInfos",customInfos);
            String data = JSON.toJSONString(result);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    /**
     * 通过订单号查询订单内容，（待付款的去付款）
     *
     * @param order_code
     * @return
     */
    @ApiOperation("根据订单号查询")
    @PostMapping(value = "/queryProductByOrderCode")
    public String queryProductByOrderCode(@RequestParam String order_code){
        PageData pOrderDetail = new PageData();
        pOrderDetail.put("order_code", order_code);
        if (pOrderDetail != null) {
            String data = JSON.toJSONString(orderService.queryProductByOrderCode(pOrderDetail));
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);

    }

    /**
    *根据状态查询
     * @param status
    * @return
    */
    @ApiOperation("根据状态查询")
    @PostMapping(value = "/queryOrderByStatus")
    public String queryOrderByStatus(@RequestParam Integer status){
        Map<String, Object> date = new LinkedHashMap<String, Object>();
        List<PageData> lObj = new ArrayList<>();
        PageData pOrder = new PageData();
        pOrder.put("status", status);
        if (pOrder != null) {
            String data = JSON.toJSONString(orderService.queryOrderByStatus(pOrder));
            return Message.mesTrue(code_200, data, message_200);
        }
        for (int i = 0; i < pOrder.size(); i++) {
            PageData pOrderDetail = new PageData();
            PageData pProduct = new PageData();
            pProduct.put("order_code", pOrder.get(i).toString());
            String order_code = pOrder.get(i).toString();
            System.out.println("=======code="+order_code);
            List<PageData> listProduct = orderService.queryProductByOrderCode(pProduct);
            System.out.println("=======listProduct="+listProduct.size());
            pOrderDetail.put("listProduct", listProduct);
            pOrderDetail.put("OrderCode", pOrder.get(i));
            lObj.add(pOrderDetail);
        }
        date.put("List", lObj);
        return Message.mesFalse(code_501, message_501);

    }

    /**
     * 通过手机号查询商品订单
     * @param mobile
     * @return
     */
    @ApiOperation("根据手机号查询")
    @PostMapping(value = "/queryProductBymobile")
    public String queryProductBymobile(@RequestParam String mobile){
        PageData pOrderDetail = new PageData();
        pOrderDetail.put("mobile", mobile);
        if (pOrderDetail != null) {
            String data = JSON.toJSONString(orderService.queryProductBymobile(pOrderDetail));
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);

    }



}
