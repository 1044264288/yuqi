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
import com.pyjh.clothing.service.UserService;
import com.pyjh.clothing.util.CodeMessage;
import com.pyjh.clothing.util.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author liu
 * @date 2018/8/21 10:03
 */
@RestController
@RequestMapping(value = "/user")
@Api(value = "user", description = "后台用户接口")
public class UserController extends CodeMessage {

    @Resource
    UserService userService;

    /**
     * @param accountname 账号
     * @param password    密码
     * @return
     */
    @ApiOperation("后台用户登陆")
    @PostMapping(value = "/getUser")
    public String getUser(String accountname, String password) {
        if (accountname == null && password == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = userService.getUser(accountname, password);
        if (pageData != null) {
            String data = JSON.toJSONString(pageData);
            return Message.mesTrue(code_200, data, code_200);
        }
        return Message.mesFalse(code_203, message_203);
    }

    /**
     * @param platform_user_id 用户id
     * @param password         修改前的密码
     * @param password1        要修改的密码
     * @return
     */
    @ApiOperation("后台用户修改密码")
    @PostMapping(value = "/updateUser")
    public String updateUser(@RequestParam Integer platform_user_id, @RequestParam String password, @RequestParam String password1) {
        PageData pageData = userService.getUserId(platform_user_id);
        System.out.println(JSON.toJSONString(pageData));
        if (pageData != null) {
            if (password1.equals(pageData.get("password"))) {
                Integer i = userService.updateUser(platform_user_id, password);
                String data = JSON.toJSONString(i);
                return Message.mesTrue(code_200, data, code_200);
            }
            return Message.mesFalse(code_206, message_206);
        }
        return Message.mesFalse(code_400, message_400);
    }
}
