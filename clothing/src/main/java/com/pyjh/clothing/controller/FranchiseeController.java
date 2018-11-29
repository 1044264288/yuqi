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
import com.pyjh.clothing.service.FranchiseeService;
import com.pyjh.clothing.service.UserService;
import com.pyjh.clothing.util.CodeMessage;
import com.pyjh.clothing.util.DateTime;
import com.pyjh.clothing.util.Message;
import com.pyjh.clothing.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liu
 * @date 2018/8/21 14:07
 */
@RestController
@RequestMapping(value = "franchisee")
@Api(value = "franchisee", description = "店铺接口")
public class FranchiseeController extends CodeMessage {

    @Resource
    FranchiseeService franchiseeService;
    @Resource
    UserService userService;


    @ApiOperation("增加店铺")
    @PostMapping("/insertFranchisee")
    public String insertFranchisee(@RequestParam String pagedata, @RequestParam Integer create_id, @RequestParam Integer role_id) {
        PageData pageDatas = JSON.parseObject(pagedata, PageData.class);
        if (pageDatas != null) {
            pageDatas.put("serial_number", RandomUtil.generateDigitalString(8));
            pageDatas.put("create_id", create_id);
            pageDatas.put("create_time", DateTime.GetNowDate());
            Integer i = franchiseeService.insertFranchisee(pageDatas);
            if (i > 0) {
                PageData pageData = franchiseeService.getFranchiseenumber(pageDatas.get("serial_number"));
                if (pageData != null) {
                    pageData.put("role_id", role_id);
                    pageData.put("create_user_id", create_id);
                    pageData.put("create_time", DateTime.GetNowDate());
                    Integer a = userService.insertUser(pageData);
                    if (a > 0) {
                        String data = JSON.toJSONString(a);
                        return Message.mesTrue(code_200, data, message_200);
                    }
                    return Message.mesFalse(code_400, message_400);
                }
                return Message.mesFalse(code_401, message_401);
            }
        }
        return Message.mesFalse(code_400, message_400);
    }

    @ApiOperation("首页信息展示")
    @RequestMapping(value = "/homelist", method = RequestMethod.GET)
    public String homelist() {

        List<PageData> homes = franchiseeService.findList();
        Map<String, Object> mapdata = new HashMap<>();
        if (homes!=null) {
            mapdata.put("homes", homes);
        } else {
                return Message.mesFalse(code_231, message_231);
        }

        return Message.mesTrue(code_200, JSON.toJSONString(mapdata), message_200);

    }







}
