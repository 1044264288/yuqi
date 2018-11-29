package com.pyjh.clothing.controller;

import com.alibaba.fastjson.JSON;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.RechargeService;
import com.pyjh.clothing.util.CodeMessage;
import com.pyjh.clothing.util.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王豆豆 on 2018/9/10.
 */
@RestController
@RequestMapping("recharge")
@Api(value = "recharge",description = "充值记录")
public class RechargeController extends CodeMessage {
    @Resource
    RechargeService rechargeService;

    @ApiOperation("查询充值记录")
    @GetMapping(value = "/getByID")
    public String getByID(Integer recharge_id) {
        PageData pageData = new PageData();
        pageData.put("recharge_id", recharge_id);
        if (recharge_id != null) {
            String data = JSON.toJSONString(rechargeService.getByID(recharge_id));
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查询充值记录和消费记录")
    @RequestMapping(value = "/findForEntity", method = {RequestMethod.POST,RequestMethod.GET})
    public Object findForEntity(Integer member_id) {
        Map<String, Object> date = new HashMap<String, Object>();
        PageData pd = new PageData();
        pd.put("member_id", member_id);
        List<PageData> meList = rechargeService.findForEntity(pd);
        date.put("meList", meList);
        if (meList != null) {
            return Message.mesTrue(code_200, JSON.toJSONString(meList), message_200);
        }
        return Message.mesFalse(code_501, message_501);

    }

}