package com.wyk.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * @Description:
 * @Author: 57715
 * @Date: 2021/1/4 11:19
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class RegisterFrame extends JFrame {

    private static final Integer FRAME_WIDTH = 420;    /* 登录窗体宽度 */
    private static final Integer FRAME_HEIGHT = 265;   /* 登录窗体高度 */

    public RegisterFrame() {
        this.setTitle("注册窗口");                 // 窗口名
        setSize(FRAME_WIDTH, FRAME_HEIGHT);      // 设置窗体大小
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);                     // 禁用窗口最大最小化

        /* 获取屏幕对象的高度和宽度 */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setLocation((width - FRAME_WIDTH) / 2, (height - FRAME_HEIGHT) / 2);

        //加载窗体的背景图片
        ImageIcon imageIcon = new ImageIcon("src/image/w.jpg");
        //创建标签、并将图片添加进去
        JLabel lblBackground = new JLabel(imageIcon);
        //设置图片的位置和大小
        lblBackground.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        //设置布局为空布局
        lblBackground.setLayout(null);
        //添加到当前窗体中
        this.add(lblBackground);

        //创建"账号"标签
        JLabel lblUid = new JLabel("账 号: ");
        //设置位置、大小
        lblUid.setBounds(80, 40, 120, 30);
        lblUid.setFont(new Font("三极榜书", 0, 16));
        //设置标签文本的颜色为白色
        lblUid.setForeground(Color.WHITE);
        //将标签添加到背景图片上
        lblBackground.add(lblUid);

        //账号文本框
        JTextField textUid = new JTextField();
        //设置文本框的位置、大小
        textUid.setBounds(150, 40, 160, 30);
        lblBackground.add(textUid);

        //创建"密码"标签
        JLabel lblPsw = new JLabel("密 码: ");
        //设置标签的位置、大小
        lblPsw.setBounds(80, 80, 120, 30);
        lblPsw.setFont(new Font("三极榜书", 0, 16));
        //设置字体颜色为白色
        lblPsw.setForeground(Color.WHITE);
        //添加到背景图片上
        lblBackground.add(lblPsw);

        //创建一个密码框，用于输入用户密码
        JPasswordField textPsw = new JPasswordField();
        //设置密码框的位置、大小
        textPsw.setBounds(150, 80, 160, 30);
        lblBackground.add(textPsw);

        //创建"确认密码"标签
        JLabel checkPsw = new JLabel("确 认 密 码: ");
        //设置标签的位置、大小
        checkPsw.setBounds(60, 120, 120, 30);
        checkPsw.setFont(new Font("三极榜书", 0, 16));
        //设置字体颜色为白色
        checkPsw.setForeground(Color.WHITE);
        //添加到背景图片上
        lblBackground.add(checkPsw);

        //创建一个确认密码框，用于输入用户密码
        JPasswordField textCheckPsw = new JPasswordField();
        //设置密码框的位置、大小
        textCheckPsw.setBounds(150, 120, 160, 30);
        lblBackground.add(textCheckPsw);

        JButton register = new JButton("注 册");
        // 设置布局为空布局
        register.setBounds(165, 170, 80, 25);
        // 注册触发事件
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File dest = new File("D:\\吴仪坤\\IDEA-Code\\WinSocket\\src\\com\\wyk\\chat\\registeredUser.txt");
                OutputStream os = null;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(dest, true));
                    String userName = textUid.getText();
                    String password = textPsw.getText();
                    String checkPassword = textCheckPsw.getText();
                    if (password.equals(checkPassword)) {
                        String msg = "\n"+userName + "|" + password;
                        byte[] dates = msg.getBytes();
                        os.write(dates, 0, dates.length);
                        os.flush();
                        JOptionPane.showMessageDialog(null, "注册成功!");
                        disposeFrame();
                    } else {
                        JOptionPane.showMessageDialog(null, "两次输入的密码不一致!");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        if (null != os) {
                            os.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        //添加到背景图片上
        lblBackground.add(register);

        this.setLayout(null);
        setVisible(true);
    }

    // 注册窗口消失
    public void disposeFrame() {
        this.dispose();
    }
}
