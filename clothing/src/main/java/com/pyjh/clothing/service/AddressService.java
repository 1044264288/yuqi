package com.pyjh.clothing.service;

import com.pyjh.clothing.entity.PageData;

import java.util.List;

public interface AddressService {

    /**
     * 获取用户收获地址
     *
     * @param memberId
     * @return
     */
    List<PageData> getMemberAddress(Integer memberId);

    /**
     * 添加收获地址
     *
     * @return
     */
    Integer addMemberAddress(PageData pageData);

    /**
     * 修改收获地址
     *
     * @param pageData
     * @return
     */
    Integer editMemberAddress(PageData pageData);

    /**
     * 删除收获地址
     *
     * @param addressId
     * @return
     */
    Integer deleteMemAddress(Integer addressId);
    /**
     * 查看
     *
     * @param pd
     * @return
     */
    PageData findforEntity(PageData pd)throws Exception;
}
