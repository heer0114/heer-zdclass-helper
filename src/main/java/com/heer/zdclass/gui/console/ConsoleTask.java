package com.heer.zdclass.gui.console;

import javax.swing.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author: heer_
 * @date: 19/03/13 08:35
 * @description: 输出到JTextArea【console】
 */
@SuppressWarnings(value = "all")
public class ConsoleTask implements Runnable {
    private JTextArea txa;
    // 数据队列
    ArrayBlockingQueue<String> dataQueue = new ArrayBlockingQueue<>(5000);

    public ConsoleTask(final JTextArea txa) {
        this.txa = txa;
    }

    public void append(final String ctn) {
        synchronized (dataQueue) {
            dataQueue.offer(ctn);
            dataQueue.notifyAll();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (dataQueue.size() == 0) {
                    synchronized (dataQueue) {
                        dataQueue.wait();
                    }
                } else {
                    synchronized (dataQueue) {
                        this.txa.append(dataQueue.poll());
                        // 实时显示
//                        this.txa.paintImmediately(txa.getBounds());
                    }
                    this.txa.setCaretPosition(this.txa.getDocument().getLength());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

