/*
 * Created by JFormDesigner on Sun Mar 10 22:59:22 CST 2019
 */

package com.heer.zdclass.gui.index;

import com.heer.zdclass.core.CoreParser;
import com.heer.zdclass.gui.console.ConsoleTask;
import com.heer.zdclass.gui.console.JTextAreaOutputStream;
import com.heer.zdclass.model.ClassInfo;
import com.heer.zdclass.model.UserProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/**
 * @author heer
 */
@SuppressWarnings("all")
public class IndexFrame extends JPanel {

    private CoreParser coreParser;
    private Map<String, String> keUrlMap;
    private UserProperties userProperties;
    private static boolean START_BUTTON_FLAG = true;

    public IndexFrame(Map<String, String> keUrlMap, CoreParser coreParser, UserProperties userProperties) {
        // init
        try {
            this.coreParser = coreParser;
            this.keUrlMap = keUrlMap;
            this.userProperties = userProperties;
            // 初始化界面
            this.initUI();
            // 初始化console
            this.initConsoleTask();
            // 加载数据
            this.loadingData();
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
        consoleScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
    }

    /**
     * 加载数据
     */
    private void loadingData() {
        this.keList.setListData(new String[]{"正在加载数据..."});
        this.setKeListData();
    }

    /**
     * 获取课程列表
     *
     * @return arr
     */
    public void setKeListData() {
        new SetKeListDataThread(keList).start();
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
            System.out.println("The console was successfully initialized! welcome to use.");
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
        String hint = !IndexFrame.START_BUTTON_FLAG ? "+++ 注意：任务正在运行，请勿重复点击！+++" : null;
        IndexFrame.START_BUTTON_FLAG = false;
        if (hint != null) {
            System.out.println(hint);
            return;
        }
        DemandTaskEventHandle task = new DemandTaskEventHandle();
        task.setName("demandTask");
        task.start();
    }

    /**
     * clear console
     *
     * @param e
     */
    private void clearConsoleActionPerformed(ActionEvent e) {
        this.consoleArea.setText("");
    }

    /**
     * 开始任务点击事件处理线程
     */
    private class DemandTaskEventHandle extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Demand task starting...");
                coreParser.demandAllClass(keUrlMap);
                System.out.println("已完成全部任务！");
                IndexFrame.START_BUTTON_FLAG = true;
            } catch (InterruptedException ex) {
                System.out.println(String.format("程序出现异常！%s\n", ex.getMessage()));
                System.out.println("正在为你重启任务...");
                try {
                    keUrlMap = coreParser.login(userProperties.getUsername(), userProperties.getPwd());
                    coreParser.demandAllClass(keUrlMap);
                } catch (Exception e) {
                    System.out.println("任务重启失败！");
                }
            }
        }
    }


    /**
     * set ke list data
     */
    private class SetKeListDataThread extends Thread {

        public JList keList;

        public SetKeListDataThread(JList keList) {
            this.keList = keList;
        }

        @Override
        public void run() {
            // 读取课程信息
            List<ClassInfo> classInfoList = coreParser.getKeInfoList(keUrlMap);
            StringBuilder strTemp = new StringBuilder();
            for (int i = 0; i < classInfoList.size(); i++) {
                ClassInfo classInfo = classInfoList.get(i);
                String keName = classInfo.getClassName();
                strTemp.append(keName + " ");
                String credit = classInfo.getCredit();
                strTemp.append(credit);
                strTemp.append(",");
            }
            synchronized (this.keList) {
                this.keList.setListData(strTemp.toString().split(","));
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        index = new JFrame();
        tabbedPane5 = new JTabbedPane();
        videoPanel = new JPanel();
        keList = new JList();
        startTask = new JButton();
        clearConsole = new JButton();
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
                                    "[237,fill]" +
                                    "[5,fill]" +
                                    "[280,fill]" +
                                    "[5,fill]" +
                                    "[192,fill]" +
                                    "[280,fill]" +
                                    "[5,fill]",
                            // rows
                            "[10]" +
                                    "[26,fill]" +
                                    "[557]" +
                                    "[5]"));

                    //---- keList ----
                    keList.setBorder(new TitledBorder(null, "\u8bfe\u7a0b\u5217\u8868", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                            new Font("\u5e7c\u5706", Font.PLAIN, 14)));
                    keList.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 14));
                    keList.setOpaque(false);
                    videoPanel.add(keList, "cell 1 0 1 3,hmin 600");

                    //---- startTask ----
                    startTask.setText("\u5f00\u59cb\u4efb\u52a1");
                    startTask.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 14));
                    startTask.setBackground(Color.white);
                    startTask.setOpaque(false);
                    startTask.addActionListener(e -> startTaskActionPerformed(e));
                    videoPanel.add(startTask, "cell 3 1");

                    //---- clearConsole ----
                    clearConsole.setText("\u6e05\u7a7aConsole");
                    clearConsole.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 14));
                    clearConsole.setBackground(Color.white);
                    clearConsole.setOpaque(false);
                    clearConsole.addActionListener(e -> clearConsoleActionPerformed(e));
                    videoPanel.add(clearConsole, "cell 6 1");

                    //======== consoleScrollPane ========
                    {
                        consoleScrollPane.setBorder(new TitledBorder(null, "Console", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                                new Font("\u5e7c\u5706", Font.PLAIN, 14)));
                        consoleScrollPane.setBackground(Color.white);
                        consoleScrollPane.setOpaque(false);

                        //---- consoleArea ----
                        consoleArea.setBorder(null);
                        consoleArea.setFont(new Font("\u5e7c\u5706", Font.PLAIN, 15));
                        consoleArea.setCaretColor(Color.green);
                        consoleArea.setForeground(new Color(51, 153, 255));
                        consoleArea.setMinimumSize(null);
                        consoleArea.setOpaque(false);
                        consoleArea.setEditable(false);
                        consoleArea.setLineWrap(true);
                        consoleArea.setWrapStyleWord(true);
                        consoleScrollPane.setViewportView(consoleArea);
                    }
                    videoPanel.add(consoleScrollPane, "pad 0,cell 3 2 4 1,hmin 560");
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
    private JButton startTask;
    private JButton clearConsole;
    private JScrollPane consoleScrollPane;
    private JTextArea consoleArea;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
