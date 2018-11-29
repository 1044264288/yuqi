package com.pyjh.clothing.service;

import com.pyjh.clothing.entity.PageData;

import java.util.List;

/**
 * Created by 王豆豆 on 2018/9/13.
 */
public interface Product_order_detailService {
    /**
     * 查询商品详情
     *
     * @return
     */
    List<PageData> findforEntity(PageData pd)throws Exception;

    /**
     * 增加
     *
     * @return
     */
    Integer insertOrderDetail(PageData pd);
}
