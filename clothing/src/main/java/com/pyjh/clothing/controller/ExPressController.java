package com.pyjh.clothing.controller;
import  com.pyjh.clothing.util.CommonUtil;

import com.pyjh.clothing.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import  com.pyjh.clothing.entity.PageData;
import  com.pyjh.clothing.util.ResponseEnum;
import org.springframework.web.bind.annotation.*;
import  com.pyjh.clothing.util.KdniaoTrackQueryAPI;


import org.apache.ibatis.annotations.Param;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("express")
@Api(value = "express", description = "快递接口")
public class ExPressController {

    @Autowired
    private OrderService orderService;

    /**
     * 通过订单id，查询出物流信息
     * @param order_id
     * @return
     */

    @ApiOperation("查询快递根据订单ID")
    @RequestMapping(value = "/queryLogisticsInfo", method = { RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public Object queryLogisticsInfo(@RequestParam("order_id") Integer order_id) {

        if (CommonUtil.paramIsNull(order_id) ) {
            return ResponseEnum.ARGUMENT_IS_NULL.toString();
        }
        PageData orderTracesByJson = new PageData();
        PageData pOrder = new PageData();
        pOrder.put("order_id", order_id);
        try {
        pOrder= orderService.findExpress(pOrder).get(0);
            if (pOrder == null) {
                return ResponseEnum.SERVER_DATA_NOTEXIST.toString();
            }

            // 获得物流单号
            pOrder.put("express_number", pOrder.getString("express_number"));
            pOrder.put("express_code",pOrder.getString("express_code"));
            orderTracesByJson = KdniaoTrackQueryAPI.getOrderTracesByJson(pOrder.getString("express_code"), pOrder.getString("express_number"));
            orderTracesByJson.put("pOrder", pOrder);
        } catch (Exception e) {
            return ResponseEnum.SERVER_SQL_ERROR.toString();
        }
        return ResponseEnum.SUCCESS.appendObject(orderTracesByJson);
    }





}
