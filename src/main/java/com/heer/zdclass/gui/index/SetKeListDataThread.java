package com.heer.zdclass.gui.index;

import com.heer.zdclass.core.CoreParser;
import com.heer.zdclass.model.ClassInfo;
import com.heer.zdclass.model.UrlInfo;

import javax.swing.*;
import java.util.List;

/**
 * @author heer_
 * <p>
 *
 * </p>
 */
public class SetKeListDataThread extends Thread {

    /**
     * 课程列表
     */
    private JList keList;

    /**
     * 解析类
     */
    private CoreParser coreParser;

    public SetKeListDataThread(JList keList, CoreParser coreParser) {
        this.keList = keList;
        this.coreParser = coreParser;
    }

    @Override
    public void run() {
        // 读取课程信息
        List<ClassInfo> classInfoList = coreParser.getKeInfoList(UrlInfo.KE_URL_MAP);
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
