package com.mars.phms.utils.email;

/**
 * @author mars
 * @create 2020-04-07 8:12
 */
public interface EmailService {
    /**
     * 发送简单邮件
     * @param to 收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendSimpleEmail(String to,String subject,String content);

    /**
     * 发送验证码
     * @param to 收件人
     * @param code 验证码
     */
    void sendValidateCode(String to,String code);
}
