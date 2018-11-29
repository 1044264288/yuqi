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
package com.pyjh.clothing.dao;

import com.pyjh.clothing.entity.PageData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liu
 * @date 2018/8/19 17:57
 */
@Mapper
public interface CouponsDao {

    /**
     * 根据当前时间查询优惠卷
     *
     * @return
     */
    List<PageData> getCoupons(@Param("current_time") String current_time);

    /**
     * 根据名字查询优惠卷
     *
     * @return
     */
    List<PageData> getCouponsName(@Param("coupons_name") String coupons_name);

    /**
     *查询新用户优惠券 ,根据member_id 查看是否领取
     * @param pd
     * @return
     */
    List<PageData> newCoupons(PageData pd)throws Exception;

    /**
     * 用户领取优惠券，添加优惠券
     *
     * @param pageData
     * @return
     */
    Integer updateMember(PageData pageData);
    /**
     * 查询个人所有优惠券
     *
     * @param pageData
     * @return
     */
    List<PageData> listExchange(PageData pageData)throws Exception;



}
