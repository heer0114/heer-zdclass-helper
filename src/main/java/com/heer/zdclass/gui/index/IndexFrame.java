/*
 * Created by JFormDesigner on Sun Mar 10 22:59:22 CST 2019
 */

package com.heer.zdclass.gui.index;

import com.heer.zdclass.core.CoreParser;
import com.heer.zdclass.core.HeartBeatThread;
import com.heer.zdclass.gui.console.ConsoleTask;
import com.heer.zdclass.gui.console.JTextAreaOutputStream;
import com.heer.zdclass.gui.login.LoginDialog;
import com.heer.zdclass.model.BaseUserInfo;
import com.heer.zdclass.model.CoreProperties;
import com.heer.zdclass.model.UrlInfo;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author heer
 */
@SuppressWarnings("all")
@Component
public class IndexFrame extends JPanel implements InitializingBean {

    @Autowired
    private CoreProperties coreProperties;
    @Autowired
    private CoreParser coreParser;
    @Autowired
    private BaseUserInfo baseUserInfo;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    /**
     * 登录窗口
     */
    @Autowired
    private LoginDialog loginDialog;

    private static boolean START_BUTTON_FLAG = false;

    /**
     * 启动入口
     *
     * @throws Exception e
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.startup();
    }

    /**
     * 初始化并启动
     */
    public void startup() {
        // init
        try {
            // 初始化界面
            this.initUI();
            // 初始化console
            this.initConsoleTask();
            // 加载得分情况
//            this.loadingData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * init ui
     */
    private void initUI() {
        initComponents();
        this.index.getContentPane().setBackground(Color.white);
        this.consoleScrollPane.getViewport().setOpaque(false);
//        consoleScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
    }

    /**
     * 判断登录状态
     *
     * @param errTipMsg     错误提示信息
     * @param successTipMsg 登录后的提示信息
     * @return boolean
     */
    private boolean judgeLoginState() {
        // 判断用户是否登录
        if (UrlInfo.LOGOUT_URL.trim().isEmpty()) {
            this.keList.setListData(new String[]{"用户未登录！请登录！"});
            return false;
        } else {
            return true;
        }
    }

    /**
     * 加载数据
     */
    public void loadingData() {
        if (!this.judgeLoginState()) {
            System.out.println("用户未登录！");
            return;
        }
        // 启动心跳加载
        HeartBeatThread task = new HeartBeatThread(this.tipText, baseUserInfo);
        executorService.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
        // 加载得分情况
        this.keList.setListData(new String[]{"正在加载数据..."});
        this.setKeListData();
    }

    /**
     * 获取课程列表
     *
     * @return arr
     */
    public void setKeListData() {
        new SetKeListDataThread(keList, coreParser).start();
    }

    /**
     * 初始化打印任务
     */
    private void initConsoleTask() {
        ConsoleTask consoleTask = new ConsoleTask(this.consoleArea);
        Thread thread = new Thread(consoleTask);
        thread.setName("print to console.");
        thread.start();
        try (
                JTextAreaOutputStream outputStream = new JTextAreaOutputStream(consoleTask);
        ) {
            PrintStream printStream = new PrintStream(outputStream);
            System.setOut(printStream);
            System.setErr(printStream);
            System.out.println("---------------------------");
            System.out.println("控制台初始化成功！");
            System.out.println("作者: Heer_");
            System.out.println("---------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * start task
     *
     * @param e e
     */
    private void startTaskActionPerformed(ActionEvent e) {
        if (!this.judgeLoginState()) {
            System.out.println("用户未登录！");
            return;
        }
        if (IndexFrame.START_BUTTON_FLAG) {
            System.out.println("+++ 注意：任务正在运行，请勿重复点击！+++");
            return;
        }
        IndexFrame.START_BUTTON_FLAG = true;
        DemandTaskEventHandle task = new DemandTaskEventHandle(this);
        task.setName("demandTask");
        task.start();
    }

    /**
     * 登录事件
     *
     * @param e
     */
    private void LoginButtonActionPerformed(ActionEvent e) {
        if (this.judgeLoginState()) {
            System.out.println("用户已登录！");
            return;
        }
        loginDialog.startup(this);
    }

    /**
     * 关闭窗口
     *
     * @param e
     */
    private void indexWindowClosing(WindowEvent e) {
        try {
            System.out.println("正在注销登录");
            coreParser.logout();
            System.out.println("注销成功。");
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("注销失败！");
        }
    }

    /**
     * 开始任务点击事件处理线程
     */
    private class DemandTaskEventHandle extends Thread {

        private IndexFrame indexFrame;

        public DemandTaskEventHandle(IndexFrame indexFrame) {
            this.indexFrame = indexFrame;
        }

        @Override
        public void run() {
            try {
                System.out.println("Demand task starting...");
                coreParser.demandAllClass(indexFrame, UrlInfo.KE_URL_MAP);
                System.out.println("已完成全部任务！");
                IndexFrame.START_BUTTON_FLAG = false;
            } catch (Exception ex) {
                System.out.println(String.format("程序出现异常！%s\n", ex.getMessage()));
                System.out.println("正在为你重启任务...");
                try {
                    coreParser.login(coreProperties.getUsername(), coreProperties.getPwd());
                    coreParser.demandAllClass(indexFrame, UrlInfo.KE_URL_MAP);
                } catch (Exception e) {
                    System.out.println("任务重启失败！");
                }
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        index = new JFrame();
        tabbedPane5 = new JTabbedPane();
        videoPanel = new JPanel();
        keList = new JList();
        LoginButton = new JButton();
        startTask = new JButton();
        scrollPane1 = new JScrollPane();
        tipText = new JTextArea();
        consoleScrollPane = new JScrollPane();
        consoleArea = new JTextArea();

        //======== index ========
        {
            index.setVisible(true);
            index.setMinimumSize(null);
            index.setIconImage(new ImageIcon(getClass().getResource("/img/helper.png")).getImage());
            index.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            index.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 16));
            index.setBackground(Color.white);
            index.setResizable(false);
            index.setTitle("Author: Heer. Version: 2.0.6");
            index.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    indexWindowClosing(e);
                }
            });
            Container indexContentPane = index.getContentPane();
            indexContentPane.setLayout(new BoxLayout(indexContentPane, BoxLayout.X_AXIS));

            //======== tabbedPane5 ========
            {
                tabbedPane5.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 14));
                tabbedPane5.setBackground(Color.white);

                //======== videoPanel ========
                {
                    videoPanel.setBackground(Color.white);
                    videoPanel.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 16));
                    videoPanel.setLayout(new MigLayout(
                        "insets 0,hidemode 3,gap 0 0",
                        // columns
                        "[5,fill]" +
                        "[236,fill]" +
                        "[8,fill]" +
                        "[356,fill]" +
                        "[5,fill]" +
                        "[28,fill]" +
                        "[280,fill]" +
                        "[5,fill]",
                        // rows
                        "[10!]" +
                        "[38!]" +
                        "[250!]" +
                        "[336!]"));

                    //---- keList ----
                    keList.setBorder(new TitledBorder(null, "\u8bfe\u7a0b\u5f97\u5206\u60c5\u51b5", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                        new Font("\u5e7c\u5706", Font.PLAIN, 14)));
                    keList.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 14));
                    keList.setOpaque(false);
                    keList.setMinimumSize(new Dimension(50, 93));
                    videoPanel.add(keList, "cell 1 3,aligny top,growy 0,hmin 331");

