package com.pyjh.clothing.service;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductService {
//查询**************************************
    /**
     * 查询所有商品
     *
     * @return
     */
    Map<String, Object> getProduct(PageData pageData);

    /**
     * 根据商品id查询banner
     *
     * @return
     */
    List<PageData> getProductBanner(Integer productId);


    /**
     * 根据商品id查询detail
     *
     * @return
     */
    List<PageData> getProductDetail(Integer productId);
    /**
     * 获取商品分类列表
     *
     * @return
     */
    List<PageData> getProductType();

    /**
     * 查询商品风格
     *
     * @return
     */
    List<PageData> getProductStyle();

    /**
     * 查询商品规格
     *
     * @return
     */
    List<PageData> getProductSku(Integer productId);

    /**
     * 查询商品规格cal
     *
     * @param pageData
     * @return
     */
    List<PageData> getProductSkuVal(PageData pageData);

    /**
     * 根据商品id查询基本信息
     *
     * @param productId
     * @return
     */
    PageData getProductId(Integer productId);

    /**
     * 查询全部商品规格
     *
     * @return
     */
    List<PageData> getProductSkuId();

    /**
     * 查询全部homebanner图片
     *
     * @return
     */
    List<PageData> getHomeBanner();
    /**
     * 查询增加最大id
     *
     * @return
     */
    Integer selectProduct();
    /**
     *根据商品的ID查看商品的规格，以及根据商品规格类型查询
     * @param pd
     * @return
     */
    List<PageData> findProductValueId(PageData pd)throws Exception;


    /**
     *查看所有的规格类型
     * @return
     */
    List<PageData>  findSkuType(PageData pageData)throws Exception;

    //单个查询
    PageData findforEntity(PageData pd)throws Exception;

//增加*****************************************************************************
    /**
     * 增加商品
     *
     * @param pageData
     * @return
     */
    Integer insertProduct(PageData pageData);



    /**
     * 增加商品轮播图
     *
     * @param pageData
     * @return
     */
    Integer insertProduct_banner(PageData pageData);


    /**
     * 新增商品类型
     *
     * @param pageData
     * @return
     */
    Integer insertProductType(PageData pageData);

    /**
     * 增加商品图片
     *
     * @param pageData
     * @return
     */
    Integer insertProduct_detail(PageData pageData);

    /**
     * 新增商品风格
     *
     * @param pageData
     * @return
     */
    Integer insertProductStyle(PageData pageData);


    /**
     * 新增商品规格
     *
     * @param pageData
     * @return
     */
    Integer insertProductSku(PageData pageData);

    /**
     * 新增商品规格
     *
     * @param pageData
     * @return
     */
    Integer insertProductSkuValue(PageData pageData);

    /**
     * 新增商品规格类型
     *
     * @param pageData
     * @return
     */
    Integer insertProductSkuType(PageData pageData);

    //修改************************************************************************************************

    /**
     * 修改商品信息
     *
     * @param pageData
     * @return
     */
    Integer updateProduct(PageData pageData);

    /**
     * 修改商品状态
     *
     * @param pageData
     * @return
     */
    Integer updateProductStatus(PageData pageData);


    /**
     * 修改商品类型
     *
     * @param pageData
     * @return
     */
    Integer updateProductType(PageData pageData);


    Integer deleteProductSku(String product_id);

    /**
     * 修改商品风格
     *
     * @param pageData
     * @return
     */
    Integer updateProductStyle(PageData pageData);


    /**
     * 修改商品规格
     *
     * @param pageData
     * @return
     */
    Integer updateProductSku(PageData pageData);

    /**
     * 修改商品规格
     *
     * @param pageData
     * @return
     */
    Integer updateProductSkuValue(PageData pageData);

    /**
     * 修改商品规格
     *
     * @param pageData
     * @return
     */
    Integer updateProductSkuType(PageData pageData);


    /**
     * 增加homebanner图片
     *
     * @param pageData
     * @return
     */
    Integer insertHomeBanner(PageData pageData);

    /**
     * 修改homebanner图片状态
     *
     * @param pageData
     * @return
     */
    Integer updateHomeBanner(PageData pageData);

    /**
     * 查询规格类型
     *
     * @param productId
     * @return
     */
    List<PageData> getProductSkuType(Integer productId);

    /**
     * 查看所有规格值
     * @param value_id,query_key
     * @return
     * @throws Exception
     */
    List<PageData>  forEntitySkuId(Integer value_id,String query_key)throws Exception;
    /**
     * 删除detail图
     *
     * @param pageData
     * @return
     */
    Integer delectProduct_detail(PageData pageData);

    /**
     * 删除banner图
     *
     * @param pageData
     * @return
     */
    Integer delectProduct_banner(PageData pageData);
    /**
     * 删除规格值
     *
     * @param pageData
     * @return
     */
    Integer dateleProductValue(PageData pageData);



}
