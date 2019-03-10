package com.heer.zdclass.core;


import com.heer.zdclass.execute.Execute;
import com.heer.zdclass.model.ClassInfo;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @author: heer_
 * @date: 19/03/06 15:24
 * @description:
 */
public class DemandClassThread implements Runnable {
    private ClassInfo classInfo;

    private Semaphore semaphore;

    private CountDownLatch countDownLatch;

    DemandClassThread(ClassInfo classInfo, Semaphore semaphore, CountDownLatch countDownLatch) {
        this.classInfo = classInfo;
        this.semaphore = semaphore;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            // 拿到任务执行权
            semaphore.acquire();
            System.out.println("开始点播【"+Thread.currentThread().getName()+"】......");
            if(semaphore.hasQueuedThreads()){
                // 是否有等待的课程
                System.out.println("**************************************************");
                System.out.println("还有["+semaphore.getQueueLength()+"]门课程等待！");
                System.out.println("**************************************************");
            }
            Map<String, String> notSeenUrlMap = classInfo.getClassVideoUrlMap();
            Set<Map.Entry<String, String>> notseen = notSeenUrlMap.entrySet();
            for (Map.Entry<String, String> map : notseen) {
                String k = map.getKey();
                String v = map.getValue();
                // 点播
                Execute.getDocumentByMethod(v, "GET", null);
                if(isCreditFull()){
                    // 中断线程
                    Thread.currentThread().interrupt();
                    System.out.println("课程【" + classInfo.getClassName() + "】学分已得满分。");
                    // 释放任务的执行权
                    semaphore.release();
                    countDownLatch.countDown();
                    break;
                }
                System.out.println("点播了课程视频：【" + classInfo.getClassName() + "】" + k);
                System.out.println("3.30分钟后,继续点播视频。请稍等...");
                // 点播后休眠3.30分钟
                Thread.sleep(1000 * 60 * 3 + 1000 * 30);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断课程的学分是否已满
     * @return boolean
     */
    private boolean isCreditFull() throws IOException {
        ParseAssistanter assistanter = new ParseAssistanter();
        String creditStr = assistanter.getCredit(classInfo.getClassUrl());
        String[] strArray = creditStr.split("，");
        String alreay = strArray[0].replaceAll("[^\\d+]", "");
        String full = strArray[1].replaceAll("[^\\d+]", "");
        return alreay.equals(full);
    }
}
