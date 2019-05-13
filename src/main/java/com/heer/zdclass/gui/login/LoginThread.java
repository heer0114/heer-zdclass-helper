package com.heer.zdclass.gui.login;

import com.heer.zdclass.core.CoreParser;
import com.heer.zdclass.gui.index.IndexFrame;
import com.heer.zdclass.model.BaseUserInfo;
import com.heer.zdclass.model.CoreProperties;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author heer_
 */
public class LoginThread extends Thread {
    private JDialog dialog1;
    private JLabel errMsg;
    private CoreProperties coreProperties;
    private IndexFrame indexFrame;
    private CoreParser coreParser;
    private BaseUserInfo baseUserInfo;

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    LoginThread(IndexFrame indexFrame, JDialog dialog1, JLabel errMsg,
                CoreProperties coreProperties, CoreParser coreParser,
                BaseUserInfo baseUserInfo) {
        this.indexFrame = indexFrame;
        this.dialog1 = dialog1;
        this.errMsg = errMsg;
        this.coreProperties = coreProperties;
        this.coreParser = coreParser;
        this.baseUserInfo = baseUserInfo;
    }

    @Override
    public void run() {
        try {
            coreParser.login(coreProperties.getUsername(), coreProperties.getPwd());
            // 加载得分情况
            indexFrame.loadingData();
        } catch (Exception e) {
            String errMsg = "登录出现异常！请确保账户/密码正确！";
            System.out.println(errMsg);
            e.printStackTrace();
            this.errMsg.setText(errMsg);
            this.errMsg.setVisible(true);
            LoginDialog.eventCount.set(0);
            return;
        }
        // close login dialog
        this.dialog1.dispose();

    }
}
