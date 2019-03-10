package com.heer.zdclass.main;

import com.heer.zdclass.core.CoreParser;
import com.heer.zdclass.model.UserProperties;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * @author: heer_
 * @date: 19/03/10 11:13
 * @description: 初始化，启动执行
 */
public class Start implements InitializingBean {

    private UserProperties userProperties;

    private CoreParser coreParser;

    public Start(UserProperties userProperties, CoreParser coreParser) {
        this.userProperties = userProperties;
        this.coreParser = coreParser;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        boolean flag= userProperties.getUsername() == null || userProperties.getUsername().isEmpty() &&
                userProperties.getPwd() == null || userProperties.getPwd().isEmpty();
        if (flag) {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入你的账号/密码：");
            String namepwd = sc.nextLine();
            String[] nameAndPwd = namepwd.split("/");
            String username = nameAndPwd[0];
            String password = nameAndPwd[1];
            userProperties.setUsername(username);
            userProperties.setPwd(password);
            System.out.println(userProperties.toString());
        }
        // 登录并初始化信息
        Map<String, String> keUrlMap = null;
        try {
            keUrlMap = coreParser.login(userProperties.getUsername(), userProperties.getPwd());
        } catch (Exception e) {
            throw new RuntimeException("登录异常，请确保账号/密码正确！");
        }
        System.out.println("课程加载完毕。开始执行点播任务，请稍等......");
        System.out.println("点播课程全部完成后，程序自动关闭！");
        // 点播全部课程
        coreParser.demandAllClass(keUrlMap);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("已完成全部任务，程序即将关闭。");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
