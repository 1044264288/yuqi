package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.AddressDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Resource
    AddressDao addressDao;

    @Override
    public List<PageData> getMemberAddress(Integer memberId) {
        return addressDao.getMemberAddress(memberId);
    }

    @Override
    public Integer addMemberAddress(PageData pageData) {
        return addressDao.addMemberAddress(pageData);
    }

    @Override
    public Integer editMemberAddress(PageData pageData) {
        return addressDao.editMemberAddress(pageData);
    }

    @Override
    public Integer deleteMemAddress(Integer addressId) {
        return addressDao.deleteMemAddress(addressId);
    }
    @Override
    public PageData findforEntity(PageData pd) throws Exception {
        return addressDao.findforEntity(pd);
    }
}
