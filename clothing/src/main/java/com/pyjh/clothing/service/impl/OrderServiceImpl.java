package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.OrderDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderDao orderDao;

    @Override
    public List<PageData> getOrder(PageData pageData) {
        //  PageData pageData = new PageData();
        List<PageData> data = orderDao.getOrder(pageData);
        if (data != null) {
            return data;
        }
        return null;
    }

    @Override
    public Integer addOrder(PageData pageData) {
        return orderDao.addOrder(pageData);
    }

    @Override
    public Integer addOrderDetail(PageData pageData) {
        return orderDao.addOrderDetail(pageData);
    }

    @Override
    public Integer getMaxOrderId() {
        return orderDao.getMaxOrderId();
    }

    @Override
    public Integer updateOrder(PageData pageData) {
        Integer i = orderDao.updateOrder(pageData);
        if (i > 0) {
            return i;
        }
        return null;
    }

    @Override
    public List<PageData> getExpress() {
        List<PageData> pageDataList = orderDao.getExpress();
        if (pageDataList != null) {
            return pageDataList;
        }
        return null;
    }

    @Override
    public List<PageData> getOrderEx() {
        List<PageData> pageDataList = orderDao.getOrderEx();
        if (pageDataList != null) {
            return pageDataList;
        }
        return null;
    }

    @Override
    public PageData getOrdercode(String order_code) {
        PageData pageData = orderDao.getOrdercode(order_code);
        if (pageData != null) {
            return pageData;
        }
        return null;
    }

    @Override
    public Integer addRecharge(PageData pageData) {
        if (orderDao.addRecharge(pageData) > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public List<PageData> getOrderSku(Integer orderId) {
        return orderDao.getOrderSku(orderId);
    }

    @Override
    public List<PageData> findDetailId(PageData pageData) {
        return orderDao.findDetailId(pageData);
    }

    @Override
    public List<PageData> findExpress(PageData pageData) {
        return orderDao.findExpress(pageData);
    }

    @Override
    public List<PageData> findDetailSku(PageData pageData) {
        return orderDao.findDetailSku(pageData);
    }
    @Override
    public Integer updateOrders(PageData pageData){
        return  orderDao.updateOrders(pageData);
    }
    /**
     *  查看余额支付通知
     * @param pOrder
     * @return
     */
    @Override
    public PageData findfor(PageData pOrder) {
        return orderDao.findfor(pOrder);
    }

    /**
     *根据订单号查询
     * @param pageData
     * @return
     */
    @Override
    public List<PageData> queryProductByOrderCode(PageData pageData) {
        return orderDao.queryProductByOrderCode(pageData);
    }
    /**
     *根据状态查询
     * @param pageData
     * @return
     */
    @Override
    public List<PageData> queryOrderByStatus(PageData pageData) {
        return orderDao.queryOrderByStatus(pageData);
    }

    /**
     *根据手机号查询
     * @param pageData
     * @return
     */
    @Override
    public List<PageData> queryProductBymobile(PageData pageData) {
        return orderDao.queryProductBymobile(pageData);
    }
    @Override
    public List<PageData> findDetailSkus(PageData pageData) {
        return orderDao.findDetailSkus(pageData);
    }
    @Override
    public List<PageData> findDetailCustomInfo(PageData pageData) {
        return orderDao.findDetailCustomInfo(pageData);
    }

}
