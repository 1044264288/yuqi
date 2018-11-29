package com.pyjh.clothing.dao;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HomeBannerDao {

    /**
     * 获取首页banner图
     *
     * @return
     */
    List<PageData> getHomeBanner(PageData pageData);

    /**
     * 上传首页banner图
     *
     * @return
     */
    Integer addHomeBanner(PageData pageData);

    /**
     * 修改首页banner图
     *
     * @return
     */
    Integer editHomeBanner(PageData pageData);

    /**
     * 上传首页banner图
     *
     * @return
     */
    Integer addTypeBanner(PageData pageData);

    /**
     * 修改首页banner图
     *
     * @return
     */
    Integer editTypeBanner(PageData pageData);

    /**
     * 获取分类banner
     *
     * @return
     */
    List<PageData> getTypeBanner(PageData pageData);

    /***
     * 根據id查看
     * @param pd
     * @return
     * @throws Exception
     */
     PageData findforEntityHom(PageData pd)throws Exception;

    /***
     * 根據id查看
     * @param pd
     * @return
     * @throws Exception
     */
    PageData findforEntityType(PageData pd)throws Exception;
}
