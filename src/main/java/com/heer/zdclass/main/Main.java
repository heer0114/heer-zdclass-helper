package com.heer.zdclass.main;

import com.heer.zdclass.core.CoreParser;
import com.heer.zdclass.model.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author: heer_
 * @date: 19/03/10 11:07
 * @description: 初始化 上下文配置
 */
@Configuration
public class Main {

    @Autowired
    private UserProperties userProperties;
    @Autowired
    private CoreParser coreParser;

    @Bean
    public Start initialContext() {
        // 输入用户名/密码
       return new Start(userProperties, coreParser);
    }
}
