package com.heer.zdclass.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: heer_
 * @date: 19/02/23 18:08
 * @description:
 */
@Component
@ConfigurationProperties(prefix = "user")
public class UserProperties {
    public final static String LOGIN_FORM_PROP_UID = "uid";
    public final static String LOGIN_FORM_PROP_PASSWD = "pw";

    private String username;
    private String pwd;

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
}
