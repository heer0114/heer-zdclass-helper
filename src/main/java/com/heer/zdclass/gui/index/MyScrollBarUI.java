package com.heer.zdclass.gui.index;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * 自定义滚动条
 * @author Silly
 *
 */
public class MyScrollBarUI extends BasicScrollBarUI  {

    @Override
    protected void configureScrollBarColors() {
        // 滑道
        trackColor = Color.WHITE;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        super.paintTrack(g, c, trackBounds);
    }

    //把手
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        // 把绘制区的x，y点坐标定义为坐标系的原点，这句一定一定要加上，不然拖动就失效了
        g.translate(thumbBounds.x, thumbBounds.y);
        // 设置把手颜色
        g.setColor(new Color(200,200,200));
        // 画一个圆角矩形，前4个参数为坐标和宽高，后面两个参数用于控制角落的圆角弧度
        g.drawRoundRect(5, 0, 6, thumbBounds.height-1, 5, 5);
        Graphics2D g2 = (Graphics2D) g;
        //消除锯齿------------没有任何意义注掉
//        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.addRenderingHints(rh);
        // 半透明
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        // 设置填充颜色，这里设置了渐变，由下往上 ------------不好看注掉
//        g2.setPaint(new GradientPaint(c.getWidth() / 2, 1, Color.GRAY, c.getWidth() / 2, c.getHeight(), Color.GRAY));
        // 填充圆角矩形
        g2.fillRoundRect(5, 0, 6, thumbBounds.height-1, 5, 5);
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton button = new JButton(produceImage("down.png"));
        button.setBorder(null);
        return button;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton button = new JButton(produceImage("up.png"));
        button.setBorder(null);
        return button;
    }

    /**
     * 获取图片
     * @param name 图片名称
     * @return
     */
    private ImageIcon produceImage(String name) {
        ImageIcon backImage = new ImageIcon("img\\scroll\\"+name);
        return backImage;
    }

}
