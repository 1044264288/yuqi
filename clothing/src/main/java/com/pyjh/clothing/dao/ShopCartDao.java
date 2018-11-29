package com.pyjh.clothing.dao;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopCartDao {

    /**
     * 获取用户购物车
     *
     * @return
     */
    List<PageData> getMemberShopCart(Integer memberId);

    /**
     * 购物车新增商品
     *
     * @param pageData
     * @return
     */
    Integer addMemberShopCart(PageData pageData);

    /**
     * 购物车新增商品
     *
     * @param pageData
     * @return
     */
    Integer editMemberShopCart(PageData pageData);

    /**
     * 删除购物车
     *
     * @param shopCartId
     * @return
     */
    Integer deleteMemChopCart(Integer shopCartId);

    /**
     * 新增购物车规格
     *
     * @param pageData
     * @return
     */
    Integer addShopOrderSku(PageData pageData);

    /**
     * 查询最大购物车id
     *
     * @return
     */
    Integer getMaxShopCart();

    /**
     * 购物车规格
     *
     * @return
     */
    Integer updateShopChartSku(PageData pageData);

    /**
     * 查询购物车规格
     *
     * @param cartId
     * @return
     */
    List<PageData> getCartSku(Integer cartId);

    /**
     * 查询
     *
     * @param pd
     * @return
     */
    PageData findforEntity(PageData pd)throws Exception;
    /**
     * 删除
     *
     * @param pd
     * @return
     */
    Integer delete(PageData pd)throws Exception;  //删除
}
