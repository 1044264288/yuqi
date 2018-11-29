package com.pyjh.clothing.dao;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by 王豆豆 on 2018/9/13.
 */
@Mapper
public interface Product_order_detailDao {

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
