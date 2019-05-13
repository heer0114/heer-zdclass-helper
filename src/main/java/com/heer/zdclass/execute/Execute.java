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
    public static Document getDocumentByMethod(String url, String method, Map<String, Object> datas) throws IOException {

        URL uri = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        // 禁用缓存
        connection.setUseCaches(Boolean.FALSE);
        connection.setDoOutput(true);
        //设置请求头信息
        connection.addRequestProperty(ZdclassRequestHeaderConstant.USER_AGENT, ZdclassRequestHeaderConstant.USER_AGENT_VALUE);
        connection.addRequestProperty(ZdclassRequestHeaderConstant.ACCEPT, ZdclassRequestHeaderConstant.ACCEPT_VALUE);

        // 请求类型
        connection.setRequestMethod(method.toUpperCase());
        connection.setInstanceFollowRedirects(true);
        connection.connect();
        // 设置求情参数
        if ("post".equals(method.toLowerCase()) && datas != null) {
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
