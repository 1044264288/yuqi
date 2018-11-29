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

import com.pyjh.clothing.dao.ExchangeDao;
import com.pyjh.clothing.entity.PageData;
import com.pyjh.clothing.service.ExchangeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liu
 * @date 2018/8/19 16:32
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Resource
    ExchangeDao exchangeDao;

    /**
     * 给新用户增加优惠卷
     *
     * @param pageData
     * @return
     */
    @Override
    public Integer insertExchange(PageData pageData) {
        return exchangeDao.insertExchange(pageData);
    }

    /**
     * 根据member_id查出自己的优惠卷
     *
     * @param member_id
     * @return
     */
    @Override
    public List<PageData> getExchange(Integer member_id) {
        return exchangeDao.getExchange(member_id);
    }

    /**
     *   根据id修改状态  已使用（3）
     *
     * @param pageData
     * @return
     */
    @Override
    public Integer updateExchange(PageData pageData) {
        return exchangeDao.updateExchange(pageData);
    }
}