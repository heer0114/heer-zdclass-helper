package com.heer.zdclass.model;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: heer_
 * @date: 19/02/22 16:23
 * @description: 课程信息
 */
@Component
public class ClassInfo {

    /**
     * 课程名称
     */
    private String className;

    /**
     * 课程地址
     */
    private String classUrl;

    /**
     * 获得的学分
     */
    private String credit;

    /**
     * 总学分
     */
    private String creditCount;

    /**
     * 视频总数
     */
    private int classVideoCount;

    /**
     * 已经看过的课程数量
     */
    private int seenVideoCount;


    /**
     * 存储每个课程视频连接地址
     */
    private Map<String, String> classVideoUrlMap = new LinkedHashMap<>();


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassUrl() {
        return classUrl;
    }

    public void setClassUrl(String classUrl) {
        this.classUrl = classUrl;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(String creditCount) {
        this.creditCount = creditCount;
    }

    public int getClassVideoCount() {
        return classVideoCount;
    }

    public void setClassVideoCount(int classVideoCount) {
        this.classVideoCount = classVideoCount;
    }

    public int getSeenVideoCount() {
        return seenVideoCount;
    }

    public void setSeenVideoCount(int seenVideoCount) {
        this.seenVideoCount = seenVideoCount;
    }

    public Map<String, String> getClassVideoUrlMap() {
        return classVideoUrlMap;
    }

    public void setClassVideoUrlMap(String key, String value) {
        this.classVideoUrlMap.put(key, value);
    }
}
