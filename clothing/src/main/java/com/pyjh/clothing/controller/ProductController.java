package com.pyjh.clothing.controller;

import com.alibaba.fastjson.JSON;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.ProductService;
import com.pyjh.clothing.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "product")
@Api(value = "product", description = "商品接口")
public class ProductController extends CodeMessage {

    @Resource
    ProductService productService;

    @ApiOperation("获取商品信息")
    @RequestMapping(value = "/getProduct", method = RequestMethod.GET)
    public String getProductList(Integer corporateId, Integer typeId, Integer franId,
                                 Integer productId, Integer recommend, Integer styleId,Integer status,String query_key) {
        PageData pageData = new PageData();
        pageData.put("corporateId", corporateId);
        pageData.put("typeId", typeId);
        pageData.put("franId", franId);
        pageData.put("productId", productId);
        pageData.put("recommend", recommend);
        pageData.put("styleId", styleId);
        pageData.put("status", status);
        pageData.put("query_key",query_key);
        Map<String, Object> resMap = productService.getProduct(pageData);
        if (resMap.get("data") == null) {
            return Message.mesFalse(code_501, resMap.get("res").toString());
        }
        return Message.mesTrue(code_200, JSON.toJSONString(resMap.get("data")), message_200);
    }

    @ApiOperation("根据商品id查询商品规格详情")
    @RequestMapping(value = "getProductIdSku", method = RequestMethod.GET)
    public String getProductIdSku(Integer product_id) {
        System.out.println(product_id);
        if (product_id == null) {
            return Message.mesFalse(code_400, message_400);
        }
        Map<String, Object> datamap = new HashMap<>();
        List<PageData> productSkus = productService.getProductSku(product_id);
        List<PageData> productSku = productService.getProductSku(null);
        PageData pageData = new PageData();
        PageData res = null;
        List<PageData> datas = new ArrayList<>();
        for (PageData skuval : productSku) {
            String key = skuval.get("type_name").toString();
            pageData.put("typeId", Integer.parseInt(skuval.get("type_id").toString()));
            List<PageData> val = productService.getProductSkuVal(pageData);
            for (int a = 0; a < val.size(); a++) {
                val.get(a).put("status", 0);
            }
            res = new PageData();
            res.put("key", key);
            res.put("val", val);
            datas.add(res);
        }
        if (datamap != null) {
            datamap.put("productSkus", productSkus);
            datamap.put("datas", datas);
            return Message.mesTrue(code_200, JSON.toJSONString(datamap), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("查看所有的规格值，value_id是查询的值的详情")
    @RequestMapping(value = "/forEntitySkuId", method = { RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object forEntitySkuId(@RequestParam(value = "value_id", required = false) Integer value_id,
                           @RequestParam(value = "query_key", required = false) String query_key) throws Exception {
        Map<String, Object> date = new HashMap<String, Object>();
        PageData pd = new PageData();
        pd.put("value_id", value_id);
        pd.put("query_key", query_key);
        date.put("findtypes", productService.forEntitySkuId(value_id,query_key));
        return Message.mesTrue(code_200, JSON.toJSONString(date), message_200);
    }

    @ApiOperation("修改商品规的格信息")
    @PostMapping("/updateProduct")
    public String updateProduct(String product_id, String sku_value_id) {
        if (product_id == null) {
            System.out.println(product_id);
            System.out.println(sku_value_id);
            return Message.mesFalse(code_400, message_400);
        }
        if (sku_value_id == null) {
            System.out.println(product_id);
            System.out.println(sku_value_id);
            return Message.mesFalse(code_400, message_400);
        }
        String[] aa = sku_value_id.split(",");
        if (aa == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData1 = new PageData();
        int a = 0;
        productService.deleteProductSku(product_id);
        for (int i = 0; i < aa.length; i++) {
            pageData1.put("sku_value_id", aa[i]);
            pageData1.put("product_id", product_id);
            pageData1.put("create_time", DateTime.GetNowDate());
            Integer updatesku = productService.insertProductSku(pageData1);
            if (updatesku > 0) {
                a++;
            }
        }
        if (a == aa.length) {
            String data = JSON.toJSONString(a);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("修改商品的基本信息")
    @PostMapping("/updateProducts")
    public String updateProducts(@RequestParam String pageData) {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        if (pageData1 == null) {
            return Message.mesFalse(code_400, message_400);
        }
        Integer i = productService.updateProduct(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查询商品基本信息")
    @GetMapping(value = "getProductId")
    public String getProductId(@RequestParam Integer product_id) {
        Map<String, Object> datamap = new HashMap<>();
        List<PageData> bannerData = productService.getProductBanner(product_id);
        List<PageData> detailData = productService.getProductDetail(product_id);
        List<PageData> productType = productService.getProductType();
        List<PageData> productStyle = productService.getProductStyle();
        PageData productid = productService.getProductId(product_id);
        datamap.put("bannerData", bannerData);
        datamap.put("detailData", detailData);
        datamap.put("productType", productType);
        datamap.put("productStyle", productStyle);
        datamap.put("productid", productid);
        if (datamap != null) {
            return Message.mesTrue(code_200, JSON.toJSONString(datamap), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查询新增商品规格参数")
    @GetMapping("getProductsum")
    public String getProductsum() {
        Map<String, Object> datamap = new HashMap<>();
        List<PageData> productSku = productService.getProductSkuType(null);
        PageData pageData = new PageData();
        PageData res = null;
        List<PageData> datas = new ArrayList<>();
        for (PageData skuval : productSku) {
            String key = skuval.get("type_name").toString();
            pageData.put("typeId", Integer.parseInt(skuval.get("type_id").toString()));
            List<PageData> val = productService.getProductSkuVal(pageData);
            for (int a = 0; a < val.size(); a++) {
                val.get(a).put("status", 0);
            }
            res = new PageData();
            res.put("key", key);
            res.put("val", val);
            datas.add(res);
        }
        datamap.put("datas", datas);
        if (datamap != null) {
            String data = JSON.toJSONString(datamap);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查询商品Type,Style")
    @GetMapping(value = "/getProductTypeStyle")
    public String getProductTypeStyle() {
        List<PageData> pageDataType = productService.getProductType();
        List<PageData> pageDataStyle = productService.getProductStyle();
        Map map = new HashMap();
        map.put("pageDataType", pageDataType);
        map.put("pageDataStyle", pageDataStyle);
        if (map != null) {
            String data = JSON.toJSONString(map);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("增加商品基本信息")
    @PostMapping("/insertProduct")
    public String insertProduct(@RequestParam String pageData, @RequestParam MultipartFile icon_url) throws Exception {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        if (pageData1 == null) {
            return Message.mesFalse(code_400, message_400);
        }
        pageData1.put("icon_url", DateTime.imgUrl(icon_url));
        pageData1.put("shelf_time", DateTime.GetNowDate());
        Integer i = productService.insertProduct(pageData1);
        if (i != null) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("新增商品规格")
    @PostMapping(value = "insertProductSku")
    public String insertProductSku(Integer product_id, String sku_value_id) {
        String[] aa = sku_value_id.split(",");
        if (aa == null) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData1 = new PageData();
        Integer a = 0;
        for (int i = 0; i < aa.length; i++) {
            pageData1.put("sku_value_id", aa[i]);
            pageData1.put("product_id", product_id);
            pageData1.put("create_time", DateTime.GetNowDate());
            if (productService.insertProductSku(pageData1) > 0) {
                a++;
            }
        }
        if (a == aa.length) {
            String data = JSON.toJSONString(a);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("新增banner图")
    @PostMapping("insertProduct_banner")
    public String insertProduct_banner(@RequestParam String pageData, @RequestParam MultipartFile[] banner_url) throws Exception {
        try {
            List<PageData> listpageData = JSON.parseArray(pageData, PageData.class);
            System.out.println(listpageData);
            if (listpageData == null) {
                return Message.mesFalse(code_400, message_400);
            }
            PageData pageData1 = new PageData();
            Integer a = 0;
            for (int i = 0; i < banner_url.length; i++) {
                String back = DateTime.imgUrl(banner_url[i]);
                pageData1.put("create_time", DateTime.GetNowDate());
                pageData1.put("banner_url", back);
                pageData1.put("if_video", listpageData.get(i).get("if_video"));
                pageData1.put("product_id", listpageData.get(i).get("product_id"));
                if (productService.insertProduct_banner(pageData1) > 0) {
                    a++;
                }
            }
            if (a == banner_url.length) {
                String data = JSON.toJSONString(a);
                return Message.mesTrue(code_200, data, message_200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("增加商品详情图片")
    @PostMapping("insertProduct_detail")
    public String insertProduct_detail(@RequestParam String pageData, @RequestParam MultipartFile[] picture_url) throws Exception {
       try {
           List<PageData> listpageData = JSON.parseArray(pageData, PageData.class);
           if (listpageData == null || picture_url == null) {
               return Message.mesFalse(code_400, message_400);
           }
           PageData pageData1 = new PageData();
           Integer a = 0;
           for (int i = 0; i < picture_url.length; i++) {
               pageData1.put("create_time", DateTime.GetNowDate());
               pageData1.put("picture_url", DateTime.imgUrl(picture_url[i]));
               pageData1.put("product_id", listpageData.get(i).get("product_id"));
               if (productService.insertProduct_detail(pageData1) > 0) {
                   a++;
               }
           }
           if (a == picture_url.length) {
               String data = JSON.toJSONString(a);
               return Message.mesTrue(code_200, data, message_200);
           }
       }catch(Exception e){
           e.printStackTrace();
       }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("删除banner图")
    @PostMapping(value = "delectProduct_banner")
    public String delectProduct_banner(@RequestParam Integer banner_id) {
        PageData pd = new PageData();
        pd.put("banner_id", banner_id);
        try {
            int row = 0;
            row=productService.delectProduct_banner(pd);
            if(row>0){
                String data = JSON.toJSONString(row);
                return Message.mesTrue(code_200,data,message_200);
            }else {
                return Message.mesFalse(code_401, message_401);
            }
        } catch (Exception e) {
            return Message.mesFalse(code_400, message_400);
        }

    }


    @ApiOperation("删除detail图")
    @PostMapping(value = "delectProduct_detail")
    public String delectProduct_detail(@RequestParam Integer detail_id) {
        PageData pd = new PageData();
        pd.put("detail_id", detail_id);
        try {
            int row = 0;
            row=productService.delectProduct_detail(pd);
            if(row>0){
                String data = JSON.toJSONString(row);
                return Message.mesTrue(code_200,data,message_200);
            }else {
                return Message.mesFalse(code_401, message_401);
            }
        } catch (Exception e) {
            return Message.mesFalse(code_400, message_400);
        }

    }

    @ApiOperation("删除规格值")
    @PostMapping("dateleProductValue")
    public Object dateleProductValue(@RequestParam  Integer value_id){
        PageData pd = new PageData();
        pd.put("value_id", value_id);
        try {
            int row = 0;
            row=productService.dateleProductValue(pd);
            if(row>0){
                return Message.mesTrues(code_200,message_200);
            }else {
                return Message.mesFalse(code_401, message_401);
            }
        } catch (Exception e) {
            return Message.mesFalse(code_400, message_400);
        }

    }
    @ApiOperation("修改商品状态")
    @PutMapping("/updateProductStatus")
    public String updateProductStatus(@RequestParam String pageData) {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        if (pageData1 == null) {
            return Message.mesFalse(code_400, message_400);
        }
        if (pageData1.get("status").equals(0)) {
            pageData1.put("down_time", DateTime.GetNowDate());
        } else {
            pageData1.put("shelf_time", DateTime.GetNowDate());
        }
        Integer i = productService.updateProductStatus(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("新增商品类型")
    @PostMapping("/insertProductType")
    public String insertProductType(@RequestParam String pageData) {
        System.out.println(pageData);
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        if (pageData1 == null) {
            return Message.mesFalse(code_400, message_400);
        }
        pageData1.put("create_time", DateTime.GetNowDate());
        Integer i = productService.insertProductType(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("新增商品风格")
    @PostMapping("/insertProductStyle")
    public String insertProductStyle(@RequestParam String pageData) {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        if (pageData1 == null) {
            return Message.mesFalse(code_400, message_400);
        }
        pageData1.put("create_time", DateTime.GetNowDate());
        Integer i = productService.insertProductStyle(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("修改商品类型")
    @PutMapping("/updateProductType")
    public String updateProductType(@RequestParam String pageData) {
        System.out.println(pageData);
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        if (pageData1 == null) {
            return Message.mesFalse(code_400, message_400);
        }
        pageData1.put("update_time", DateTime.GetNowDate());
        Integer i = productService.updateProductType(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("修改商品风格")
    @PutMapping(value = "/updateProductStyle")
    public String updateProductStyle(@RequestParam String pageData) {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        if (pageData1 == null) {
            return Message.mesFalse(code_400, message_400);
        }
        pageData1.put("update_time", DateTime.GetNowDate());
        System.out.println(JSON.toJSONString(pageData1));
        Integer i = productService.updateProductStyle(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查询商品分类")
    @PostMapping("getProductType")
    public String getProductType() {
        List<PageData> pageData = productService.getProductType();
        if (pageData != null) {
            String data = JSON.toJSONString(pageData);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }

    @ApiOperation("查询商品风格")
    @RequestMapping(value = "getProductStyle", method = RequestMethod.GET)
    public String getProductStyle() {
        List<PageData> pageData = productService.getProductStyle();
        if (pageData != null) {
            String data = JSON.toJSONString(pageData);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }





    @ApiOperation("查询商品规格根据商品的ID")
    @GetMapping("getProductSku")
    public String getProductSku(Integer productId) {
        List<PageData> productSku = productService.getProductSkuType(null);
        PageData pageData = new PageData();
        pageData.put("productId",productId);
        PageData res = null;
        List<PageData> data = new ArrayList<>();
        for (PageData skuval : productSku) {
            //String key = skuval.get("type_name").toString();
            pageData.put("typeId", Integer.parseInt(skuval.get("type_id").toString()));
            List<PageData> val = productService.getProductSkuVal(pageData);
            for (int a = 0; a < val.size(); a++) {
                //System.out.println("ssss");
                val.get(a).put("status", 0);
            }
            res = new PageData();
            skuval.put("val", val);
            res.put("key", skuval);
            //res.put("val", val);
            data.add(res);
        }
        if (pageData != null) {
            // String data = JSON.toJSONString(pageData);
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }
//////////////////////////////////商品规格类型////////////////
    @ApiOperation("查询所有商品的规格类型")
    @RequestMapping(value = "findskutypes", method = RequestMethod.GET)
    public String findskutypes(String  query_key,Integer type_id)throws  Exception {
        PageData pageData=new PageData();
        pageData.put("query_key",query_key);
        pageData.put("type_id",type_id);
        List<PageData> skuTypes = productService.findSkuType(pageData);
        if (skuTypes != null) {
            String data = JSON.toJSONString(skuTypes);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("查询首页banner图")
    @GetMapping("getHomeBanner")
    public String getHomeBanner() {
        List<PageData> pageDataList = productService.getHomeBanner();
        if (pageDataList != null) {
            String data = JSON.toJSONString(pageDataList);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }

    @ApiOperation("增加首页banner图")
    @PostMapping("insertHomeBanner")
    public String insertHomeBanner(@RequestParam String pageData) {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        pageData1.put("create_time", DateTime.GetNowDate());
        Integer i = productService.insertHomeBanner(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }

    @ApiOperation("修改首页banner图状态")
    @PutMapping("updateHomeBanner")
    public String updateHomeBanner(@RequestParam String pageData) {
        PageData pageData1 = JSON.parseObject(pageData, PageData.class);
        pageData1.put("update_time", DateTime.GetNowDate());
        Integer i = productService.updateHomeBanner(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }

    @ApiOperation("查询全部商品规格")
    @GetMapping("getProductSkuId")
    public String getProductSkuId() {
        List<PageData> pageDataList = productService.getProductSkuId();
        if (pageDataList != null) {
            String data = JSON.toJSONString(pageDataList);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }

    @ApiOperation("新增商品的规格Type")
    @PostMapping("insertProductSkuType")
    public String insertProductSkuType(@RequestParam String type_name,String state) {
        PageData pageData=new PageData();
        pageData.put("type_name",type_name);
        pageData.put("state",state);
        pageData.put("create_time", DateTime.GetNowDate());

        if (type_name == null) {
            return Message.mesFalse(code_400, message_400);
        }
        Integer i = productService.insertProductSkuType(pageData);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }

    /**
     * 图片上传
     * @param image
     * @return
     * @throws Exception
     */
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


    @ApiOperation("新增规格的Value")
    @PostMapping("insertProductSkuValue")
    public String insertProductSkuValue(Integer sku_type_id,String sku_name, String content,@RequestParam MultipartFile picture) throws Exception {
        PageData pageData =new PageData();
        pageData.put("sku_type_id",sku_type_id);
        pageData.put("sku_name",sku_name);
        pageData.put("content",content);
        pageData.put("picture",urlImg(picture));
        int a = 0;
        a=productService.insertProductSkuValue(pageData);
        if (a>0) {
            return Message.mesTrue(code_200, JSON.toJSONString(picture), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


       /*
        PageData pageData1 = JSON.parseObject(PageData, PageData.class);
        if (pageData1 != null) {
            return Message.mesFalse(code_400, message_400);
        }
        pageData1.put("create_time", DateTime.GetNowDate());
        pageData1.put("picture", DateTime.imgUrl(picture));
        Integer i = productService.insertProductSkuValue(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }*/

    @ApiOperation("修改商品规格Type")
    @PostMapping("updateProductSkuType")
    public String updateProductSkuType(String type_name,Integer type_id,String state) {
        if (type_id == null ) {
            return Message.mesFalse(code_400, message_400);
        }
        PageData pageData1 = new PageData();
        pageData1.put("type_name",type_name);
        pageData1.put("type_id",type_id);
        pageData1.put("state",state);
        pageData1.put("update_time", DateTime.GetNowDate());
        Integer i = productService.updateProductSkuType(pageData1);
        if (i > 0) {
            String data = JSON.toJSONString(i);
            return Message.mesTrue(code_200, data, message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }


    @ApiOperation("修改商品规格Value")
    @RequestMapping(value = "/updateProductSkuValue", method = RequestMethod.POST)
    public Object updateProductSkuValue(
            @RequestParam Integer value_id,Integer sku_type_id,String  sku_name,
            MultipartFile picture,Integer state,String content)throws  Exception {
        PageData pageData=new PageData();
        Map<String, Object> data = new HashMap<String, Object>();
        pageData.put("value_id",value_id);
        pageData.put("sku_type_id",sku_type_id);
        pageData.put("sku_name",sku_name);
        pageData.put("picture",ImageUtil.imgUrl(picture));
        pageData.put("state",state);
        pageData.put("content",content);
        if ( productService.updateProductSkuValue(pageData)> 0) {
            data.put("picture", pageData.get("picture"));
            return Message.mesTrue(code_200, JSON.toJSONString(data), message_200);
        }
        return Message.mesFalse(code_501, message_501);
    }


    @ApiOperation("根据商品的ID查看商品的规格，以及根据商品规格类型查询")
    @GetMapping("findProductValueId")
    public Object  findProductValueId(Integer product_id) throws  Exception {

        Map<String, Object> data = new HashMap<String, Object>();
        PageData pd = new PageData();
        List<PageData> types = productService.findSkuType(null);
        data.put("types", types);
        pd.put("product_id", product_id);
        List<Object> typelist = new ArrayList<>();

        for (int i = 0; i < types.size(); i++) {
            pd.put("type_id", types.get(i).getInteger("type_id"));
            typelist.add(productService.findProductValueId(pd));

        }

        data.put("typelist", typelist);
        if (typelist != null) {
            String date = JSON.toJSONString(typelist);
            return Message.mesTrue(code_200, date, message_200);
        }
        return Message.mesFalse(code_400, message_400);
    }


}
