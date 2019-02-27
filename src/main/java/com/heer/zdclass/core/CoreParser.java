package com.heer.zdclass.core;

import com.heer.zdclass.execute.Execute;
import com.heer.zdclass.model.ClassInfo;
import com.heer.zdclass.model.UrlInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author: heer_
 * @date: 19/02/22 13:17
 * @description: 核心类
 */
@Component
public class CoreParser {

    @Autowired
    private ParseAssistanter parseAssistanter;

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
            if ("uid".equals(e.attr("name"))) {
                e.attr("value", name);
            }

            if ("pw".equals(e.attr("name"))) {
                e.attr("value", pwd);
            }

            if (e.attr("name").length() > 0) {
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
     * @param redirectUrl
     * @throws IOException io
     */
    public Map<String, String> index(String redirectUrl) throws IOException {
        Document document = Execute.getDocumentByMethod(redirectUrl, "GET", null);
        // 获取首页信息url
        String firstPageUrl = parseIndexAssi(document, "frame[NAME='content']");
        return firstPage(firstPageUrl);
    }


    /**
     * 首页
     *
     * @param firstPageUrl
     * @throws IOException
     */
    public Map<String, String> firstPage(String firstPageUrl) throws IOException {
        // first page
        Document firstPage = Execute.getDocumentByMethod(firstPageUrl, "GET", null);

        return parseAssistanter.parseFirstPageInfo(firstPage);
    }

    /**
     * @param document document
     * @param queryCss 匹配
     * @return result
     */
    private String parseIndexAssi(Document document, String queryCss) {
        Elements elements = document.select(queryCss);
        return elements.get(0).attr("src");
    }

    /**
     * 获取课程的情况
     *
     * @param keName
     * @param request
     * @return
     */
    public ClassInfo getClassDetailInfo(String keName, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("username");
        session.setAttribute("keName", keName);
        Map<String, String> urlMap = (Map<String, String>) session.getAttribute(String.format("%skeUrlMap", name));
        String keUrl = urlMap.get(keName);
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName(keName);
        classInfo.setClassUrl(keUrl);
        Document document = Execute.getDocumentByMethod(keUrl, "GET", null);
        parseAssistanter.parseClassDocument(document, classInfo);
        session.setAttribute(String.format("%sclassInfo", name + keName), classInfo);
        return classInfo;
    }


    /**
     * @param request
     */
    public void demandBykeName(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("username");
        String keName = (String) session.getAttribute("keName");
        ClassInfo classInfo = (ClassInfo) session.getAttribute(String.format("%sclassInfo", name + keName));

        // 启动点播任务
        DemandVideo demandVideo = new DemandVideo(classInfo);
        Thread demandVideoThread = new Thread(demandVideo);
        demandVideoThread.start();
    }


    /**
     * 点播视频线程
     */
    private class DemandVideo implements Runnable {
        private ClassInfo classInfo;

        public DemandVideo(ClassInfo classInfo) {
            this.classInfo = classInfo;
        }

        @Override
        public void run() {
            Map<String, String> notSeenUrlMap = classInfo.getClassVideoUrlMap();
            Set<Map.Entry<String, String>> notseen = notSeenUrlMap.entrySet();
            for (Map.Entry<String, String> map : notseen) {
                String k = map.getKey();
                String v = map.getValue();

                System.out.println("点播了课程视频：【"+classInfo.getClassName()+"】" + k);
                try {
                    // 点播
                    Execute.getDocumentByMethod(v, "GET", null);
                    System.out.println("3.30分钟后,继续点播视频。请稍等...");
                    ParseAssistanter assistanter = new ParseAssistanter();
                    String creditStr = assistanter.getCredit(classInfo.getClassUrl());
                    String credit = creditStr.replaceAll("[^\\d+]", "");
                    if ("1010".equals(credit)) {
                        // 中断线程
                        Thread.currentThread().interrupt();
                        System.out.println("点播课程【" + classInfo.getClassName() + "】任务中断，原因：你的本门课程已修完10分。");
                        break;
                    }
                    // 点播后休眠3.30分钟
                    Thread.sleep(1000 * 60 * 3 + 1000 * 30);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
