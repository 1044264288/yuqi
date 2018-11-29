package com.pyjh.clothing.controller;

import com.alibaba.fastjson.JSON;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.HomeBannerService;
import com.pyjh.clothing.util.CodeMessage;
import com.pyjh.clothing.util.Message;
import com.pyjh.clothing.util.QiniuCloudUtil;
import com.sun.imageio.plugins.common.ImageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.pyjh.clothing.util.CommonUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("banner")
@Api(value = "banner", description = "首页banner接口")
public class HomeBannerController extends CodeMessage {

    @Resource
    HomeBannerService homeBannerService;

    @ApiOperation("获取首页banner")
    @RequestMapping(value = "/getHomeBanner", method = RequestMethod.GET)
    public String getHomeBanner(Integer state) {
        PageData pageData=new PageData();
        pageData.put("state",state);
        List<PageData> data = homeBannerService.getHomeBanner(pageData);
        if (data != null) {
            String res = JSON.toJSONString(data);
            return Message.mesTrue(code_200, res, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("获取分类页banner")
    @RequestMapping(value = "/getTypeBanner", method = RequestMethod.GET)
    public String getTypeBanner(Integer state) {
        PageData pageData=new PageData();
        pageData.put("state",state);
        List<PageData> data = homeBannerService.getTypeBanner(pageData);
        if (data != null) {
            String res = JSON.toJSONString(data);
            return Message.mesTrue(code_200, res, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("新增banner")
    @RequestMapping(value = "/addBanner", method = RequestMethod.POST)
    public String addBanner(MultipartFile image, String targetUrl,String banner_video,
                            Integer ifVideo) throws Exception {
        String url = urlImg(image);
        int a = 0;
        if (homeBannerService.addHomeBanner(url, targetUrl, ifVideo)) {
            return Message.mesTrue(code_200, JSON.toJSONString(url), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }



    private String[] urlImg(MultipartFile[] image) throws Exception {
        QiniuCloudUtil qiniuCloudUtil = new QiniuCloudUtil();
        String[] data = null;
        if(image == null){
            return null;
        }
        for (int i = 0; i < image.length; i++) {
            byte[] bytes = image[i].getBytes();
            String imageName = "pyjh/son/" + UUID.randomUUID().toString();
            String url = qiniuCloudUtil.put64image(bytes, imageName);
            data[i] = url;
        }
        return data;
    }

    private String urlImg(MultipartFile image) throws Exception {
        QiniuCloudUtil qiniuCloudUtil = new QiniuCloudUtil();
        if(image == null){
            return null;
        }
        byte[] bytes = image.getBytes();
        String imageName = "pyjh/son/" + UUID.randomUUID().toString();
        String url = qiniuCloudUtil.put64image(bytes, imageName);
        return url;
    }

    @ApiOperation("新增分类banner")
    @RequestMapping(value = "/addTypeBanner", method = RequestMethod.POST)
    public String addTypeBanner(@RequestParam MultipartFile image, String targetUrl,
                                Integer ifVideo) throws Exception {
        String banner_url = urlImg(image);
        int a = 0;
        if (homeBannerService.addTypeBanner(banner_url, targetUrl, ifVideo)) {
            return Message.mesTrue(code_200, JSON.toJSONString(banner_url), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }
    @ApiOperation("修改首页banner")
    @RequestMapping(value = "/modifyBanner", method = RequestMethod.POST)
    public String modifyBanner(MultipartFile banner_url, String homeBannerId,
                               String targetUrl, String  state, String ifVideo) throws Exception {
        PageData pd = new PageData();
        Map<String, Object> data = new HashMap<String, Object>();
        pd.put("homeBannerId", homeBannerId);
        pd.put("banner_url", urlImg(banner_url));
        pd.put("state", state);
        pd.put("ifVideo", ifVideo);
        pd.put("targetUrl", targetUrl);
        if ( homeBannerService.editHomeBanner(pd) > 0) {
            data.put("banner_url", pd.get("banner_url"));
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("修改分类banner")
    @RequestMapping(value = "/modifyTypeBanner", method = RequestMethod.POST)
    public String modifyTypeBanner( MultipartFile banner_url, String homeBannerId,
                                   String targetUrl, String state, String ifVideo) throws Exception {

        PageData pd = new PageData();
        Map<String, Object> data = new HashMap<String, Object>();
        pd.put("homeBannerId", homeBannerId);
        pd.put("banner_url", urlImg(banner_url));
        pd.put("state", state);
        pd.put("ifVideo", ifVideo);
        pd.put("targetUrl", targetUrl);
        if ( homeBannerService.editTypeBanner(pd) > 0) {
            data.put("banner_url", pd.get("banner_url"));
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("根据ID查看homeBanner")
    @RequestMapping(value = "/findHomeID", method = { RequestMethod.POST,
            RequestMethod.GET })
    public Object findHomeID(Integer home_banner_id){
        Map<String, Object> data = new HashMap<String, Object>();
        PageData pd = new PageData();
        try {
            pd.put("home_banner_id", home_banner_id);

            PageData findId = homeBannerService.findforEntityHom(pd);
            data.put("findId", findId);
        } catch (Exception e) {
            return Message.mesFalse(code_501, message_501);
        }

        return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
    }



    @ApiOperation("根据ID查看typeBanner")
    @RequestMapping(value = "/findtypeID", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json;charset=UTF-8")
    public Object findtypeID(Integer home_banner_id) {
        Map<String, Object> data = new HashMap<String, Object>();

        PageData pd = new PageData();
        try {
            pd.put("home_banner_id", home_banner_id);
            PageData findId = homeBannerService.findforEntityType(pd);
            data.put("findId", findId);
        } catch (Exception e) {
            return Message.mesFalse(code_501, message_501);
        }

        return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
    }







}
