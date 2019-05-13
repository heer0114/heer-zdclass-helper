package com.heer.zdclass.core;

import com.heer.zdclass.execute.Execute;
import com.heer.zdclass.gui.index.IndexFrame;
import com.heer.zdclass.model.BaseUserInfo;
import com.heer.zdclass.model.ClassInfo;
import com.heer.zdclass.model.CoreProperties;
import com.heer.zdclass.model.UrlInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
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

    @Autowired
    private CoreProperties coreProperties;

    @Autowired
    private BaseUserInfo baseUserInfo;

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
    public void login(String name, String pwd) throws Exception {
        Document document = Execute.getDocumentByMethod(UrlInfo.PAGE_URL, "GET", null);

        // 拿到登录表单
        List<Element> elementList = document.select("form");
        Elements elements = elementList.get(0).getAllElements();
        Map<String, Object> datas = new HashMap<>();
        // 设置登录数据
        elements.forEach(e -> {
            if (CoreProperties.LOGIN_FORM_PROP_UID.equals(e.attr(ATTR_KEY_NAME))) {
                e.attr(ATTR_KEY_VALUE, name);
            }

            if (CoreProperties.LOGIN_FORM_PROP_PASSWD.equals(e.attr(ATTR_KEY_NAME))) {
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
        System.out.println("开始加载你的课程，请稍等......");
        index(temp[1]);
    }

    /**
     * 注销
     */
    public void logout() throws IOException {
        if (!UrlInfo.LOGOUT_URL.isEmpty()) {
            coreProperties.setUsername("");
            Execute.getDocumentByMethod(UrlInfo.LOGOUT_URL, "GET", null);
        }
    }

    /**
     * 主页面
     *
     * @param redirectUrl 跳转链接
     * @throws IOException io
     */
    private void index(String redirectUrl) throws IOException {
        // index page
        Document document = Execute.getDocumentByMethod(redirectUrl, "GET", null);

        // index top page
        Elements mytopPage = document.select("frame[name='mytop']");
        String mytopPageUrl = mytopPage.get(0).attr("src");
        // 解析基本信息
        Document document1 = Execute.getDocumentByMethod(mytopPageUrl, "GET", null);
        Element userInfoTable = document1.select("table").get(2);
        Elements infos = userInfoTable.select("font[color='#008000']");
        baseUserInfo.setAccount(infos.get(0).text());
        baseUserInfo.setName(infos.get(1).text());
        baseUserInfo.setIdentity(infos.get(2).text());

        // 解析 注销url
        Element table1 = document1.select("table").get(1);
        Elements as = table1.select("a");
        Element logoutUrlEle = as.get(as.size() - 1);
        UrlInfo.LOGOUT_URL = logoutUrlEle.attr("href");

        String heartbeatUrlTemp = mytopPageUrl.replaceAll("mygettita", "zzjgetonlinetime") + "&sid=%s&ww6=652";
        long sid = Math.round(Math.random() * 800000 + 100000);
        // 取在线时长 和  在线人数
        UrlInfo.HEART_BEAT = String.format(heartbeatUrlTemp, sid);

        // index first page[content]
        Elements firstPage = document.select("frame[NAME='content']");
        String firstPageUrl = firstPage.get(0).attr("src");
        firstPage(firstPageUrl);
    }

    /**
     * 首页
     *
     * @param firstPageUrl 首页的url
     * @throws IOException io
     */
    private void firstPage(String firstPageUrl) throws IOException {
        // first page
        Document firstPage = Execute.getDocumentByMethod(firstPageUrl, "GET", null);

        parseAssistanter.parseFirstPageInfo(firstPage);
    }

    /**
     * 点播全部课程
     *
     * @param keUrlMap 全部课程url
     */
    public void demandAllClass(IndexFrame indexFrame, Map<String, String> keUrlMap) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(keUrlMap.size());
        // 课程信息列表
        List<ClassInfo> keList = this.getKeInfoList(keUrlMap);
        // 启动任务
        this.startTaskByConsole(indexFrame, keList, countDownLatch);
        // 等待全部任务结束
        countDownLatch.await();
    }

    /**
     * 启动任务
     *
     * @param keList         课程list
     * @param countDownLatch 计数器
     */
    private void startTaskByConsole(IndexFrame indexFrame, List<ClassInfo> keList, CountDownLatch countDownLatch) {
        // 初始化线程数量
        if (coreProperties.getThreadNum() == 0) {
            // 如没有设置线程数，同时执行线程数为当前学期课程数量
            coreProperties.setThreadNum(keList.size());
        }

        //控制线程任务执行数量
        final Semaphore parallelNum = new Semaphore(coreProperties.getThreadNum());

        System.out.println("同时运行线程数【课程数量】：" + coreProperties.getThreadNum());

        // 启动任务
        keList.forEach(classInfo -> {
            // 创建任务
            DemandClassThread demandVideo = new DemandClassThread(
                    indexFrame,
                    classInfo,
                    parallelNum,
                    countDownLatch);
            Thread thread = new Thread(demandVideo);
            thread.setName(classInfo.getClassName());
            thread.start();
        });
    }

    /**
     * 获取课程信息列表
     *
     * @param keUrlMap url
     * @return list
     */
    public List<ClassInfo> getKeInfoList(Map<String, String> keUrlMap) {
        // 课程信息列表
        List<ClassInfo> keList = new ArrayList<>();
        keUrlMap.forEach((k, v) -> {
            try {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassName(k);
                classInfo.setClassUrl(v);
                Document document = Execute.getDocumentByMethod(v, "GET", null);
                parseAssistanter.parseClassDocument(document, classInfo);
                keList.add(classInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return keList;
    }
}
