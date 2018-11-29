package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.RechargeDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.RechargeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 王豆豆 on 2018/9/5.
 */
@Service
public class RechargeServiceImpl implements RechargeService{
    @Resource
    RechargeDao rechargeDao;
    /**
     * 添加充值金额
     *
     * @return
     */
    @Override
    public Integer insertrecharge_priceByrecharge(PageData pageData)throws Exception {
        return rechargeDao.insertrecharge_priceByrecharge(pageData);
    }
    /**
     * 查询充值金额 提交信息
     *
     * @return
     */
    @Override
    public List<PageData> selectListInfo() {
        return rechargeDao.selectListInfo();
    }
    /**
     * 修改
     *
     * @return
     */
    @Override
    public Integer update(PageData pageData)throws Exception {
        return rechargeDao.update(pageData);
    }

    /**
     * 查看充值记录
     *
     * @return
     */
    @Override
    public PageData getByID(Integer recharge_id) {
        return rechargeDao.getByID(recharge_id);
    }

    /**
     * 查询充值记录和消费记录
     *
     * @return
     */
    @Override
    public List<PageData> findForEntity(PageData pageData) {
        return rechargeDao.findForEntity(pageData);
    }
}
