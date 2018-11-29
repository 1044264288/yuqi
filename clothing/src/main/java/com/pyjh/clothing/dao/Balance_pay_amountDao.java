package com.pyjh.clothing.dao;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by 王豆豆 on 2018/9/5.
 */
@Mapper
public interface Balance_pay_amountDao {

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
