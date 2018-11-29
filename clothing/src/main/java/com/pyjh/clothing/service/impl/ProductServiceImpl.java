package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.ProductDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Resource
    ProductDao productDao;


    @Override
    public List<PageData> forEntitySkuId(Integer value_id, String query_key) throws  Exception{
        PageData pdData=new PageData();
        //关键字
        if(query_key==null||"".equals(query_key)){
            query_key="";
        }
        pdData.put("query_key", query_key);
        if(value_id==null||"".equals(value_id)){
            value_id=null;
        }
        pdData.put("value_id", value_id);
        return productDao.forEntitySkuId(pdData);

    }

    @Override
    public Map<String, Object> getProduct(PageData pageData) {
        Map<String, Object> res = new HashMap<>();
        List<PageData> data = productDao.getProduct(pageData);
        if (pageData.get("typeId") != null && pageData.get("franId") != null) {
            res.put("res", "不能同时获取两种分类商品");
            return res;
        }
        if (pageData.get("corporateId") != null && pageData.get("typeId") != null) {
            res.put("res", "不能同时获取两种分类商品");
            return res;
        }
        res.put("data", data);
        return res;
    }

    @Override
    public List<PageData> getProductBanner(Integer productId) {
        return productDao.getProductBanner(productId);
    }

    @Override
    public List<PageData> getProductDetail(Integer productId) {
        return productDao.getProductDetail(productId);
    }

    @Override
    public Integer insertProduct_banner(PageData pageData) {
        return productDao.insertProduct_banner(pageData);
    }

    @Override
    public Integer insertProduct_detail(PageData pageData) {
        return productDao.insertProduct_detail(pageData);
    }

    @Override
    public Integer selectProduct() {
        return productDao.selectProduct();
    }


    @Override
    public Integer insertProduct(PageData pageData) {
        return productDao.insertProduct(pageData);
    }

    @Override
    public Integer updateProduct(PageData pageData) {
        return productDao.updateProduct(pageData);
    }

    @Override
    public Integer updateProductStatus(PageData pageData) {
        return productDao.updateProductStatus(pageData);
    }

    @Override
    public Integer insertProductType(PageData pageData) {
        return productDao.insertProductType(pageData);
    }

    @Override
    public Integer updateProductType(PageData pageData) {
        return productDao.updateProductType(pageData);
    }

    @Override
    public Integer insertProductStyle(PageData pageData) {
        return productDao.insertProductStyle(pageData);
    }

    @Override
    public Integer updateProductStyle(PageData pageData) {
        return productDao.updateProductStyle(pageData);
    }

    @Override
    public Integer insertProductSku(PageData pageData) {
        return productDao.insertProductSku(pageData);
    }

    @Override
    public Integer updateProductSku(PageData pageData) {
        return productDao.updateProductSku(pageData);
    }

    @Override
    public List<PageData> getProductType() {
        return productDao.getProductType();
    }

    @Override
    public List<PageData> getProductStyle() {
        return productDao.getProductStyle();
    }

    @Override
    public List<PageData> getProductSku(Integer productId) {
        return productDao.getProductSku(productId);
    }

    @Override
    public PageData getProductId(Integer productId) {
        return productDao.getProductId(productId);
    }

    @Override
    public List<PageData> getProductSkuVal(PageData pageData) {
        return productDao.getProductSkuVal(pageData);
    }

    @Override
    public List<PageData> getProductSkuId() {
        return productDao.getProductSkuId();
    }

    @Override
    public Integer insertProductSkuValue(PageData pageData) {
        return productDao.insertProductSkuValue(pageData);
    }

    @Override
    public Integer insertProductSkuType(PageData pageData) {

        return productDao.insertProductSkuType(pageData);
    }

    @Override
    public Integer updateProductSkuValue(PageData pageData) {
        return productDao.updateProductSkuValue(pageData);
    }

    @Override
    public Integer updateProductSkuType(PageData pageData) {

        return productDao.updateProductSkuType(pageData);
    }

    @Override
    public List<PageData> getHomeBanner() {
        return productDao.getHomeBanner();
    }

    @Override
    public Integer insertHomeBanner(PageData pageData) {
        return productDao.insertHomeBanner(pageData);
    }

    @Override
    public Integer updateHomeBanner(PageData pageData) {
        return productDao.updateHomeBanner(pageData);
    }

    @Override
    public List<PageData> getProductSkuType(Integer productId) {
        return productDao.getProductSkuType(productId);
    }

    @Override
    public Integer delectProduct_banner(PageData pageData) {

        return productDao.delectProduct_banner(pageData);
    }

    @Override
    public Integer delectProduct_detail(PageData pageData) {
        return productDao.delectProduct_detail(pageData);
    }

    @Override
    public Integer deleteProductSku(String product_id) {
        return productDao.deleteProductSku(product_id);
    }

    /**
     * 删除规格值
     * @param pageData
     * @return
     */
    @Override
    public Integer dateleProductValue(PageData pageData) {

        return productDao.dateleProductValue(pageData);
    }
    /**
     *根据商品的ID查看商品的规格，以及根据商品规格类型查询
     * @param pd
     * @return
     */
    @Override
    public List<PageData> findProductValueId(PageData pd)throws Exception{
        return productDao.findProductValueId(pd);
    }


    /**
     *查看所有的规格类型
     * @return
     */
    @Override
    public  List<PageData> findSkuType(PageData pageData)throws Exception{
        return productDao.findSkuType(pageData);
    }
    @Override
    public PageData findforEntity(PageData pd) throws Exception {
        return productDao.findforEntity(pd);
    }

}
