//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖保佑             永无BUG 
//          佛曰:  
//                  写字楼里写字间，写字间里程序员；  
//                  程序人员写程序，又拿程序换酒钱。  
//                  酒醒只在网上坐，酒醉还来网下眠；  
//                  酒醉酒醒日复日，网上网下年复年。  
//                  但愿老死电脑间，不愿鞠躬老板前；  
//                  奔驰宝马贵者趣，公交自行程序员。  
//                  别人笑我忒疯癫，我笑自己命太贱；  
//                  不见满街漂亮妹，哪个归得程序员？
package com.pyjh.clothing.service.impl;

import com.pyjh.clothing.dao.BalanceDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.BalanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liu
 * @date 2018/8/28 16:03
 */
@Service
public class BalanceServiceImpl implements BalanceService {

    @Resource
    BalanceDao balanceDao;

    /**
     * 查询充值金额
     *
     * @return
     */
    @Override
    public List<PageData> getBalance(PageData pageData) {
        List<PageData> pageDataList = balanceDao.getBalance(pageData);
        if (pageDataList != null) {
            return pageDataList;
        }
        return null;
    }

     /**
     * 增加充值金额
     *
     * @param pageData
     * @return
     */
    @Override
    public Integer insertBalance(PageData pageData) {
        Integer i = balanceDao.insertBalance(pageData);
        if (i > 0) {
            return i;
        }
        return 0;
    }

    /**
     * 修改充值金额
     *
     * @param pageData
     * @return
     */
    @Override
    public Integer updateBalance(PageData pageData) {
        Integer i = balanceDao.updateBalance(pageData);
        if (i > 0) {
            return i;
        }
        return 0;
    }

    /**
     * 删除充值金额
     *
     * @param pageData
     * @return
     */
    @Override
    public Integer deleteBanlance(PageData pageData) {
        Integer i = balanceDao.deleteBanlance(pageData);
        if (i > 0) {
            return i;
        }
        return 0;
    }
}
