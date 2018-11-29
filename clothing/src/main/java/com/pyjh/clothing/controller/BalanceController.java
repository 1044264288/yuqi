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
//                  佛祖保佑       永无BUG
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
import com.pyjh.clothing.service.BalanceService;
import com.pyjh.clothing.util.CodeMessage;
import com.pyjh.clothing.util.DateUtil;
import com.pyjh.clothing.util.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liu
 * @date 2018/8/28 16:34
 */
@RestController
@RequestMapping(value = "Balance")
@Api(value = "Balance", description = "设置付款金额")
public class BalanceController extends CodeMessage {

    @Resource
    BalanceService balanceService;

    @ApiOperation("查询充值金额，根据ID查看详情")
    @PostMapping("getBalance")
    public String getBalance(Integer id,Integer state) {
        PageData pd=new PageData();
        pd.put("id",id);
        pd.put("state",state);
        List<PageData> pageDataList = balanceService.getBalance(pd);
        if (pageDataList != null) {
            String data = JSON.toJSONString(pageDataList);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }

    @ApiOperation("增加充值金额")
    @PostMapping("insertBalance")
    public String insertBalance(@RequestParam String pageData) {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        if (pageData1 != null) {
            pageData1.put("create_time", DateUtil.getTime());
            Integer i = balanceService.insertBalance(pageData1);

            if (i > 0) {
                String data = JSON.toJSONString(i);
                return Message.mesTrue(code_200, data, message_200);
            }
            return Message.mesFalse(code_401, message_401);
        }
        return Message.mesFalse(code_400, message_400);
    }

    @ApiOperation("修改充值金额")
    @PostMapping("updateBalance")
    public String updateBalance(@RequestParam String pageData) {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        if (pageData1 != null) {
            Integer i = balanceService.updateBalance(pageData1);
            if (i > 0) {
                String data = JSON.toJSONString(i);
                return Message.mesTrue(code_200, data, message_200);
            }
            return Message.mesFalse(code_401, message_401);
        }
        return Message.mesFalse(code_400, message_400);
    }

    @ApiOperation("删除充值金额")
    @PostMapping("deleteBanlance")
    public Object deleteBanlance(@RequestParam  Integer id){
        PageData pd = new PageData();
        pd.put("id", id);
        try {
            int row = 0;
            row=balanceService.deleteBanlance(pd);
            if(row>0){
                return Message.mesTrues(code_200,message_200);
            }else {
                return Message.mesFalse(code_401, message_401);
            }
        } catch (Exception e) {
            return Message.mesFalse(code_400, message_400);
        }

    }
}
