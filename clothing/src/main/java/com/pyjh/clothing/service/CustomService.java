package com.pyjh.clothing.service;

import com.pyjh.clothing.entity.PageData;

import java.util.List;

public interface CustomService {

    /**
     * 根据id查询体量类型信息
     *
     * @return
     */
    List<PageData> getCustomTypeByMember(Integer memberId);

    /**
     * 根据id查询体量信息
     *
     * @return
     */
    PageData getCustomByMember(Integer memberId,Integer state);
    /**
     * 根据id查询体量类型信息
     *
     * @return
     */
    List<PageData> newCustom(PageData pageData);
    List<PageData> newCustomAll(PageData pageData);
    /**
     * 根据id查询体量信息
     *
     * @return
     */
    PageData getCustomByMember1(Integer memberId);

    /**
     * 修改用户体量信息
     *
     * @return
     */
    boolean editCustomByMember(PageData pageData);

    /**
     * 用户量体信息录入
     *
     * @param pageData
     * @return
     */
    Integer addCustomByMember(PageData pageData);
    /**
     * 用户量体选择值的录入
     *
     * @param pageData
     * @return
     */
    Integer addCustomValue(PageData pageData);


    PageData findforEntity(PageData pd) throws Exception;

    /**
     * 查询最新的量体信息根据机器编号
     * @param pd
     * @return
     * @throws Exception
     */
    PageData firstcustom(PageData pd) throws Exception;
    /**
     * 添加量体表的unionid
     *
     * @param pd
     * @return
     * @throws Exception
     */
    int updateUnionid(PageData pd) throws Exception;

    List<PageData> findforList(PageData pd) throws Exception;
    /**
     * 查询开始定制的分类，根据男女
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> findforTypes(PageData pd) throws Exception;
    /**
     * 查询开始定制的分类值根据分类的ID
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> findforValues(PageData pd) throws Exception;

    /**
     * 模糊查询量体值，根据ID查看
     * @param pd
     * @return
     * @throws Exception
     */
    List<PageData> findforTypeValues(PageData pd) throws Exception;

    /**
     * 添加到 custom——type里面-
     * @param pageData
     * @return
     */
    Integer addCustomtype(PageData pageData);

    /**
     * 添加到 custom——type_value里面
     * @param pageData
     * @return
     */
    Integer addCustomtypevalue(PageData pageData);

    /**
     * 修改量体类型
     * @param pageData
     * @return
     */
    Integer editCustomtype(PageData pageData);

    /**
     *  修改量体类型值-
     * @param pageData
     * @return
     */
    Integer editCustomtypeValue(PageData pageData);

    /**
     * 后台的量体修改
     * @param pageData
     * @return
     */
    Integer editCustom(PageData pageData);

    /**
     * 修改量体信息录入
     *
     * @param pageData
     * @return
     */
    Integer updateCustomByMember(PageData pageData);

    /**
     * 修改量体信息录入
     *
     * @return
     */
    public Integer findMaxId();


    /**
     * 根据customid删除量体信息单条记录
     */
    public Integer deleteCustomById(Integer id);

    /**
     * 将指定ID的量体信息修改为默认选中信息
     */

    public Integer updateCustomIsDefatul(Integer id);
}
