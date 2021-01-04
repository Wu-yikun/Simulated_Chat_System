package com.wyk.chat.ulist;

import com.wyk.chat.entity.User;

import javax.swing.*;
import java.awt.*;

/**
 * @Description: DefaultListCellRenderer是JList的渲染器、继承了JLabel、所以该类当成一个JLabel
 * @Author: 57715
 * @Date: 2020/12/20 20:10
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class ImageCellRenderer extends DefaultListCellRenderer {

    /**
     * @param list         JList对象
     * @param value        模型数据
     * @param index        当前选择的单元格下标
     * @param isSelected   单元格选中的状态、假设你选中A后再选中A，返回false
     * @param cellHasFocus
     * @return
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if (value instanceof User) {
            User user = (User) value;
            // 头像img路径
            String iconPath = user.getUiconPath();
            // 用户名
            String userName = user.getUserName();
            // 获取个性签名---目前设置为固定的签名
            String motto = user.getMotto();
            ImageIcon icon = new ImageIcon(iconPath);
            icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            setIcon(icon);
            // 在客户端的用户列表内设置 (姓名 + 个性签名) 的样式
            String text = "<html><body><span color='gray' style='font-size:15px;'>" + userName + "</span><br>" + motto + "</body></html>";
            setText(text);  // 支持HTML语句
            setForeground(Color.BLUE);
            setVerticalTextPosition(SwingConstants.TOP);
            setHorizontalTextPosition(SwingConstants.RIGHT);
        }
        return this;
    }
}
