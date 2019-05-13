package com.heer.zdclass.model;

import java.util.Map;

/**
 * @author: heer_
 * @date: 19/02/22 16:32
 * @description: 各种url
 */
public class UrlInfo {
    /**
     * 官网 url
     */
    public static final String PAGE_URL = "http://vls3.zzu.edu.cn/";
    /**
     * 登录 url
     */
    public static final String LOGIN_URL = "http://123.15.57.15/sss/zzjlogin.dll/login";

    /**
     * 在线时长/人数url
     */
    public static String HEART_BEAT = "";

    /**
     * 注销
     */
    public static String LOGOUT_URL = "";

    /**
     登录并初始化信息
     */
    public static Map<String, String> KE_URL_MAP = null;

}
