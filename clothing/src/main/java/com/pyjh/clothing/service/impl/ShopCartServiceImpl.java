package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.ShopCartDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.ShopCartService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("shopCartService")
public class ShopCartServiceImpl implements ShopCartService {

    @Resource
    ShopCartDao shopCartDao;

    @Override
    public List<PageData> getMemberShopCart(Integer memberId) {
        return shopCartDao.getMemberShopCart(memberId);
    }

    @Override
    public Integer addMemberShopCart(PageData pageData) {
        return shopCartDao.addMemberShopCart(pageData);
    }

    @Override
    public Integer editMemberShopCart(PageData pageData) {
        return shopCartDao.editMemberShopCart(pageData);
    }

    @Override
    public Integer deleteMemChopCart(Integer shopCartId) {
        return shopCartDao.deleteMemChopCart(shopCartId);
    }

    @Override
    public Integer addShopOrderSku(PageData pageData) {
        return shopCartDao.addShopOrderSku(pageData);
    }

    @Override
    public Integer getMaxShopCart() {
        return shopCartDao.getMaxShopCart();
    }

    @Override
    public Integer updateShopChartSku(PageData pageData) {
        return shopCartDao.updateShopChartSku(pageData);
    }

    @Override
    public List<PageData> getCartSku(Integer cartId) {
        return shopCartDao.getCartSku(cartId);
    }
    @Override
    public PageData findforEntity(PageData pd) throws Exception {
        return shopCartDao.findforEntity(pd);
    }
    @Override
    public Integer delete(PageData pd) throws Exception {
        return shopCartDao.delete(pd);
    }
}
