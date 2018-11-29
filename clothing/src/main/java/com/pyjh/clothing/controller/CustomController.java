package com.pyjh.clothing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.CustomService;
import com.pyjh.clothing.util.*;
import com.pyjh.clothing.wxpay.util.MD5;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("custom")
@Api(value = "custom", description = "量体接口")
public class CustomController extends CodeMessage {

    @Resource
    CustomService customService;

    @ApiOperation("获取用户量体信息")
    @RequestMapping(value = "/getMemberCustom", method = RequestMethod.GET)
    public String getMemberCustom(Integer memberId, Integer state) {
        if (memberId == null || state == null) {
            return Message.mesFalse(code_400, message_400);
        }
        Map<String, Object> mapdata = new HashMap<>();
        List<PageData> typedata = customService.getCustomTypeByMember(memberId);
        PageData custom = customService.getCustomByMember(memberId, state);
        if (typedata != null) {
            mapdata.put("type", typedata);
        }
        if (custom != null) {
            mapdata.put("custom", custom);
        } else {
            return Message.mesFalse(code_231, message_231);
        }
        return Message.mesTrue(code_200, JSON.toJSONString(mapdata), message_200);
    }


    @ApiOperation("获取用户最新得出量体信息")
    @RequestMapping(value = "/newCustom", method = RequestMethod.GET)
    public String newCustom(Integer memberId, Integer state) {
        if (memberId == null || memberId == 0) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = new PageData();
        pageData.put("memberId", memberId);
        if(state!=null){
            pageData.put("state", state);
        }
        List<PageData> custom = customService.newCustomAll(pageData);
        ArrayList list = new ArrayList();
        if (custom.size()==0){
            return Message.mesFalse(code_504,message_504);
        } else {
            for(int i = 0;i<custom.size();i++){
                Map<String, Object> mapdata = new HashMap<>();
                mapdata.put("cus", custom.get(i));
                pageData.put("id", custom.get(i).getInteger("id"));
                List<PageData> typedata = customService.newCustom(pageData);
                    mapdata.put("type", typedata);
                list.add(mapdata);
            }
            }
        return Message.mesTrue(code_200, JSON.toJSONString(list), message_200);

    }


