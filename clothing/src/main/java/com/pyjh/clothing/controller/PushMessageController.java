package com.pyjh.clothing.controller;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.pyjh.clothing.util.SignUtil;

@RestController
@RequestMapping(value = "/push")
@Api(value = "push", description = "Token令牌")
public class PushMessageController {


    @ApiOperation("Token令牌")
    @RequestMapping(value = "/Tokensession", method = { RequestMethod.POST,
            RequestMethod.GET })
    public void Tokensession(HttpServletRequest request, HttpServletResponse response){
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        PrintWriter print; if (isGet) {
            //下面从请求中获取校验需要的参数
            // 微信加密签名
           /// HttpServletRequest request=ServletActionContext.getRequest();


            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");

            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && SignUtil.checkSignature(signature, timestamp, nonce)) {
                try {
                    print = response.getWriter();
                    print.write(echostr);
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
