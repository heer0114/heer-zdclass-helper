package com.heer.zdclass.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: heer_
 * @date: 19/02/23 18:08
 * @description:
 */
@Component
@ConfigurationProperties(prefix = "zzdx.core-properties")
public class CoreProperties {

    /**
     * 用户表单name
     */
    public final static String LOGIN_FORM_PROP_UID = "uid";
    public final static String LOGIN_FORM_PROP_PASSWD = "pw";

    /**
     * 用户名/密码
     */
    private String username;
    private String pwd;

    /**
     * 运行线程数【同时点播的课程数量】， 默认：5，最大不超过本学期的课程数
     */
    private int threadNum;

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "CoreProperties{" +
                "username='" + username + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
