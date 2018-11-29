package com.pyjh.clothing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author zhichaoWang
 * @description 常用工具类
 * @date 2017年12月6日
 */
public class CommonUtil {
    private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * @param date
     * @return
     * @throws ParseException
     * @description 日期字符串
     */
    public static String getDateString(Date date) throws ParseException {
        return simpleDateFormat.format(date);
    }

    /**
     * @param date
     * @return
     * @throws ParseException
     * @description 日期字符串
     */
    public static String getDateTimeString(Date date) throws ParseException {
        return simpleDateTimeFormat.format(date);
    }

    /**
     * @param object
     * @return true 表示 对象不为空 false 表示 对象为空
     * @description 判断对象是否为空, object != null
     */

    public static boolean isEmptyObject(Object object) {
        if (object != null && !"".equals(object)) {
            return true;
        }
        return false;
    }

    /**
     * @param object
     * @return true 表示 对象为空 false 表示 对象不为空
     * @description 判断参数是否为空, object == null || "".equals(object)
     */

    public static boolean paramIsNull(Object object) {
        if (object == null || "".equals(object)) {
            return true;
        }
        return false;
    }

    /**
     * 10000 的随机数
     *
     * @return string类型
     */
    public static String randomNum() {
        Random random = new Random(10000);
        return String.valueOf(random.nextInt(10000));
    }

    /**
     * GUID
     *
     * @return 唯一标识符
     */
    public static String createGUID() {
        return UUID.randomUUID().toString();
    }

    //生成随机数字和字母,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();
        //length为几位密码 
        for (int i = 0; i < length; i++) {
//            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; 
            //输出字母还是数字  
//            if( "char".equalsIgnoreCase(charOrNum) ) {  
            //输出是大写字母还是小写字母
//               int temp = random.nextInt(2) % 2 == 0 ? 65 : 97; 
            int temp = 65;
            val += (char) (random.nextInt(26) + temp);
//            }
//            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
//                val += String.valueOf(random.nextInt(10));  
//            }  
        }
        return val;
    }

    public static void main(String[] args) {
        int seq = 0;
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS", date, seq++);
        System.out.println("6:" + str + getStringRandom(6));
        //6:20180621190248LDYDUU
    }

    public static int RandomInt(int x, int y) {
        int i = x + (int) (Math.random() * (y - x + 1));
        return i;
    }

    public static String getSix() {
        int flag = new Random().nextInt(999999);
        if (flag < 100000) {
            flag += 100000;
        }
        return flag + "";

    }

}
