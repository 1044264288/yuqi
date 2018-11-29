package com.pyjh.clothing.service;

import com.pyjh.clothing.entity.PageData;

import java.util.List;

public interface HomeBannerService {

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
    boolean addHomeBanner(String url, String targetUrl, Integer ifVideo);

    /**
     * 修改首页banner图
     *
     * @return
     */
    int editHomeBanner(PageData pd);

    /**
     * 上传首页banner图
     *
     * @return
     */
    boolean addTypeBanner(String url, String targetUrl, Integer ifVideo);

    /**
     * 修改首页banner图
     *
     * @return
     */
    int editTypeBanner(PageData pd);

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
