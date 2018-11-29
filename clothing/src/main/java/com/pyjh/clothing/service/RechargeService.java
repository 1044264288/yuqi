package com.pyjh.clothing.service;

import com.pyjh.clothing.entity.PageData;

import java.util.List;

/**
 * Created by 王豆豆 on 2018/9/5.
 */
public interface RechargeService {
    /**
     * 添加充值金额
     *
     * @return
     */
    Integer insertrecharge_priceByrecharge(PageData pageData)throws Exception;
    /**
     * 查询充值金额 提交信息
     *
     * @return
     */
    List<PageData> selectListInfo();
    /**
     * 修改
     *
     * @return
     */
    Integer update(PageData pageData)throws Exception;
    /**
     * 查看充值记录
     *
     * @return
     */
    PageData getByID(Integer recharge_id);

    //查询充值记录和消费记录
    List<PageData> findForEntity(PageData pageData);
}
