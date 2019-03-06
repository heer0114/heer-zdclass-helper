package com.heer.zdclass.core;

import com.heer.zdclass.execute.Execute;
import com.heer.zdclass.model.ClassInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: heer_
 * @date: 19/02/22 16:30
 * @description: 解析助手
 */
@Component
public class ParseAssistanter {

    private ExecutorService executorService = Executors.newFixedThreadPool(6);

    private List<ClassInfo> classInfos = new ArrayList<>();

    public Map<String, String> parseFirstPageInfo(Document firstPage) {
        Elements mytds = firstPage.select("td[class='mytd1']");
        Elements aelements = mytds.get(1).select("p").select("a");
        Map<String, String> keUrlMap = new LinkedHashMap<>();
        System.out.println("==============你的学习课程==============");
        aelements.forEach(a -> {
            String className = a.text().replace("<网上考试>", "");
            System.out.println(className);
            keUrlMap.put(className, a.attr("href"));
        });
        System.out.println("===========================================");
        return keUrlMap;
    }

    /**
     * 获取课程的视频信息
     *
     * @param document
     * @param classInfo
     * @return
     * @throws IOException
     */
    public void parseClassDocument(Document document, ClassInfo classInfo) throws IOException {
        // 视频总数
        AtomicInteger videoCount = new AtomicInteger(0);
        // 点播过的数量
        AtomicInteger seenVideoCount = new AtomicInteger(0);

        // 获取视频页数据
        Elements elements = document.select("td[class='mytd1']");
        String keVideoPageUrl = elements.get(1).select("a").get(0).attr("href");
        Document videoDoc = Execute.getDocumentByMethod(keVideoPageUrl, "GET", null);
        Elements videos = videoDoc.select("table[bordercolor='#79A8E8']");
        // 学分情况
        Element credit = videos.get(0).select("tr").first().select("td").get(1);
        classInfo.setCredit(credit.text());
        // 视频信息
        Elements videoUrls = videos.get(0).select("td[class='mytd1'] a");
        videoUrls.forEach(a -> {
            Element e = a.select("font").get(0);
            // 派出已经点播的视频
            if ("#0000FF".equals(e.attr("color"))) {
                classInfo.setClassVideoUrlMap(a.text(), a.attr("href"));
                videoCount.incrementAndGet();
            } else {
                seenVideoCount.incrementAndGet();
            }
        });
        classInfo.setClassVideoCount(videoCount.get());
        classInfo.setSeenVideoCount(seenVideoCount.get());
    }

    /**
     * 获取学分
     * @param keUrl
     * @return
     * @throws IOException
     */
    public String getCredit(String keUrl) throws IOException {

        Document document = Execute.getDocumentByMethod(keUrl, "GET", null);

        // 获取视频页数据
        Elements elements = document.select("td[class='mytd1']");
        String keVideoPageUrl = elements.get(1).select("a").get(0).attr("href");
        Document videoDoc = Execute.getDocumentByMethod(keVideoPageUrl, "GET", null);
        Elements videos = videoDoc.select("table[bordercolor='#79A8E8']");
        // 学分情况
        Element credit = videos.get(0).select("tr").first().select("td").get(1);
        return credit.text();
    }

    /**
     * 从session中拿去课程连接
     * @param session se
     * @return Map
     */
    public Map<String, String> getUrlMap(HttpSession session) {
        String name = String.valueOf(session.getAttribute("username"));
        return (Map<String, String>) session.getAttribute(String.format("%skeUrlMap", name));
    }

}
