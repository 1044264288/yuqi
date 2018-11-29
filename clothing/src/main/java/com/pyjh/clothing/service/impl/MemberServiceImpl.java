package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.MemberDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.MemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Resource
    MemberDao memberDao;

    @Override
    public PageData memberLogin(String mobile, String password) {
        PageData pageData = new PageData();
        pageData.put("mobile", mobile);
        pageData.put("password", password);
        return memberDao.memberLogin(pageData);
    }

    /**
     * 查询全部用户信息，模糊查询
     *
     * @return
     */
    @Override
    public List<PageData> getmember(String query_key) {
        PageData pageData=new PageData();
        //关键字
        if(query_key==null||"".equals(query_key)){
            query_key="";
        }
        pageData.put("query_key", query_key);
        return memberDao.getmember(pageData);
    }


    /**
     * 根据用户ID查看用户的个人信息
     *
     * @return
     */
    @Override
    public List<PageData> FindMemberId(PageData pageData) {

        return memberDao.FindMemberId(pageData);
    }
    /**
     * 根据member_id查询用户是不是新用户
     *
     * @param member_id
     * @return
     */
    @Override
    public PageData getMemberId(Integer member_id) {
        return memberDao.getMemberId(member_id);
    }

    @Override
    public boolean findForEntity(String mobile) {
        PageData pageData = new PageData();
        pageData.put("mobile", mobile);
        if (memberDao.findForEntity(pageData) == null) {
            System.out.println(memberDao.findForEntity(pageData));
            return true;
        }
        System.out.println(memberDao.findForEntity(pageData));
        return false;
    }

    @Override
    public PageData findEntity(PageData pd) {
        return memberDao.findForEntity(pd);
    }

    @Override
    public Integer addMember(PageData pageData) {
        return memberDao.addMember(pageData);
    }

    @Override
    public Integer editMember(PageData pageData) {
        return memberDao.editMember(pageData);
    }

    /**
     * 修改用户user_status状态
     *
     * @param member_id
     * @param user_status
     * @return
     */
    @Override
    public Integer updateMember(Integer member_id, Integer user_status) {
        return memberDao.updateMember(member_id, user_status);
    }


    /**
     * 修改用户status状态
     * @return
     */
    @Override
    public Integer updateMeberID(PageData pageData) {
        return memberDao.updateMeberID(pageData);
    }

    /**
     * 查询余额
     *
     * @return
     */
    @Override
    public PageData getbalance(Integer member_id) {
        return memberDao.getbalance(member_id);
    }
    /**
     * 查询是否存在
     *
     * @return
     */
    @Override
public   PageData findForEntity1(PageData pageData){
    return  memberDao.findForEntity1(pageData);
}

    /**
     * 查询手机号是否存在
     *
     * @return
     */
    @Override
    public  PageData findForEntity2(PageData pageData)throws Exception{
        return  memberDao.findForEntity2(pageData);
    }
    /**
     * 修改支付密码
     *
     * @param pageData
     * @return
     */
    @Override
    public Integer updateMemberpay_password(PageData pageData) {
        return memberDao.updateMemberpay_password(pageData);
    }
}
