package com.pyjh.clothing.service;

import com.pyjh.clothing.entity.PageData;


import java.util.List;

public interface MemberService {

    /**
     * 用户登陆
     *
     * @param mobile
     * @param password
     * @return
     */
    PageData memberLogin(String mobile, String password);

    /**
     * 根据用户ID查询用户信息
     *
     * @return
     */
    List<PageData> FindMemberId(PageData pageData);
    /**
     * 查询全部用户信息
     *
     * @return
     */
    List<PageData> getmember(String query_key);


    /**
     * 根据member_id查询用户是不是新用户
     *
     * @param member_id
     * @return
     */
    PageData getMemberId(Integer member_id);

    /**
     * 查询是否存在
     *
     * @return
     */
    boolean findForEntity(String mobile);

    /**
     * 获取用户
     * @param pd
     * @return
     */
    PageData findEntity(PageData pd);

    /**
     * 用户新增
     *
     * @param pageData
     * @return
     */
    Integer addMember(PageData pageData);

    /**
     * 用户新增
     *
     * @param pageData
     * @return
     */
    Integer editMember(PageData pageData);

    /**
     * 修改用户user_status状态
     *
     * @param member_id
     * @param user_status
     * @return
     */
    Integer updateMember(Integer member_id, Integer user_status);


    /**
     * 修改用户status状态
     *
     * @return
     */
    Integer updateMeberID(PageData pageData);

    /**
     * 查询余额
     *
     * @return
     */
    PageData getbalance(Integer member_id);
    /**
     * 查询是否存在
     *
     * @return
     */
    PageData findForEntity1(PageData pageData);
    /**
     * 查询手机号是否存在
     *
     * @return
     */
    PageData findForEntity2(PageData pageData)throws Exception;
    /**
     * 修改支付密码
     *
     * @param pageData
     * @return
     */
    Integer updateMemberpay_password(PageData pageData);
}
