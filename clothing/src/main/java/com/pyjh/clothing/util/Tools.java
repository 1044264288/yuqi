package com.pyjh.clothing.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    /**
     * 随机生成六位数验证码
     *
     * @return
     */
    public static String getRandomNum() {
        Random r = new Random();
        return r.nextInt(900000) + 100000 + "";// (Math.random()*(999999-100000)+100000)
    }
    /**
     * @description 判断参数是否为空,object == null || "".equals(object)
     * @param object
     * @return true 表示 对象为空 false 表示 对象不为空
     */

    public static boolean paramIsNull(Object object){
        if(object == null || "".equals(object)){
            return true;
        }
        return false;
    }

    /**
     * 检测字符串是否不为空(null,"","null")
     *
     * @param s
     * @return 不为空则返回true，否则返回false
     */
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"null".equals(s);
    }

    /**
     * 检测字符串是否为空(null,"","null")
     *
     * @param s
     * @return 为空则返回true，不否则返回false
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || "null".equals(s);
    }

    /**
     * 字符串转换为字符串数组
     *
     * @param str        字符串
     * @param splitRegex 分隔符
     * @return
     */
    public static String[] str2StrArray(String str, String splitRegex) {
        if (isEmpty(str)) {
            return null;
        }
        return str.split(splitRegex);
    }

    /**
     * 用默认的分隔符(,)将字符串转换为字符串数组
     *
     * @param str 字符串
     * @return
     */
    public static String[] str2StrArray(String str) {
        return str2StrArray(str, ",\\s*");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String date2Str(Date date) {
        return date2Str(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String timestamp2Str(Timestamp time) {
        Date date = null;
        if (null != time) {
            date = new Date(time.getTime());
        }
        return date2Str(date);
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String date2TimeStr(Date date) {
        return date2Str(date, "HH:mm:ss");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
     *
     * @param date
     * @return
     */
    public static Date str2Date(String date) {
        if (notEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new Date();
        } else {
            return null;
        }
    }

    /**
     * 按照参数format的格式，日期转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String date2Str(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 按照参数format的格式，日期转字符串
     *
     * @param date
     * @return
     */
    public static String sqlDate2Str(java.sql.Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 把时间根据时、分、秒转换为时间段
     *
     * @param StrDate
     */
    public static String getTimes(String StrDate) {
        String resultTimes = "";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;

        try {
            now = new Date();
            Date date = df.parse(StrDate);
            long times = now.getTime() - date.getTime();
            long day = times / (24 * 60 * 60 * 1000);
            long hour = (times / (60 * 60 * 1000) - day * 24);
            long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

            StringBuffer sb = new StringBuffer();
            // sb.append("发表于：");
            if (hour > 0) {
                sb.append(hour + "小时前");
            } else if (min > 0) {
                sb.append(min + "分钟前");
            } else {
                sb.append(sec + "秒前");
            }

            resultTimes = sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultTimes;
    }

    /**
     * 写txt里的单行内容
     *
     * @param fileP   文件路径
     * @param content 写入的内容
     */
    public static boolean writeFile(String fileP, String content) {
        String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";
        filePath = (filePath.trim() + fileP.trim()).substring(6).trim();
        if (filePath.indexOf(":") != 1)
            filePath = File.separator + filePath;
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(content);
            writer.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            Pattern regex = Pattern
                    .compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    // /**
    // * 检测KEY是否正确
    // * @param paraname 传入参数
    // * @param FKEY 接收的 KEY
    // * @return 为空则返回true，不否则返回false
    // */
    // public static boolean checkKey(String paraname, String FKEY){
    // paraname = (null == paraname)? "":paraname;
    // return MD5.md5(paraname+DateUtil.getDays()+",fh,").equals(FKEY);
    // }
    //

    /**
     * 读取txt里的单行内容
     *
     * @param fileP 文件路径
     */
    public static String readTxtFile(String fileP) {
        try {

            String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../"; // 项目路径
            filePath = filePath.replace("file:/", "");
            filePath = filePath.replace("%20", " ");
            filePath = filePath.trim() + fileP.trim();
            if (filePath.indexOf(":") != 1) {
                filePath = File.separator + filePath;
            }
            String encoding = "utf-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); // 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    return lineTxt;
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
        }
        return "";
    }

    // public static void main(String[] args) {
    // System.out.println(getRandomNum());
    // }
    //
    // public static int SmsClientSendCode(String s){
    // JSONArray json= (JSONArray) JSONObject.fromObject(s).get("Rets");
    // JSONObject newjson= (JSONObject) json.get(0);
    // int Rspcode=(Integer) newjson.get("Rspcode");
    // return Rspcode;
    // }

    /**
     * 生成随机码，包含大小写，数字
     *
     * @param number 随机码长度
     * @return 随机码
     */
    public static String getStringRandom(int number) {
        String str = "";
        Random rand = new Random();
        for (int i = 0; i < number; i++) {
            int num = rand.nextInt(3);
            switch (num) {
                case 0:
                    char c1 = (char) (rand.nextInt(26) + 'a');// 生成随机小写字母
                    str += c1;
                    break;
                case 1:
                    char c2 = (char) (rand.nextInt(26) + 'A');// 生成随机大写字母
                    str += c2;
                    break;
                case 2:
                    str += rand.nextInt(10);// 生成随机数字
            }
        }
        return str;
    }

    /**
     * 生成随机数字（区域范围整数）
     *
     * @param x 起始范围
     * @param y 截至范围
     * @return 区域随机数
     */
    public static int RandomInt(int x, int y) {
        int i = x + (int) (Math.random() * (y - x + 1));
        return i;
    }

    /**
     * 生成随机数字（区域范围,保留两位小数）
     *
     * @param x 起始范围
     * @param y 截至范围
     * @return 区域随机数
     */
    public static double RandomDouble(double x, double y) {
        DecimalFormat df = new DecimalFormat("#.00");
        double d = Double.parseDouble(df.format(x + Math.random() * y % (y - x + 1)));
        if (d < x) {
            d = x;
        }
        if (d > y) {
            d = y;
        }
        return d;
    }

    public static int SmsClientSendCode(String s) {
        JSONObject json = JSONObject.parseObject(s);
        JSONArray newjson = (JSONArray) JSONArray.parse(json.get("Rets").toString());
        JSONObject nejson = JSONObject.parseObject(newjson.get(0).toString());
        int Rspcode = (Integer) nejson.get("Rspcode");
        return Rspcode;
    }
}
