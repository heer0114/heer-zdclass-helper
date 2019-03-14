package com.heer.zdclass.gui.console;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author: heer_
 * @date: 19/03/13 08:27
 * @description: 文本域输出流
 */
@SuppressWarnings("all")
public class JTextAreaOutputStream extends OutputStream {

    private ConsoleTask consoleTask;

    public JTextAreaOutputStream(final ConsoleTask consoleTask) {
        this.consoleTask = consoleTask;
    }

    @Override
    public void write(int b) throws IOException {
        consoleTask.append(String.valueOf((char) b));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        consoleTask.append(new String(b, off, len));
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }
}
