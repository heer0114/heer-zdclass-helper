package com.heer.zdclass.core;

import com.heer.zdclass.execute.Execute;
import com.heer.zdclass.model.ClassInfo;
import com.heer.zdclass.model.UrlInfo;
import com.heer.zdclass.model.UserProperties;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


/**
 * @author: heer_
 * @date: 19/02/22 13:17
 * @description: 核心类
 */
@Component
public class CoreParser {

    /**
     * 解析助手
     */
    @Autowired
    private ParseAssistanter parseAssistanter;

    /**
     * 控制线程任务执行数量
     */
    private static final Semaphore PARALLEL_NUM = new Semaphore(5);
    /**
     * 解析dom属性名
     */
    private final static String ATTR_KEY_NAME = "name";
    private final static String ATTR_KEY_VALUE = "value";

    /**
     * 登录
     *
     * @param name uid
     * @param pwd  密码
     * @throws IOException io
     */
    public Map<String, String> login(String name, String pwd) throws IOException {
        Document document = Execute.getDocumentByMethod(UrlInfo.PAGE_URL, "GET", null);

        // 拿到登录表单
        List<Element> elementList = document.select("form");
        Elements elements = elementList.get(0).getAllElements();
        Map<String, Object> datas = new HashMap<>();
        // 设置登录数据
        elements.forEach(e -> {
            if (UserProperties.LOGIN_FORM_PROP_UID.equals(e.attr(ATTR_KEY_NAME))) {
                e.attr(ATTR_KEY_VALUE, name);
            }

            if (UserProperties.LOGIN_FORM_PROP_PASSWD.equals(e.attr(ATTR_KEY_NAME))) {
                e.attr(ATTR_KEY_VALUE, pwd);
            }

            if (e.attr(ATTR_KEY_NAME).length() > 0) {
                datas.put(e.attr("name"), e.attr("value"));
            }
        });
        Document loginDoc = Execute.getDocumentByMethod(UrlInfo.LOGIN_URL, "POST", datas);
        Elements redirectDoc = loginDoc.select("input[name='gointo']");
        String[] temp = redirectDoc.get(0).attr("onclick").split("'");
        System.out.println("登录成功，跳转连接：【" + temp[1] + "】");
        return index(temp[1]);
    }

    /**
     * 主页面
     *
     * @param redirectUrl 跳转链接
     * @throws IOException io
     */
    private Map<String, String> index(String redirectUrl) throws IOException {
        Document document = Execute.getDocumentByMethod(redirectUrl, "GET", null);
        // 获取首页信息url
        Elements elements = document.select("frame[NAME='content']");
        String firstPageUrl = elements.get(0).attr("src");
        return firstPage(firstPageUrl);
    }


    /**
     * 首页
     *
     * @param firstPageUrl 首页的url
     * @throws IOException io
     */
    private Map<String, String> firstPage(String firstPageUrl) throws IOException {
        // first page
        Document firstPage = Execute.getDocumentByMethod(firstPageUrl, "GET", null);

        return parseAssistanter.parseFirstPageInfo(firstPage);
    }

    /**
     * 根据课程名点播
     *
     * @param session session
     */
    public void demandBykeName(HttpSession session, String keName) throws IOException {
        String keUrl = parseAssistanter.getUrlMap(session).get(keName);
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName(keName);
        classInfo.setClassUrl(keUrl);
        Document document = Execute.getDocumentByMethod(keUrl, "GET", null);
        parseAssistanter.parseClassDocument(document, classInfo);
        // 创建任务
        DemandClassThread demandVideo = new DemandClassThread(classInfo, PARALLEL_NUM);
        Thread thread = new Thread(demandVideo);
        thread.setName(keName);
        thread.start();
    }

    /**
     * 点播全部课程
     *
     * @param session session
     */
    public void demandAllClass(HttpSession session) {
        parseAssistanter.getUrlMap(session).forEach((k, v) -> {
            try {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassName(k);
                classInfo.setClassUrl(v);
                Document document = Execute.getDocumentByMethod(v, "GET", null);
                parseAssistanter.parseClassDocument(document, classInfo);
                // 创建任务
                DemandClassThread demandVideo = new DemandClassThread(classInfo, PARALLEL_NUM);
                Thread thread = new Thread(demandVideo);
                thread.setName(k);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
