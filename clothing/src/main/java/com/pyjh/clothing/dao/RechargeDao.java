package com.pyjh.clothing.dao;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by 王豆豆 on 2018/9/5.
 */
@Mapper
public interface RechargeDao {
    //添加充值金额
    Integer insertrecharge_priceByrecharge(PageData pageData);
    //查询充值金额 提交信息
    List<PageData> selectListInfo();
    //修改
    Integer update(PageData pageData);
    //查看充值记录
    PageData getByID(Integer recharge_id);
    //查询充值记录和消费记录
    List<PageData> findForEntity(PageData pageData);
}
