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
 * @date 2018/8/19 15:16
 */
@Mapper
public interface ExchangeDao {

    /**
     * 给新用户增加优惠卷
     *
     * @param pageData
     * @return
     */
    Integer insertExchange(PageData pageData);

    /**
     * 根据member_id查出自己的优惠卷
     *
     * @param member_id
     * @return
     */
    List<PageData> getExchange(@Param("member_id") Integer member_id);

    /**
     * 用户使用后或日期过后删除
     *
     * @param ec_id
     * @return
     */
    Integer deleteExchange(@Param("ec_id") Integer ec_id);

    /**
     *   根据id修改状态  已使用（3）
     *
     * @param pageData
     * @return
     */
    Integer updateExchange(PageData pageData);

}
