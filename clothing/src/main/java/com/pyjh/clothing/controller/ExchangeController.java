//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖保佑             永无BUG 
//          佛曰:  
//                  写字楼里写字间，写字间里程序员；  
//                  程序人员写程序，又拿程序换酒钱。  
//                  酒醒只在网上坐，酒醉还来网下眠；  
//                  酒醉酒醒日复日，网上网下年复年。  
//                  但愿老死电脑间，不愿鞠躬老板前；  
//                  奔驰宝马贵者趣，公交自行程序员。  
//                  别人笑我忒疯癫，我笑自己命太贱；  
//                  不见满街漂亮妹，哪个归得程序员？
package com.pyjh.clothing.controller;

import com.alibaba.fastjson.JSON;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.CouponsService;
import com.pyjh.clothing.service.ExchangeService;
import com.pyjh.clothing.service.MemberService;
import com.pyjh.clothing.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liu
 * @date 2018/8/19 16:36
 */
@RestController
@RequestMapping(value = "/Exchange")
@Api(value = "Exchange", description = "红包信息接口")
public class ExchangeController extends CodeMessage {

    @Resource
    ExchangeService exchangeService;
    @Resource
    MemberService memberService;
    @Resource
    CouponsService couponsService;
    @Resource
    DateTime dateTime;

    /**
     * 查询可以领取的优惠卷
     *
     * @param member_id
     * @param current_time
     * @return
     */
    @ApiOperation("查询可以领取的优惠卷")
    @GetMapping("/getExchange")
    public String getExchange(@RequestParam Integer member_id, @RequestParam String current_time) {
        PageData pageData = memberService.getMemberId(member_id);
        String coupons_name = "新用户";
        if (pageData.get("user_status").equals(1)) {
            List<PageData> pageDataList = couponsService.getCoupons(current_time);
            List<PageData> pageDataList1 = couponsService.getCouponsName(coupons_name);
            Map map = new HashMap();
            map.put("pageDataList", pageDataList);
            map.put("pageDataList1", pageDataList1);
            String data = JSON.toJSONString(map);
            if (data != null) {
                return Message.mesTrue(code_200, data, message_200);
            }
            return Message.mesFalse(code_400, message_400);
        } else {
            List<PageData> pageDataList = couponsService.getCoupons(current_time);
            String data = JSON.toJSONString(pageDataList);
            if (data != null) {
                return Message.mesTrue(code_200, data, message_200);
            }
            return Message.mesFalse(code_401, message_401);
        }
    }

    /**
     * 领取新用户优惠卷
     *
     * @param coupons_id
     * @return
     */
    @ApiOperation("给新用户增加优惠卷")
    @PostMapping("/insertExchange")
    public String insertExchange(@RequestParam String coupons_id, @RequestParam Integer member_id) {
        String[] ss = coupons_id.split(",");
        PageData json = new PageData();
        Integer a = 0;
        for (int i = 0; i < 1; i++) {
            json.put("co_id", ss[i]);
            json.put("ec_code", RandomUtil.generateDigitalString(11));
            json.put("member_id", member_id);
            json.put("create_time", dateTime.GetNowDate());
            if (exchangeService.insertExchange(json) > 0) {
                a++;
            }
        }
        if (a == ss.length) {
            String data = JSON.toJSONString(a);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_401, message_401);
    }

    /**
     * 根据member_id查出自己的优惠卷
     *
     * @param member_id
     * @return
     */
    @ApiOperation("查询自己的优惠卷")
    @GetMapping("/getExchangeId")
    public String getExchangeId(@RequestParam Integer member_id) {
        List<PageData> pageDataList = exchangeService.getExchange(member_id);
        if (pageDataList != null) {
            String data = JSON.toJSONString(pageDataList);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    /**
     * 查询新用户优惠券 ,根据member_id 查看是否领取
     *
     * @param member_id
     * @return
     */
    @ApiOperation("查询新用户优惠券 ,根据ctp_id 查看详情，member_id 查看领取详情查看是否领取")
    @GetMapping("/newCoupons")
    public String newCoupons( Integer ctp_id ,Integer member_id) throws Exception{

        PageData pageData=new PageData();
        pageData.put("ctp_id",ctp_id);
        pageData.put("member_id",member_id);
        List<PageData> pageDataList = couponsService.newCoupons(pageData);
        if (pageDataList != null) {
            String data = JSON.toJSONString(pageDataList);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("用户领取优惠券，添加优惠券-")
    @PostMapping("/updateMember")
    public Object updateMember(String ecids,Integer member_id, HttpServletRequest request) {
        PageData pageData = new PageData();
   //     pageData.put("ec_ids", ec_ids);
        pageData.put("member_id", member_id);
        pageData.put("update_time", DateUtil.getTime());
        pageData.put("status",1);
        System.out.println("str=="+ecids);
        String[] ec_ids = ecids.split(",");
        System.out.println("str[]=="+ Arrays.toString(ec_ids));

        if (ec_ids != null) {
            int rows = 0;
            for (int i = 0; i <2; i++) {
                pageData.put("ec_id", Integer.parseInt(ec_ids[i]));
                rows += couponsService.updateMember(pageData);
            }
            if (rows == ec_ids.length) {
                String data = JSON.toJSONString(rows);
                return Message.mesTrue(code_200, data, message_200);
            }

        }
        return Message.mesFalse(code_501, message_501);

    }

}
