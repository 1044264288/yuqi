package com.pyjh.clothing.dao;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDao {

    /**
     * 获取订单信息
     *
     * @param pageData
     * @return
     */
    List<PageData> getOrder(PageData pageData);

    /**
     * 新增订单信息
     *
     * @param pageData
     * @return
     */
    Integer addOrder(PageData pageData);

    /**
     * 新增订单详情
     *
     * @param pageData
     * @return
     */
    Integer addOrderDetail(PageData pageData);

    /**
     * 获取最大订单id
     *
     * @return
     */
    Integer getMaxOrderId();

    /**
     * 修改订单状态
     *
     * @param pageData
     * @return
     */
    Integer updateOrder(PageData pageData);
    Integer updateOrders(PageData pageData);
    /**
     * 查询全部快递名称
     *
     * @return
     */
    List<PageData> getExpress();

    /**
     * 查询全部订单和快递单
     *
     * @return
     */
    List<PageData> getOrderEx();

    /**
     * 根据订单名字查出订单id
     *
     * @param order_code
     * @return
     */
    PageData getOrdercode(@Param("order_code") String order_code);

    /**
     * 充值记录
     * @param pageData
     * @return
     */
    Integer addRecharge(PageData pageData);

    /**
     * 查询订单规格
     *
     * @param orderId
     * @return
     */
    List<PageData> getOrderSku(Integer orderId);


    /**
     * 查看订单详情
     * @param pageData
     * @return
     */
    List<PageData> findDetailId(PageData pageData);

    /**
     *点击发货，根据order_id，查看订单的收货信息
     * @param pageData
     * @return
     */
    List<PageData> findExpress(PageData pageData);
    /**
     *查看订单详情会员量体规格
     * @param pageData
     * @return
     */
    List<PageData> findDetailSku(PageData pageData);
    /**
     *  查看余额支付通知
     * @param pOrder
     * @return
     */
    PageData findfor(PageData pOrder);

    /**
     *根据订单号查询
     * @param pageData
     * @return
     */
    List<PageData> queryProductByOrderCode(PageData pageData);
    /**
     *根据状态查询
     * @param pageData
     * @return
     */
    List<PageData> queryOrderByStatus(PageData pageData);

    /**
     *根据手机号查询
     * @param pageData
     * @return
     */
    List<PageData> queryProductBymobile(PageData pageData);

    List<PageData> findDetailSkus(PageData pageData);
    List<PageData> findDetailCustomInfo(PageData pageData);
}
