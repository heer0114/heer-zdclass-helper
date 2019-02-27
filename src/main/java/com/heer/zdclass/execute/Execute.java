package com.heer.zdclass.execute;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author: heer_
 * @date: 19/02/22 12:59
 * @description: 请求访问执行类
 */
public class Execute {

    private static String USER_AGENT = "User-Agent";

    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";

    public static Document getDocumentByMethod(String url, String method, Map<String, Object> datas) throws IOException {

        URL uri = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        // 禁用缓存
        connection.setUseCaches(Boolean.FALSE);
        connection.setDoOutput(true);
        //设置请求头信息
        connection.addRequestProperty(USER_AGENT, USER_AGENT_VALUE);
        // 请求类型
        connection.setRequestMethod(method);
        connection.setInstanceFollowRedirects(true);
        connection.connect();
        // 设置求情参数
        if ("post".equals(method.toLowerCase()) && datas != null) {
            // 已form表单的形式传递传递参数 默认可以不传
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())
            ){
                StringBuffer datasBuffer = new StringBuffer();
                datas.forEach((k, v) -> {
                    datasBuffer.append(k + "=" + v + "&");
                });
                outputStream.writeBytes(datasBuffer.toString());
            }
        }
        return Jsoup.parse(connection.getInputStream(), "GBK", url);
    }
}