    @ApiOperation("录入用户量体信息")
    @RequestMapping(value = "/addMemberCustom", method = RequestMethod.POST)
    public String addMemberShopCart(@RequestParam(value = "memberId") Integer memberId,
                                    @RequestParam(value = "height") Double height,
                                    @RequestParam(value = "name") String name,
                                    @RequestParam(value = "weight") Double weight,
                                    @RequestParam(value = "s_neck_girth", required = false) Double s_neck_girth,
                                    @RequestParam(value = "state") Integer state,
                                    @RequestParam(value = "chest", required = false) Double chest,
                                    @RequestParam(value = "waistCircumference", required = false) Double waistCircumference,
                                    @RequestParam(value = "hipCircumference", required = false) Double hipCircumference,
                                    @RequestParam(value = "kneeCircumference", required = false) Double kneeCircumference,
                                    @RequestParam(value = "armLength", required = false) Double armLength,
                                    @RequestParam(value = "longLegs", required = false) Double longLegs,
                                    @RequestParam(value = "memCustom", required = false) String memCustom,
                                    @RequestParam(value = "breadth", required = false) Double breadth) {
        if (memberId == null || memCustom == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = new PageData();
        pageData.put("memberId", memberId);
        pageData.put("height", height);
        pageData.put("name", name);
        pageData.put("weight", weight);
        pageData.put("s_neck_girth", s_neck_girth);
        pageData.put("state", state);
        pageData.put("chest", chest);
        pageData.put("waistCircumference", waistCircumference);
        pageData.put("hipCircumference", hipCircumference);
        pageData.put("kneeCircumference", kneeCircumference);
        pageData.put("armLength", armLength);
        pageData.put("longLegs", longLegs);
        pageData.put("memCustom", memCustom);
        pageData.put("breadth", breadth);
        Integer res = customService.addCustomByMember(pageData);

        // List skulist = (List) pageData.get("sku");
        //   String skuStr = (String) pageData.get("sku");
        String[] mem_Customs = memCustom.split(",");

        System.out.println("str[]==" + Arrays.toString(mem_Customs));
        if (res > 0) {
            if (mem_Customs != null) {
                Integer id = customService.findMaxId();
                int rows = 0;
                for (int i = 0; i < mem_Customs.length; i++) {
                    pageData.put("value_id", Integer.parseInt(mem_Customs[i]));
                    pageData.put("custom_id", id);
                    rows += customService.addCustomValue(pageData);
                }
                if (rows == mem_Customs.length) {
                    String data = JSON.toJSONString(rows);
                    return Message.mesTrue(code_200, data, message_200);
                }
            }
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("后台用户量体信息")
    @RequestMapping(value = "/addCustoms", method = { RequestMethod.POST,
            RequestMethod.GET })
    public String addCustoms(@RequestParam(value = "memberId", required = false) Integer memberId,
                            @RequestParam(value = "height", required = false) Double height,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "weight", required = false) Double weight,
                            @RequestParam(value = "breadth", required = false) Double breadth,
                            @RequestParam(value = "state", required = false) Integer state,
                            @RequestParam(value = "chest", required = false) Double chest,
                            @RequestParam(value = "waistCircumference", required = false) Double waistCircumference,
                            @RequestParam(value = "hipCircumference", required = false) Double hipCircumference,
                            @RequestParam(value = "kneeCircumference", required = false) Double kneeCircumference,
                            @RequestParam(value = "armLength", required = false) Double armLength,
                            @RequestParam(value = "longLegs", required = false) Double longLegs,
                            @RequestParam(value = "s_arm_right", required = false) Double s_arm_right,
                            @RequestParam(value = "s_cross", required = false) Double s_cross,
                            @RequestParam(value = "s_distance", required = false) Double s_distance,
                            @RequestParam(value = "s_max_balley", required = false) Double s_max_balley,
                            @RequestParam(value = "s_neck_girth", required = false) Double s_neck_girth,
                            @RequestParam(value = "s_neck_height", required = false) Double s_neck_height,
                            @RequestParam(value = "s_waist_height", required = false) Double s_waist_height,
                            @RequestParam(value = "s_waist_buttock", required = false) Double s_waist_buttock,
                            @RequestParam(value = "s_pricture", required = false) MultipartFile s_picture,
                            @RequestParam(value = "s_phone", required = false) String s_phone,
                            @RequestParam(value = "only_code", required = false) String only_code,
                            @RequestParam(value = "s_cross_height", required = false) Double s_cross_height,
                            @RequestParam(value = "s_buttock_height", required = false) Double s_buttock_height,
                            @RequestParam(value = "machine_number", required = false) String machine_number)throws  Exception{
        PageData pageData = new PageData();
        pageData.put("memberId", memberId);
        pageData.put("height", height);
        pageData.put("state", state);
        pageData.put("name", name);
        pageData.put("weight", weight);
        pageData.put("breadth", breadth);
        pageData.put("chest", chest);
        pageData.put("waistCircumference", waistCircumference);
        pageData.put("hipCircumference", hipCircumference);
        pageData.put("kneeCircumference", kneeCircumference);
        pageData.put("armLength", armLength);
        pageData.put("longLegs", longLegs);
        pageData.put("s_arm_right", s_arm_right);
        pageData.put("s_cross", s_cross);
        pageData.put("s_distance", s_distance);
        pageData.put("s_max_balley", s_max_balley);
        pageData.put("s_neck_girth", s_neck_girth);
        pageData.put("s_neck_height", s_neck_height);
        pageData.put("s_waist_height", s_waist_height);
        pageData.put("s_waist_buttock", s_waist_buttock);
        pageData.put("s_picture", DateTime.imgUrl(s_picture));
        pageData.put("s_phone", s_phone);
        pageData.put("s_cross_height", s_cross_height);
        pageData.put("s_phone", s_phone);
        pageData.put("s_buttock_height", s_buttock_height);
        pageData.put("machine_number", machine_number
        );
        Integer res = customService.addCustomByMember(pageData);
        if (res > 0) {
            PageData pageData1=new PageData();
            pageData1.put("machine_number",pageData.getString("machine_number"));
            PageData pageData2=customService.firstcustom(pageData1);
            //  System.out.println(pageData2+"jjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
            String data= JSON.toJSONString(pageData2.getInteger("id"));
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);

        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("机器量体信息录入")
    @RequestMapping(value = "/addCustom", method = { RequestMethod.POST,
            RequestMethod.GET })
    public String addCustom(String custom,MultipartFile s_picture)throws Exception {
        if (custom == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = JSONObject.parseObject(custom, PageData.class);
        pageData.put("s_picture",DateTime.imgUrl(s_picture));
        System.out.println("ghjghjghjghg"+JSON.toJSONString(pageData));
        Integer res = customService.addCustomByMember(pageData);
        if (res > 0) {
             PageData pageData1=new PageData();
             pageData1.put("machine_number",pageData.getString("machine_number"));
             PageData pageData2=customService.firstcustom(pageData1);
          //  System.out.println(pageData2+"jjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
            String data= JSON.toJSONString(pageData2.getInteger("id"));
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("后台用户量体信息修改")
    @RequestMapping(value = "/editCustom", method = RequestMethod.POST)
    public String editCustom(
            @RequestParam(value = "id")Integer id,
                            @RequestParam(value = "height",required = false) Double height,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "weight",required = false) Double weight,
                            @RequestParam(value = "breadth", required = false) Double breadth,
                            @RequestParam(value = "state",required = false) Integer state,
                            @RequestParam(value = "chest", required = false) Double chest,
                            @RequestParam(value = "waistCircumference", required = false) Double waistCircumference,
                            @RequestParam(value = "hipCircumference", required = false) Double hipCircumference,
                            @RequestParam(value = "kneeCircumference", required = false) Double kneeCircumference,
                            @RequestParam(value = "armLength", required = false) Double armLength,
                            @RequestParam(value = "longLegs", required = false) Double longLegs,
                            @RequestParam(value = "s_arm_right", required = false) Double s_arm_right,
                            @RequestParam(value = "s_cross", required = false) Double s_cross,
                            @RequestParam(value = "s_distance", required = false) Double s_distance,
                            @RequestParam(value = "s_max_balley", required = false) Double s_max_balley,
                            @RequestParam(value = "s_neck_girth", required = false) Double s_neck_girth,
                            @RequestParam(value = "s_neck_height", required = false) Double s_neck_height,
                            @RequestParam(value = "s_waist_height", required = false) Double s_waist_height,
                            @RequestParam(value = "s_waist_buttock", required = false) Double s_waist_buttock) {

        PageData pageData = new PageData();
        pageData.put("id",id);
        pageData.put("height", height);
        pageData.put("state", state);
        pageData.put("name", name);
        pageData.put("weight", weight);
        pageData.put("breadth", breadth);
        pageData.put("chest", chest);
        pageData.put("waistCircumference", waistCircumference);
        pageData.put("hipCircumference", hipCircumference);
        pageData.put("kneeCircumference", kneeCircumference);
        pageData.put("armLength", armLength);
        pageData.put("longLegs", longLegs);
        pageData.put("s_arm_right", s_arm_right);
        pageData.put("s_cross", s_cross);
        pageData.put("s_distance", s_distance);
        pageData.put("s_max_balley", s_max_balley);
        pageData.put("s_neck_girth", s_neck_girth);
        pageData.put("s_neck_height", s_neck_height);
        pageData.put("s_waist_height", s_waist_height);
        pageData.put("s_waist_buttock", s_waist_buttock);
        Integer res = customService.editCustom(pageData);
        if (res > 0) {

            String data = JSON.toJSONString(res);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("根据量体ID的查看详情后台使用")
    @RequestMapping(value = "findSelfCoutom", method = RequestMethod.GET)
    public String findSelfCoutom(Integer id) throws Exception {
        PageData pageData = new PageData();
        pageData.put("id", id);
        Map<String, Object> data = new HashMap<String, Object>();
        PageData custom = customService.findforEntity(pageData);
        data.put("custom", custom);
        if (pageData != null) {
            // String data = JSON.toJSONString(pageData);
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }


    @ApiOperation("修改用户量体信息")
    @RequestMapping(value = "/modifyMemberCustom", method = RequestMethod.POST)
    public String modifyMemberCustom(String memCustom) {
        if (memCustom == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData = JSONObject.parseObject(memCustom, PageData.class);
        if (customService.editCustomByMember(pageData)) {
            return Message.mesTrue(code_200, "\"true\"", message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("根据性别查看类型，以及获取类型的值")
    @RequestMapping(value = "getCoutomTypes", method = RequestMethod.GET)
    public String getProductIdSku(Integer sex) throws Exception {

        /*if (sex == null) {
            return Message.mesFalse(code_400, message_400);
        }*/
        PageData pageData = new PageData();
        pageData.put("sex", sex);
        List<PageData> types = customService.findforTypes(pageData);


        PageData res = null;
        List<PageData> datas = new ArrayList<>();
        for (PageData skuval : types) {
            //  String key = skuval.get("type_name").toString();
            pageData.put("type_id", Integer.parseInt(skuval.get("type_id").toString()));
            List<PageData> val = customService.findforValues(pageData);
            for (int a = 0; a < val.size(); a++) {
                val.get(a).put("status", 0);
            }
            res = new PageData();
            res.put("key", skuval);
            res.put("val", val);
            datas.add(res);
        }
        if (pageData != null) {
            // String data = JSON.toJSONString(pageData);
            return Message.mesTrue(code_200, JSON.toJSONString(datas), message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }


    @ApiOperation("查看所有类型，根据type_id查看详情，模糊查询")
    @RequestMapping(value = "fintypes", method = RequestMethod.GET)
    public String fintypes(Integer type_id, String query_key) throws Exception {
        PageData pageData = new PageData();
        pageData.put("type_id", type_id);
        pageData.put("query_key", query_key);
        Map<String, Object> data = new HashMap<String, Object>();
        List<PageData> searchs = customService.findforTypes(pageData);
        data.put("types", searchs);
        if (pageData != null) {
            // String data = JSON.toJSONString(pageData);
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }


    @ApiOperation("模糊查询量体值，根据ID查看")
    @RequestMapping(value = "findforTypeValues", method = RequestMethod.GET)
    public String findforTypeValues(Integer id, String query_key) throws Exception {
        PageData pageData = new PageData();
        pageData.put("id", id);
        pageData.put("query_key", query_key);
        Map<String, Object> data = new HashMap<String, Object>();
        List<PageData> searchs = customService.findforTypeValues(pageData);
        data.put("valus", searchs);
        if (pageData != null) {
            // String data = JSON.toJSONString(pageData);
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }

    @ApiOperation("添加到 custom——type里面")
    @RequestMapping(value = "/addCustomtype", method = RequestMethod.POST)
    public String addCustomtype(String type_name, Integer sex
    ) throws Exception {
        PageData pageData = new PageData();
        pageData.put("type_name", type_name);
        pageData.put("sex", sex);
        int a = 0;
        a = customService.addCustomtype(pageData);
        if (a > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(a), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    private String urlImg(MultipartFile image) throws Exception {
        QiniuCloudUtil qiniuCloudUtil = new QiniuCloudUtil();
        if (image == null) {
            return null;
        }
        byte[] bytes = image.getBytes();
        String imageName = "pyjh/son/" + UUID.randomUUID().toString();
        String url = qiniuCloudUtil.put64image(bytes, imageName);
        return url;
    }

    @ApiOperation("添加到 custom——type_value里面")
    @RequestMapping(value = "/addCustomtypevalue", method = RequestMethod.POST)
    public String addCustomtypevalue(MultipartFile icon_url, String value, String content,
                                     Integer type_id) throws Exception {
        PageData pageData = new PageData();
        pageData.put("type_id", type_id);
        pageData.put("content", content);
        pageData.put("icon_url", urlImg(icon_url));
        pageData.put("value", value);
        int a = 0;
        a = customService.addCustomtypevalue(pageData);
        if (a > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(icon_url), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("修改量体类型")
    @RequestMapping(value = "/editCustomtype", method = RequestMethod.POST)
    public String editCustomtype(String type_name, Integer sex, Integer type_id, Integer state) throws Exception {
        PageData pageData = new PageData();
        if (type_id == null) {
            return Message.mesFalse(code_400, message_400);
        }
        pageData.put("type_name", type_name);
        pageData.put("sex", sex);
        pageData.put("type_id", type_id);
        pageData.put("state", state);
        int a = 0;
        a = customService.editCustomtype(pageData);
        if (a > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(a), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("修改量体类型值")
    @RequestMapping(value = "/editCustomtypeValue", method = RequestMethod.POST)
    public String editCustomtypeValue(MultipartFile icon_url, String value, String content, Integer state,
                                      Integer type_id, Integer id) throws Exception {
        PageData pageData = new PageData();
        pageData.put("type_id", type_id);
        pageData.put("content", content);
        pageData.put("icon_url", urlImg(icon_url));
        pageData.put("value", value);
        pageData.put("state", state);
        pageData.put("id", id);
        int a = 0;
        a = customService.editCustomtypeValue(pageData);
        if (a > 0) {
            return Message.mesTrue(code_200, JSON.toJSONString(icon_url), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("修改用户量体信息")
    @RequestMapping(value = "/updateCustomByMember", method = RequestMethod.POST)
    public String updateCustomByMember(@RequestParam(value = "memberId") Integer memberId,
                                       @RequestParam(value = "height") Double height,
                                    @RequestParam(value = "name") String name,
                                    @RequestParam(value = "weight") Double weight,
                                    @RequestParam(value = "breadth", required = false) Double breadth,
                                    @RequestParam(value = "state") Integer state,
                                    @RequestParam(value = "chest", required = false) Double chest,
                                   @RequestParam(value = "waistCircumference", required = false) Double waistCircumference,
                                    @RequestParam(value = "hipCircumference", required = false) Double hipCircumference,
                                    @RequestParam(value = "kneeCircumference", required = false) Double kneeCircumference,
                                    @RequestParam(value = "armLength", required = false) Double armLength,
                                    @RequestParam(value = "longLegs", required = false) Double longLegs
                                   //@RequestParam(value = "memCustom", required = false) String memCustom
                                       ) {
        PageData pageData3 = new PageData();
        pageData3.put("memberId", memberId);
        pageData3.put("height", height);
        pageData3.put("name", name);
        pageData3.put("weight", weight);
        pageData3.put("breadth", breadth);
        pageData3.put("state", state);
        pageData3.put("chest", chest);
        pageData3.put("waistCircumference", waistCircumference);
        pageData3.put("hipCircumference", hipCircumference);
        pageData3.put("kneeCircumference", kneeCircumference);
        pageData3.put("armLength", armLength);
        pageData3.put("longLegs", longLegs);
        //pageData3.put("memCustom", memCustom);
       Integer res = customService.updateCustomByMember(pageData3);
        if (res > 0) {
            String date1=JSON.toJSONString(res);
            return Message.mesTrue(code_200, date1, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("量体信息删除")
    @RequestMapping(value = "/deleteCustomById",method = RequestMethod.POST)
    public String deleteCustomById(@RequestParam(value = "id",required = false) Integer id){
        if(id == null || id == 0){
            return Message.mesFalse(code_401,message_401);
        }
        Integer res = customService.deleteCustomById(id);
        if(res>0){
            String s = JSON.toJSONString(res);
            return Message.mesTrue(code_200,s,message_200);
        }
    return Message.mesFalse(code_501,message_501);
    }

    @ApiOperation("量体信息设为默认")
    @RequestMapping(value = "/updateCustomIsDefatul")
    public String updateCustomIsDefatul(Integer id){
        if(id == null){
            return Message.mesFalse(code_401,message_401);
        }
        Integer res = customService.updateCustomIsDefatul(id);
        if(res>0){
            String s = JSON.toJSONString(res);
            return Message.mesTrue(code_200,s,message_200);
        }
        return Message.mesFalse(code_501,message_501);
    }

}
