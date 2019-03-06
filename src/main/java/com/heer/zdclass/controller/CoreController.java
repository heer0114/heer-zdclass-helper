package com.heer.zdclass.controller;

import com.heer.zdclass.core.CoreParser;
import com.heer.zdclass.model.SimpleResponse;
import com.heer.zdclass.model.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * @author: heer_
 * @date: 19/02/22 17:15
 * @description: 登录
 */
@RestController
public class CoreController {

    @Autowired
    private CoreParser coreParser;
    @Autowired
    private UserProperties userProperties;

    /**
     * 登录
     *
     * @return
     */
    @GetMapping("/login")
    public SimpleResponse login(HttpSession session) {
        try {
            String username = (String)session.getAttribute("username");
            if(username != null && !username.isEmpty()){
                return new SimpleResponse("你已经登录！");
            }
            // 把用户名存到Session
            session.setAttribute("username", userProperties.getUsername());
            Map<String, String> classUrlMap = coreParser.login(userProperties.getUsername(), userProperties.getPwd());
            // 把得到的url存到session
            session.setAttribute(String.format("%skeUrlMap", userProperties.getUsername()), classUrlMap);
            return new SimpleResponse("登录成功！");
        } catch (IOException e) {
            return new SimpleResponse("登录失败" + e.getMessage());
        }
    }

    /**
     * 更具课程名称点播
     *
     * @param keName
     * @return
     * @throws IOException
     */
    @GetMapping("/demand/{keName}")
    public SimpleResponse getClassDetailInfo(@PathVariable String keName, HttpSession session) throws IOException {
        coreParser.demandBykeName(session, keName);
        return new SimpleResponse("点播成功。");
    }

    /**
     * 点播课程
     * @return
     */
    @GetMapping("/demandAll")
    public SimpleResponse demandAll(HttpSession session){
        coreParser.demandAllClass(session);
        return new SimpleResponse("点播成功");
    }
}
