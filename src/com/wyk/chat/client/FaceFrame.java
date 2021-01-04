package com.wyk.chat.client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @Description: 表情框
 * @Author: 57715
 * @Date: 2020/12/19 20:45
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class FaceFrame extends JFrame {

    public FaceFrame(JTextPane textPane) {

        // 放置图片的容器
        JPanel panel = (JPanel)getContentPane();
        panel.setLayout(null);
        // 用双重循环来摆放图片
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 6; col++) {
                // 循环获取每一张图片
                ImageIcon icon = new ImageIcon("src/image/face/" + (6 * row + col + 1) + ".gif");
                // 将图片放在JLabel里
                JLabel lblIcon = new JLabel(icon);
                lblIcon.setSize(50, 50);   // 设置表情大小
                lblIcon.setLocation(0 + col * 50, 0 + row * 50);    // 每一张图根据索引摆放位置

                // 为标签(镶着Icon)添加鼠标点击事件:点击后放置到容器里
                lblIcon.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel jLabel = (JLabel)e.getSource();
                        Icon icon2 = jLabel.getIcon();
                        // 在输入框中插入选中图片
                        textPane.insertIcon(icon2);
                        // 选择一个表情后即可关闭当前图片框
                        FaceFrame.this.dispose();
                    }

                });
                panel.add(lblIcon);
            }
        }
        setSize(320, 300);
        // 设置弹出表情框的位置
        setLocation(800, 400);
        setTitle("嘻哈猴");
        setVisible(true);
    }
}
