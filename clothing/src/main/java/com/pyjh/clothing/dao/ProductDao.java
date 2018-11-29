package com.pyjh.clothing.dao;

import com.github.pagehelper.Page;
import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductDao {

    /**
     * 查询所有商品
     *
     * @return
     */
    List<PageData> getProduct(PageData pageData);

    /**
     * 查询增加最大id
     *
     * @return
     */
    Integer selectProduct();

    /**
     * 根据商品id查询基本信息
     *
     * @param productId
     * @return
     */
    PageData getProductId(@Param("productId") Integer productId);

    /**
     * 修改商品状态
     *
     * @param pageData
     * @return
     */
    Integer updateProductStatus(PageData pageData);

    /**
     * 增加商品
     *
     * @param pageData
     * @return
     */
    Integer insertProduct(PageData pageData);

    /**
     * 修改商品信息
     *
     * @param pageData
     * @return
     */
    Integer updateProduct(PageData pageData);

    /**
     * ProductBanner
     */
    /**
     * 根据商品id查询banner
     *
     * @return
     */
    List<PageData> getProductBanner(Integer productId);

    /**
     * 增加商品轮播图
     *
     * @param pageData
     * @return
     */
    Integer insertProduct_banner(PageData pageData);

    /**
     * 查询全部homebanner图片
     *
     * @return
     */
    List<PageData> getHomeBanner();

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
     * 根据商品id查询detail
     *
     * @return
     */
    List<PageData> getProductDetail(Integer productId);

    /**
     * 增加详情图商品图片
     *
     * @param pageData
     * @return
     */
    Integer insertProduct_detail(PageData pageData);


    /**
     * 获取商品分类列表
     *
     * @return
     */
    List<PageData> getProductType();

    /**
     * 新增商品类型
     *
     * @param pageData
     * @return
     */
    Integer insertProductType(PageData pageData);

    /**
     * 修改商品类型
     *
     * @param pageData
     * @return
     */
    Integer updateProductType(PageData pageData);

    /**
     * ProductStyle
     */
    /**
     * 查询商品风格
     *
     * @return
     */
    List<PageData> getProductStyle();

    /**
     * 新增商品风格
     *
     * @param pageData
     * @return
     */
    Integer insertProductStyle(PageData pageData);

    /**
     * 修改商品风格
     *
     * @param pageData
     * @return
     */
    Integer updateProductStyle(PageData pageData);

    /**
     * 查询商品规格
     *
     * @return
     */
    List<PageData> getProductSku(Integer productId);

    /**
     * 新增商品规格
     *
     * @param pageData
     * @return
     */
    Integer insertProductSku(PageData pageData);

    /**
     * 修改商品规格
     *
     * @param pageData
     * @return
     */
    Integer updateProductSku(PageData pageData);

    /**
     * 查询全部商品规格
     *
     * @return
     */
    List<PageData> getProductSkuId();

    /**
     * 新增商品规格Value
     *
     * @param pageData
     * @return
     */
    Integer insertProductSkuValue(PageData pageData);

    /**
     * 修改商品规格Value
     *
     * @param pageData
     * @return
     */
    Integer updateProductSkuValue(PageData pageData);

    /**
     * 新增商品规格
     *
     * @param pageData
     * @return
     */
    Integer insertProductSkuType(PageData pageData);

    /**
     * 修改商品规格
     *
     * @param pageData
     * @return
     */
    Integer updateProductSkuType(PageData pageData);

    /**
     * 查询商品规格cal
     *
     * @param pageData
     * @return
     */
    List<PageData> getProductSkuVal(PageData pageData);

    Integer deleteProductSku(@Param("product_id") String product_id);
   
    /**
     * 查询规格类型
     *
     * @param productId
     * @return
     */
    List<PageData> getProductSkuType(Integer productId);

    /**
     *查看所有的规格值，以及查看详情
     * @param pd
     * @return
     */
    List<PageData>  forEntitySkuId(PageData pd)throws Exception;

    /**
     * 删除banner图
     *
     * @param pageData
     * @return
     */
    Integer delectProduct_banner(PageData pageData);

    /**
     * 删除detail图
     *
     * @param pageData
     * @return
     */
    Integer delectProduct_detail(PageData pageData);
    /**
     * 删除规格值
     *
     * @param pageData
     * @return
     */
    Integer dateleProductValue(PageData pageData);


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

    /**
     * 根据ID删除商品信息
     * @param pageData
     * @return
     */
    Integer  deleteID(PageData pageData);
}
