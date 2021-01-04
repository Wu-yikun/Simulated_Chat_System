package com.wyk.chat.client;

import com.wyk.chat.constants.Constants;
import com.wyk.chat.entity.ChatStatus;
import com.wyk.chat.entity.TransferInfo;
import com.wyk.chat.io.IOStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

/**
 * 基于 Java 实现的聊天窗口
 * Group: 彭鑫杰、吴仪坤、王灿阳、李品庚
 *
 * 登陆界面
 */

public class LoginFrame extends JFrame {

    private static final Integer FRAME_WIDTH = 400;    /* 登录窗体宽度 */
    private static final Integer FRAME_HEIGHT = 260;   /* 登录窗体高度 */

    public LoginFrame() {
        this.setTitle("登录窗口");                 // 窗口名
        setSize(FRAME_WIDTH, FRAME_HEIGHT);      // 设置窗体大小
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 窗口退出即程序运行结束
        setResizable(false);                     // 禁用窗口最大最小化

        /* 获取屏幕对象的高度和宽度 */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setLocation((width - FRAME_WIDTH) / 2, (height - FRAME_HEIGHT) / 2);     // 设置窗口居中显示

        //加载窗体的背景图片
        ImageIcon imageIcon = new ImageIcon("src/image/fight.jpg");

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

        lblPsw.setBounds(80, 100, 120, 30);

        lblPsw.setFont(new Font("三极榜书", 0, 16));
        //设置字体颜色为白色
        lblPsw.setForeground(Color.WHITE);
        //添加到背景图片上
        lblBackground.add(lblPsw);

        //创建一个密码框，用于输入用户密码
        JPasswordField textPsw = new JPasswordField();
        //设置密码框的位置、大小

        textPsw.setBounds(150, 80, 160, 30);

        textPsw.setBounds(150, 100, 160, 30);

        lblBackground.add(textPsw);

        //创建文字按钮--登录
        JButton enter = new JButton("登 录");
        //设置位置、大小
        enter.setBounds(110, 170, 80, 25);
        enter.addActionListener(new ActionListener() {

            /**
             * LoginFrame---ClientHandler 
             */
            // 登录按钮触发事件
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取输入文本框中的用户信息---并将其传给客户端进行验证(ConnectionServer方法中实现)
                String userName = textUid.getText();
                String password = textPsw.getText();
                if("".equals(userName)||"".equals(password)){
                    JOptionPane.showMessageDialog(null, "请输入账号和密码~");
                }else{
                TransferInfo tfi = new TransferInfo();
                    tfi.setUserName(userName);
                    tfi.setPassword(password);

                    // 消息分类设置为:登录消息
                    tfi.setStatusEnum(ChatStatus.LOGIN);
                    connectionServer(tfi);
                }
            }
        });
        lblBackground.add(enter);

        // 创建文字按钮--注册
        JButton register = new JButton("注 册");
        // 设置布局为空布局
        register.setBounds(215, 170, 80, 25);
        // 注册触发事件
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame();
            }
        });
        //添加到背景图片上
        lblBackground.add(register);

        this.setLayout(null);       // 设置布局为null
        setVisible(true);
    }

    /**
     * 连接服务器的入口---传入TransferInfo对象===用户的登录账号和密码
     * 
     * @param tfi
     */
    public void connectionServer(TransferInfo tfi) {
        try {
            // 服务器_IP + 服务器_端口：连接对应的服务器(响应accept)
            Socket socket = new Socket(Constants.SERVER_IP, Constants.SERVER_PORT);
            IOStream.writerMessage(socket, tfi);    // 将用户的基本登录信息发送给客户端进行验证

            // 开启客户端线程---并且传入LoginFrame窗口对象
            ClientHandler clientHandler = new ClientHandler(socket,this);
            clientHandler.start();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}