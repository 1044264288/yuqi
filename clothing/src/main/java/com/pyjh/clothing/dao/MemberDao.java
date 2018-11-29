package com.pyjh.clothing.dao;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberDao {

    /**
     * 用户登陆
     *
     * @param pageData
     * @return
     */
    PageData memberLogin(PageData pageData);

    /**
     * 查询全部用户信息
     *
     * @return
     */
    List<PageData> getmember(PageData pageData);

    /**
     * 根据用户ID查询用户信息
     *
     * @return
     */
    List<PageData> FindMemberId(PageData pageData);

    /**
     * 根据member_id查询用户是不是新用户
     *
     * @param member_id
     * @return
     */
    PageData getMemberId(@Param("member_id") Integer member_id);

    /**
     * 查询是否存在
     *
     * @return
     */
    PageData findForEntity(PageData pageData);

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
    Integer updateMember(@Param("member_id") Integer member_id, @Param("user_status") Integer user_status);

    Integer updateMeberID(PageData pageData);

    /**
     * 查询余额
     *
     * @return
     */
    PageData getbalance(@Param("member_id") Integer member_id);
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