                    //---- LoginButton ----
                    LoginButton.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 14));
                    LoginButton.setBackground(Color.white);
                    LoginButton.setOpaque(false);
                    LoginButton.setMinimumSize(new Dimension(350, 30));
                    LoginButton.setText("\u767b\u5f55");
                    LoginButton.addActionListener(e -> LoginButtonActionPerformed(e));
                    videoPanel.add(LoginButton, "cell 3 1,aligny bottom,growy 0");

                    //---- startTask ----
                    startTask.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 14));
                    startTask.setBackground(Color.white);
                    startTask.setOpaque(false);
                    startTask.setMinimumSize(new Dimension(350, 30));
                    startTask.setText("\u542f\u52a8\u4efb\u52a1");
                    startTask.addActionListener(e -> startTaskActionPerformed(e));
                    videoPanel.add(startTask, "cell 6 1,aligny bottom,growy 0");

                    //======== scrollPane1 ========
                    {
                        scrollPane1.setBorder(new TitledBorder(null, "\u57fa\u672c\u4fe1\u606f", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                            new Font("\u5e7c\u5706", Font.PLAIN, 14)));
                        scrollPane1.setBackground(Color.white);

                        //---- tipText ----
                        tipText.setBorder(null);
                        tipText.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 14));
                        tipText.setLineWrap(true);
                        tipText.setEditable(false);
                        scrollPane1.setViewportView(tipText);
                    }
                    videoPanel.add(scrollPane1, "cell 1 1 1 2,aligny top,growy 0,hmin 269");

                    //======== consoleScrollPane ========
                    {
                        consoleScrollPane.setBorder(new TitledBorder(null, "Console", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                            new Font("\u5e7c\u5706", Font.PLAIN, 14)));
                        consoleScrollPane.setBackground(Color.white);

                        //---- consoleArea ----
                        consoleArea.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 14));
                        consoleArea.setLineWrap(true);
                        consoleArea.setEditable(false);
                        consoleScrollPane.setViewportView(consoleArea);
                    }
                    videoPanel.add(consoleScrollPane, "pad 0,cell 3 2 4 2,aligny top,growy 0,wmax 750,height 581::581");
                }
                tabbedPane5.addTab("\u89c6\u9891\u8bfe\u7a0b", videoPanel);
            }
            indexContentPane.add(tabbedPane5);
            index.pack();
            index.setLocationRelativeTo(index.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame index;
    private JTabbedPane tabbedPane5;
    private JPanel videoPanel;
    private JList keList;
    private JButton LoginButton;
    private JButton startTask;
    private JScrollPane scrollPane1;
    private JTextArea tipText;
    private JScrollPane consoleScrollPane;
    private JTextArea consoleArea;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public JList getKeList() {
        return keList;
    }
}
