package com.pyjh.clothing.service;

import com.pyjh.clothing.entity.PageData;

/**
 * Created by 王豆豆 on 2018/9/5.
 */
public interface Balance_pay_amountService {
    /**
     *  查询对应数据  满送 满打折
     *
     * @param pageData
     * @return
     */
    PageData findForEntity(PageData pageData)throws Exception;

    /**
     * 增加
     *
     * @param pageData
     * @return
     * @throws Exception
     */
    public int save(PageData pageData) throws Exception;
}
