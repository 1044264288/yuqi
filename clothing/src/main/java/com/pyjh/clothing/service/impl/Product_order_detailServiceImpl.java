package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.Product_order_detailDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.Product_order_detailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 王豆豆 on 2018/9/13.
 */
@Service("Product_order_detailService")
public class Product_order_detailServiceImpl implements Product_order_detailService{
    @Resource
    Product_order_detailDao product_order_detailDao;
    @Override
    public List<PageData> findforEntity(PageData pd) throws Exception {
        return product_order_detailDao.findforEntity(pd);
    }
    @Override
    public Integer insertOrderDetail(PageData pd) {
        return product_order_detailDao.insertOrderDetail(pd);
    }
}
