/*
 * Created by JFormDesigner on Tue Mar 12 16:29:40 CST 2019
 */

package com.heer.zdclass.gui.login;

import com.heer.zdclass.core.CoreParser;
import com.heer.zdclass.gui.index.IndexFrame;
import com.heer.zdclass.model.UserProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * @author heer
 */
public class LoginDialog extends JPanel {

    private UserProperties userProperties;
    private CoreParser coreParser;
    public static IndexFrame indexFrame = null;

    public LoginDialog() {
        initComponents();
    }

    public LoginDialog(UserProperties userProperties, CoreParser coreParser) {
        this.userProperties = userProperties;
        this.coreParser = coreParser;
        initComponents();
        this.passwordField.setEchoChar('*');
    }

    /**
     * login event
     *
     * @param e e
     */
    private void loginActionPerformed(ActionEvent e) {
        String username = this.usernameField.getText();
        String password = String.valueOf(this.passwordField.getPassword());
        this.userProperties.setUsername(username);
        this.userProperties.setPwd(password);

        // 登录并初始化信息
        Map<String, String> keUrlMap = null;
        try {
            keUrlMap = coreParser.login(userProperties.getUsername(), userProperties.getPwd());
            // close login dialog
            this.dialog1.dispose();

            // open index frame
            indexFrame = new IndexFrame(keUrlMap, coreParser, userProperties);

        } catch (Exception ex) {
            ex.printStackTrace();
            this.errMsg.setText("登录出现异常！请确保账户/密码正确！");
            this.errMsg.setVisible(true);
            return;
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialog1 = new JDialog();
        errMsg = new JLabel();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton();

        //======== dialog1 ========
        {
            dialog1.setResizable(false);
            dialog1.setVisible(true);
            dialog1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog1.setTitle("LoginDialog");
            dialog1.setMinimumSize(new Dimension(300, 300));
            dialog1.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 16));
            Container dialog1ContentPane = dialog1.getContentPane();
            dialog1ContentPane.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[49,fill]" +
                "[335,fill]" +
                "[47,fill]",
                // rows
                "[28]" +
                "[61]" +
                "[54]" +
                "[70]" +
                "[]"));

            //---- errMsg ----
            errMsg.setVisible(false);
            errMsg.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 15));
            errMsg.setForeground(Color.red);
            dialog1ContentPane.add(errMsg, "cell 1 0");

            //---- usernameField ----
            usernameField.setBorder(new TitledBorder("Account"));
            usernameField.setBackground(new Color(242, 242, 242));
            usernameField.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 16));
            usernameField.setMinimumSize(new Dimension(80, 60));
            dialog1ContentPane.add(usernameField, "cell 1 1,wmin 280,hmin 50");

            //---- passwordField ----
            passwordField.setBorder(new TitledBorder("Password"));
            passwordField.setBackground(new Color(242, 242, 242));
            passwordField.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 16));
            passwordField.setMinimumSize(new Dimension(22, 60));
            dialog1ContentPane.add(passwordField, "cell 1 2,hmin 50");

            //---- loginButton ----
            loginButton.setText("\u767b\u5f55");
            loginButton.setBackground(new Color(242, 242, 242));
            loginButton.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 15));
            loginButton.setMinimumSize(new Dimension(92, 60));
            loginButton.addActionListener(e -> loginActionPerformed(e));
            dialog1ContentPane.add(loginButton, "cell 1 3,hmin 40");
            dialog1.pack();
            dialog1.setLocationRelativeTo(dialog1.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JDialog dialog1;
    private JLabel errMsg;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
