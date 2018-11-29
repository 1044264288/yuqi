package com.pyjh.clothing.dao;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomDao {

    /**
     * 根据id查询体量类型信息
     *
     * @return
     */
    List<PageData> getCustomTypeByMember(Integer memberId);
    /**
     * 根据id查询体量类型信息
     *
     * @return
     */
    List<PageData> newCustom(PageData pageData);
    /**
     * 根据id查询体量类型信息
     *
     * @return
     */
    List<PageData> newCustomAll(PageData pageData);

    /**
     * 根据id查询体量信息
     *
     * @return
     */
    PageData getCustomByMember(Integer memberId,Integer state);

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
    Integer editCustomByMember(PageData pageData);

    /**
     * 后台的量体修改
     * @param pageData
     * @return
     */
    Integer editCustom(PageData pageData);
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

    /**
     * 根据ID查看量体详情
     * @param pd
     * @return
     * @throws Exception
     */
    PageData findforEntity(PageData pd) throws Exception;

    /**
     * 查看最新的量体信息，根据机器编号
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
     * 查询开始定制的分类，根据男女,模糊查询
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
     * 修改量体信息录入
     *
     * @param pageData
     * @return
     */
    Integer updateCustomByMember(PageData pageData);

    /**
     * 获取量体信息最大ID
     *
     * @return
     */
    Integer findMaxId();

    Integer deleteCustomById(Integer id);

    Integer getMemberById(Integer id);

    void updateIsDefaultToZeroByMemberId(Integer memberId);

    Integer updateIsDefaultById(Integer id);
}
