package com.heer.zdclass.gui.index;

/**
 * @author heer_
 *
 * <p>
 * 构建基本信息显示文本
 * </p>
 */
public class BaseInfoTextBulider {


    private String account;

    private String name;

    private String identity;

    private String onlineTime;

    private String onlineNum;

    // 单利
    private BaseInfoTextBulider() {
    }

    private final static BaseInfoTextBulider INSTANCE = new BaseInfoTextBulider();

    public static BaseInfoTextBulider getInstance() {
        return INSTANCE;
    }

    public BaseInfoTextBulider setIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public BaseInfoTextBulider setAccount(String account) {
        this.account = account;
        return this;
    }

    public BaseInfoTextBulider setName(String name) {
        this.name = name;
        return this;
    }

    public BaseInfoTextBulider setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
        return this;
    }

    public BaseInfoTextBulider setOnlineNum(String onlineNum) {
        this.onlineNum = onlineNum;
        return this;
    }

    public String build() {

        return String.format(
                "账号：%s\n" +
                        "姓名：%s\n" +
                        "系统身份：%s\n" +
                        "在线时间：%s分钟\n" +
                        "在线人数：%s\n",
                this.account,
                this.name,
                this.identity,
                this.onlineTime,
                this.onlineNum);
    }

}
