/*
 * Created by JFormDesigner on Tue Mar 12 16:29:40 CST 2019
 */

package com.heer.zdclass.gui.login;

import com.heer.zdclass.core.CoreParser;
import com.heer.zdclass.gui.index.IndexFrame;
import com.heer.zdclass.model.BaseUserInfo;
import com.heer.zdclass.model.CoreProperties;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heer
 * <p>
 * 登录窗口
 */
@Component
public class LoginDialog extends JPanel {

    @Autowired
    private CoreProperties coreProperties;
    @Autowired
    private CoreParser coreParser;
    @Autowired
    private BaseUserInfo baseUserInfo;

    static AtomicInteger eventCount = new AtomicInteger(0);

    private IndexFrame indexFrame;

    /**
     * 初始化并启动
     *
     * @param indexFrame ind
     */
    public void startup(IndexFrame indexFrame) {

        this.indexFrame = indexFrame;
        initComponents();
        this.passwordField.setEchoChar('*');
    }

    /**
     * login event
     *
     * @param e e
     */
    private void loginActionPerformed(ActionEvent e) {
        if (LoginDialog.eventCount.get() != 0) {
            this.errMsg.setText("正在登录，请勿重复点击！");
            this.errMsg.setVisible(true);
            return;
        }

        String username = this.usernameField.getText();
        String password = String.valueOf(this.passwordField.getPassword());

        if (username.isEmpty()) {
            this.errMsg.setText("请输入账号！");
            this.errMsg.setVisible(true);
            return;
        }
        LoginDialog.eventCount.set(1);
        this.coreProperties.setUsername(username);
        this.coreProperties.setPwd(password);

//        Authentication authentication = authenticationMapper.selectAuthByAccount(username);
//        if (authentication == null || 1 != authentication.getAccreditFlag()) {
//            this.errMsg.setText("该账号还未在该程序进行注册！");
//            this.errMsg.setVisible(true);
//            LoginDialog.eventCount.set(0);
//            return;
//        }
        // 执行登录任务
        LoginThread loginThread = new LoginThread(indexFrame, dialog1,
                errMsg, coreProperties, coreParser, baseUserInfo);
        loginThread.start();
        this.errMsg.setText("正在登录...");
        this.errMsg.setVisible(true);
    }


    /**
     * 注册事件
     *
     * @param e
     */
    private void registerActionPerformed(ActionEvent e) {

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialog1 = new JDialog();
        panel1 = new JPanel();
        errMsg = new JLabel();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton();

        //======== dialog1 ========
        {
            dialog1.setResizable(false);
            dialog1.setVisible(true);
            dialog1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog1.setTitle("Login");
            dialog1.setMinimumSize(new Dimension(300, 300));
            dialog1.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 16));
            dialog1.setBackground(Color.white);
            Container dialog1ContentPane = dialog1.getContentPane();
            dialog1ContentPane.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[]",
                    // rows
                    "[]"));

            //======== panel1 ========
            {
                panel1.setBackground(Color.white);
                panel1.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[30!]" +
                                "[150!]" +
                                "[150!]" +
                                "[30!]",
                        // rows
                        "[30!]" +
                                "[55!]" +
                                "[55!]" +
                                "[60!]" +
                                "[30!]"));

                //---- errMsg ----
                errMsg.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 15));
                errMsg.setForeground(Color.red);
                errMsg.setBorder(null);
                panel1.add(errMsg, "cell 1 0 2 1");

                //---- usernameField ----
                usernameField.setBorder(new TitledBorder("Account"));
                usernameField.setBackground(Color.white);
                usernameField.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 16));
                usernameField.setMinimumSize(new Dimension(80, 60));
                panel1.add(usernameField, "cell 1 1 2 1,wmin 308");

                //---- passwordField ----
                passwordField.setBorder(new TitledBorder("Password"));
                passwordField.setBackground(Color.white);
                passwordField.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 16));
                passwordField.setMinimumSize(new Dimension(22, 60));
                panel1.add(passwordField, "cell 1 2 2 1,wmin 308");

                //---- loginButton ----
                loginButton.setText("\u767b\u5f55");
                loginButton.setBackground(Color.white);
                loginButton.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 15));
                loginButton.setMinimumSize(new Dimension(92, 60));
                loginButton.addActionListener(e -> loginActionPerformed(e));
                panel1.add(loginButton, "cell 1 3 2 1,align center bottom,grow 0 0,wmin 308,hmin 45");
            }
            dialog1ContentPane.add(panel1, "cell 0 0");
            dialog1.pack();
            dialog1.setLocationRelativeTo(dialog1.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JDialog dialog1;
    private JPanel panel1;
    private JLabel errMsg;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
