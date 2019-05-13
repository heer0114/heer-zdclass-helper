package com.heer.zdclass.model;

import org.springframework.stereotype.Component;

/**
 * @author heer_
 */
@Component
public class BaseUserInfo {

    private String account;

    private String name;

    private String identity;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
