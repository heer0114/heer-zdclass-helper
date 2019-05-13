package com.heer.zdclass.core;

import com.heer.zdclass.execute.Execute;
import com.heer.zdclass.gui.index.BaseInfoTextBulider;
import com.heer.zdclass.model.BaseUserInfo;
import com.heer.zdclass.model.UrlInfo;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;

/**
 * @author: heer_
 * @description: 心跳获取在线人数，在线时长
 */
public class HeartBeatThread implements Runnable {
    private JTextArea tipText;
    private BaseUserInfo baseUserInfo;

    public HeartBeatThread(JTextArea tipText, BaseUserInfo baseUserInfo) {
        this.tipText = tipText;
        this.baseUserInfo = baseUserInfo;
    }

    @Override
    public void run() {
        try {
            Document doc = Execute.getDocumentByMethod(UrlInfo.HEART_BEAT, "get", null);
            Elements elements = doc.select("body");
            String tempStr = elements.get(0).text();
            String[] arr = tempStr.split("#");
            String text = BaseInfoTextBulider.getInstance()
                    .setAccount(baseUserInfo.getAccount())
                    .setOnlineTime(arr[0])
                    .setOnlineNum(arr[1])
                    .setIdentity(baseUserInfo.getIdentity())
                    .setName(baseUserInfo.getName())
                    .build();
            tipText.setText("");
            tipText.append(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
