package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.Balance_pay_amountDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.Balance_pay_amountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 王豆豆 on 2018/9/5.
 */
@Service
public class Balance_pay_amountServiceImpl implements Balance_pay_amountService{
    @Resource
    Balance_pay_amountDao balance_pay_amountDao;
    /**
     *  查询对应数据  满送 满打折
     *
     * @param pageData
     * @return
     */
    @Override
    public PageData findForEntity(PageData pageData) throws Exception {
        return balance_pay_amountDao.findForEntity(pageData);
    }
    /**
     * 增加
     *
     * @param pageData
     * @return
     * @throws Exception
     */
    @Override
    public int save(PageData pageData) throws Exception {
        return balance_pay_amountDao.save(pageData);
    }


}
