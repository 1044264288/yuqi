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

/**
 * @author liu
 * @date 2018/8/21 9:35
 */
@Mapper
public interface UserDao {

    /**
     * 用户登陆
     *
     * @param accountname
     * @param password
     * @return
     */
    PageData getUser(@Param("accountname") String accountname, @Param("password") String password);

    /**
     * 修改密码
     *
     * @param password
     * @param platform_user_id
     * @return
     */
    Integer updateUser(@Param("platform_user_id") Integer platform_user_id, @Param("password") String password);

    /**
     * 根据用户id查询用户
     *
     * @param platform_user_id
     * @return
     */
    PageData getUserId(@Param("platform_user_id") Integer platform_user_id);

    /**
     * 新增用户
     *
     * @param pageData
     * @return
     */
    Integer insertUser(PageData pageData);
}
