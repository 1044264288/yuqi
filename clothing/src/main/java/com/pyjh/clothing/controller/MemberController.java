package com.pyjh.clothing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.CouponsService;
import com.pyjh.clothing.service.MemberService;
import com.pyjh.clothing.service.impl.CustomServiceImpl;
import com.pyjh.clothing.util.CodeMessage;
import com.pyjh.clothing.util.Message;
import com.pyjh.clothing.util.SmsClientSend;
import com.pyjh.clothing.util.Tools;
import com.pyjh.clothing.wxpay.util.ConstantUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import com.pyjh.clothing.util.CommonUtil;
@RestController
@RequestMapping("member")
@Api(value = "member", description = "用户接口")
public class MemberController extends CodeMessage {
    @Resource
    private CustomServiceImpl csi;// 量体信息
    @Resource
    MemberService memberService;
    @Resource
    CouponsService couponsService;
    HttpSession session;

    @ApiOperation("用户登陆")
    @RequestMapping(value = "/memberLogin", method = RequestMethod.POST)
    public String memberLogin(String mobile, String password,String unionid)throws  Exception {
        if (mobile == null || password == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData res = memberService.memberLogin(mobile, password);
        // 删除支付密码
        res.remove("password");
        List<PageData> plist=new ArrayList<>();
        List<PageData> listCoupon=new ArrayList<>();

        if (res != null) {
            Integer status = Integer.parseInt(res.get("status").toString());
            PageData pd = new PageData();
            pd.put("member_id",res.getInteger("member_id"));
            plist=couponsService.newCoupons(pd);
            if (plist.size()!=0){
                // return  Message.mesFalse(code503,message_503);
                res.put("type",true);
            }else{
                //return  Message.mesFalse(code_502,message_502);
                res.put("type","false");
                pd.clear();
                listCoupon =couponsService.newCoupons(pd);
                if(listCoupon != null){
                    res.put("listCoupon",listCoupon);
                }
            }
            if (status == 0) {
                return Message.mesFalse(code_208, message_208);
            }
            PageData pageData=new PageData();
            if(unionid!=null){
                pageData.put("member_id",res.getInteger("member_id"));
                pageData.put("unionid",unionid);
                // 给量体信息添加上用户的唯一标识符unionid
                int row = csi.updateUnionid(pageData);
            }
            return Message.mesTrue(code_200, JSON.toJSONString(res), message_200);
        }
        return Message.mesFalse(code_203, message_203);
    }

    /**
     * 注册获取手机验证码
     *
     * @param mobile 手机账号 String
     * @return status 状态码 "200"发送成功 "210"发送失败
     */
    @ApiOperation("注册验证码")
    @RequestMapping(value = "/register/code", method = RequestMethod.POST)
    public Object registerCode(String mobile, HttpServletRequest request) {
        if (Tools.isEmpty(mobile)) {
            return Message.mesFalse(code_400, message_400);
        }
        Map<String, Object> data = new HashMap<>();
        session = request.getSession();
        // 获取随机验证码
        String code = Tools.getRandomNum();
        String msg = "【东方云订制】您本次操作的验证码是" + code + "。如非本人操作，请忽略。\n（上海服装研究所有限公司）";
        String resultString = SmsClientSend.sendSms(mobile, msg);
        int Rspcode = Tools.SmsClientSendCode(resultString);
        if (Rspcode == 0) {
            session.setAttribute("registercode", code);
            data.put("registercode", code);
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
        }
        return Message.mesFalse(code_201, message_201);
    }

    @ApiOperation("用户注册")
    @RequestMapping(value = "/memberRegister", method = RequestMethod.POST)
    public String userRegister(String mobile, String password, String code) {
        if (Tools.isEmpty(mobile) || Tools.isEmpty(password) || Tools.isEmpty(code)) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = new PageData();
        Integer memberId = null;
        pageData.put("mobile", mobile);
        pageData.put("password", password);
        if (!memberService.findForEntity(mobile)) {
            return Message.mesFalse(code_202, message_202);
        }
        String registercode = session.getAttribute("registercode").toString();
        if (registercode.equals(code)) {
            memberService.addMember(pageData);
            memberId  =pageData.getInteger("member_id");
        }
        if (memberId != null) {
            return Message.mesTrue(code_200, JSON.toJSONString(memberId), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("微信用户登陆")
    @RequestMapping(value = "/wxmember", method = RequestMethod.POST)
    public String wxmember(String mobile,String unionid) throws  Exception{
        if (Tools.isEmpty(mobile) ) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = new PageData();
        int member_id = 0;
        pageData.put("mobile", mobile);
        PageData pMember = new PageData();
        pMember.put("mobile",mobile);
        pMember = memberService.findEntity(pMember);
        // true:账号不存在，添加一个
        if (pMember == null) {
            memberService.addMember(pageData);
            // 获取添加的用户id
            member_id = pageData.getInteger("member_id");
        }else{
            // 账号存在
            member_id=pMember.getInteger("member_id");
        }
        if(unionid!=null){
            // 更新用户表和量体信息表的关联
            PageData pCustom = new PageData();
            pCustom.put("member_id",member_id);
            pCustom.put("unionid",unionid);
            csi.updateUnionid(pCustom);
        }
        if (member_id != 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(member_id), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }




    @ApiOperation("密码修改")
    @RequestMapping(value = "/modifyPassWord", method = RequestMethod.POST)
    public String modifyPassWord(String mobile, String password, Integer memberId, String code) {
        if (Tools.isEmpty(mobile) || Tools.isEmpty(password) || Tools.isEmpty(code) || memberId == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = new PageData();
        pageData.put("mobile", mobile);
        pageData.put("password", password);
        pageData.put("memberId", memberId);
        if (!memberService.findForEntity(mobile)) {
            System.out.println(memberService.findForEntity(mobile));
            String registercode = session.getAttribute("registercode").toString();
            Integer res = null;
            System.out.println(registercode);
            if (registercode.equals(code)) {
                res = memberService.editMember(pageData);
            }
            if (res != null) {
                return Message.mesTrue(code_200, JSON.toJSONString(res), message_200);
            }
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("用户信息修改")
    @RequestMapping(value = "/modifyMember", method = RequestMethod.POST)
    public String modifyMember(String member) {
        if (Tools.isEmpty(member)) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = JSONObject.parseObject(member, PageData.class);
        int res = memberService.editMember(pageData);
        if (res > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(res), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @RequestMapping(value = "/getOpenId", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public JSONObject getOpenId(String code) {
        String wxCode = code;
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session"; // 请求地址
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", ConstantUtil.APP_ID); // 开发者设置中的appId
        requestUrlParam.put("secret", ConstantUtil.APP_SECRET); // 开发者设置中的appSecret
        requestUrlParam.put("js_code", wxCode); // 小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code"); // 默认参数

        /**
         * 发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
         */
        JSONObject jsonObject = JSON.parseObject(sendPost(requestUrl, requestUrlParam));
        return jsonObject;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     *            请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    @ApiOperation("查询全部用户信息")
    @GetMapping("getmember")
    public Object getmember(String query_key) {
        PageData pageData=new PageData();
        pageData.put("query_key",query_key);
        List<PageData> pageDataList = memberService.getmember(query_key);
        if (pageDataList != null) {
            String data = JSON.toJSONString(pageDataList);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("根据用户ID查询用户信息")
    @RequestMapping(value = "/FindMemberId", method = { RequestMethod.POST}, produces = "application/json;charset=UTF-8")

    public Object FindMemberId(Integer member_id) throws Exception {
        PageData pd = new PageData();
        pd.put("member_id", member_id);
        List<PageData> meList = memberService.FindMemberId(pd);
        List<PageData> result= new ArrayList<PageData>();
        String maxDate = null;
        for(int i=0;i<meList.size();i++){
            String temp = meList.get(i).get("c_create_time").toString();
            if(i==0) {
                maxDate = temp;
            }
            if(temp.equals(maxDate))
                result.add(meList.get(i));
        }
        if (result != null) {
            return Message.mesTrue(code_200, JSON.toJSONString(result), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("后台用户状态修改0禁用，1正常")
    @RequestMapping(value = "/updateMeberID", method = RequestMethod.POST)
    public Object updateMeberID(Integer member_id,Integer status ) {

        if (CommonUtil.paramIsNull(member_id)||CommonUtil.paramIsNull(status)) {
            return Message.mesFalse(code_400, message_400);
        }

        PageData pageData=new PageData();
        pageData.put("member_id",member_id);
        pageData.put("status",status);
        int res = memberService.updateMeberID(pageData);
        if (res > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(res), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }
    @ApiOperation("查询余额")
    @GetMapping("getbalance")
    public Object getbalance(Integer member_id) {
        PageData pageData = new PageData();
        pageData.put("member_id", member_id);
        PageData pageDataList = memberService.getbalance(member_id);
        if (pageDataList != null) {
            String data = JSON.toJSONString(pageDataList);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

}



