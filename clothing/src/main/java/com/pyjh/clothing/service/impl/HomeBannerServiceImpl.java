package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.HomeBannerDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.HomeBannerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("homeBannerService")
public class HomeBannerServiceImpl implements HomeBannerService {

    @Resource
    HomeBannerDao homeBannerDao;

    @Override
    public List<PageData> getHomeBanner(PageData pageData) {
        return homeBannerDao.getHomeBanner(pageData);
    }

    @Override
    public boolean addHomeBanner(String url, String targetUrl, Integer ifVideo) {
        PageData pageData = new PageData();
        pageData.put("url", url);
        pageData.put("targetUrl", targetUrl);
        pageData.put("ifVideo", ifVideo);
        if (homeBannerDao.addHomeBanner(pageData) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int editHomeBanner(PageData pd) {
        return homeBannerDao.editHomeBanner(pd);
    }

    @Override
    public boolean addTypeBanner(String url, String targetUrl, Integer ifVideo) {
        PageData pageData = new PageData();
        pageData.put("url", url);
        pageData.put("targetUrl", targetUrl);
        pageData.put("ifVideo", ifVideo);
        if (homeBannerDao.addTypeBanner(pageData) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int editTypeBanner(PageData pd) {
      return homeBannerDao.editTypeBanner(pd);
    }

    @Override
    public List<PageData> getTypeBanner(PageData pageData) {
        return homeBannerDao.getTypeBanner(pageData);
    }
    @Override
    public PageData findforEntityHom(PageData pd) throws Exception {
        // TODO Auto-generated method stub
        return homeBannerDao.findforEntityHom(pd);
    }
    @Override
    public PageData findforEntityType(PageData pd) throws Exception {
        // TODO Auto-generated method stub
        return homeBannerDao.findforEntityType(pd);
    }
}
