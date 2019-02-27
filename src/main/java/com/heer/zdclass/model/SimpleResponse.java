package com.heer.zdclass.model;

/**
 * @author: heer_
 * @date: 19/02/22 17:22
 * @description:
 */
public class SimpleResponse {

    private Object msg;

    public SimpleResponse(Object msg) {
        this.msg = msg;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
