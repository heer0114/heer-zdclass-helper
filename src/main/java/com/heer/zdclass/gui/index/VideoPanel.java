package com.heer.zdclass.gui.index;

import javax.swing.*;
import java.awt.*;

/**
 * @author: heer_
 * @date: 19/03/14 09:33
 * @description: 视频页 panel
 */
@SuppressWarnings("all")
public class VideoPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("img/videoPanelBag.jpg"));
        g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), img.getImageObserver());
    }
}
