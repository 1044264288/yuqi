package com.pyjh.clothing.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * @author zhichaoWang
 * @description 邮件工具类
 * @date 2017年12月7日
 * <p>
 * JavaMail 版本: 1.6.0
 * JDK 版本: JDK 1.7 以上（必须）
 */
public class EmailUtil {

    // ----------------------------------------------------------------------


    /**
     * @param theme主题
     * @param content内容
     * @param mailbox邮箱
     */
    public static void sendEmail(String theme, String content, String mailbox) {
        Email email = new SimpleEmail();
        email.setCharset("UTF-8");
        // 设置邮件服务器
        email.setHostName("smtp.163.com");
        //设置用户名密码
        email.setAuthenticator(new DefaultAuthenticator("pengyoujuhui168@163.com", "yuqipyjh168"));
        email.setSSLOnConnect(true);
        email.setSmtpPort(465);
        try {
            //发件人
            email.setFrom("pengyoujuhui168@163.com");
            //主题
            email.setSubject(theme);
            email.setMsg(content);
            //收件人
            email.addTo(mailbox);
            //发送
            email.send();
            System.out.println("邮件发送成功");
        } catch (EmailException e) {
            e.printStackTrace();
            System.out.println("邮件发送失败：" + e);
        }

    }
}

