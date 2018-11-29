package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.CustomDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.CustomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("customService")
public class CustomServiceImpl implements CustomService {

    @Resource
    CustomDao customDao;

    @Override
    public List<PageData> getCustomTypeByMember(Integer memberId) {
        return customDao.getCustomTypeByMember(memberId);
    }

    /**
     * 根据id查询体量类型信息
     *
     * @return
     */
    @Override
  public   List<PageData> newCustom(PageData pageData){
        return  customDao.newCustom(pageData);
    }
    @Override
  public   List<PageData> newCustomAll(PageData pageData){
        return  customDao.newCustomAll(pageData);
    }
    @Override
    public PageData getCustomByMember(Integer memberId,Integer state) {

        return customDao.getCustomByMember(memberId,state);
    }
    @Override
    public PageData getCustomByMember1(Integer memberId) {

        return customDao.getCustomByMember1(memberId);
    }

    @Override
    public boolean editCustomByMember(PageData pageData) {
        if (customDao.editCustomByMember(pageData) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Integer addCustomByMember(PageData pageData) {
       return customDao.addCustomByMember(pageData);
    }
    @Override
    public Integer addCustomValue(PageData pageData) {
        return customDao.addCustomValue(pageData);
    }

    @Override
    public PageData findforEntity(PageData pd)throws Exception {
        return customDao.findforEntity(pd);
    }

    @Override
    public int updateUnionid(PageData pd) throws Exception{
        return customDao.updateUnionid(pd);
    }


    @Override
    public List<PageData> findforList(PageData pd) throws Exception{
        return customDao.findforList(pd);
    }
    /**
     * 查询开始定制的分类，根据男女
     * @param pd
     * @return
     * @throws Exception
     */

    @Override
  public   List<PageData> findforTypes(PageData pd) throws Exception{
      return  customDao.findforTypes(pd);
  }
    /**
     * 查询开始定制的分类值根据分类的ID
     * @param pd
     * @return
     * @throws Exception
     */

    @Override
   public List<PageData> findforValues(PageData pd) throws Exception{
       return  customDao.findforValues(pd);
   }


    /**
     * 模糊查询量体值，根据ID查看
     * @param pd
     * @return
     * @throws Exception
     */

    @Override
    public List<PageData> findforTypeValues(PageData pd) throws Exception{
        return  customDao.findforTypeValues(pd);
    }

    /**
     * 添加到 custom——type里面-
     * @param pageData
     * @return
     */

    @Override
    public Integer addCustomtype(PageData pageData){
        return  customDao.addCustomtype(pageData);
    }

    /**
     * 添加到 custom——type_value里面
     * @param pageData
     * @return
     */

    @Override
    public Integer addCustomtypevalue(PageData pageData){
        return  customDao.addCustomtypevalue(pageData);
    }

    /**
     * 修改量体类型
     * @param pageData
     * @return
     */

    @Override
    public Integer editCustomtype(PageData pageData){
        return  customDao.editCustomtype(pageData);
    }

    /**
     *  修改量体类型值-
     * @param pageData
     * @return
     */

    @Override
    public Integer editCustomtypeValue(PageData pageData){

        return  customDao.editCustomtypeValue(pageData);
    }

    /**
     * 后台的量体修改
     * @param pageData
     * @return
     */
   public Integer editCustom(PageData pageData){
       return  customDao.editCustom(pageData);
   }

    /**
     * 修改量体信息录入
     *
     * @param pageData
     * @return
     */
    @Override
    public Integer updateCustomByMember(PageData pageData) {
        return customDao.updateCustomByMember(pageData);
    }

    /**
     * 查询最新的量体信息根据机器编号
     * @param pd
     * @return
     * @throws Exception
     */
    public  PageData firstcustom(PageData pd) throws Exception{
        return  customDao.firstcustom(pd);
    }

    /**
     * 查询最新的量体信息ID
     *
     * @return
     */
    @Override
    public Integer findMaxId() {
        return customDao.findMaxId();
    }

    @Override
    public Integer deleteCustomById(Integer id) {
        return customDao.deleteCustomById(id);
    }

    @Override
    public Integer updateCustomIsDefatul(Integer id) {
        Integer memberId = customDao.getMemberById(id);
        customDao.updateIsDefaultToZeroByMemberId(memberId);
        Integer res = customDao.updateIsDefaultById(id);
        return res;
    }


}
