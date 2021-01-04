package com.wyk.chat.server;

import com.wyk.chat.entity.ChatStatus;
import com.wyk.chat.entity.TransferInfo;
import com.wyk.chat.io.IOStream;
import com.wyk.chat.ui.OnlineUserPanel;
import com.wyk.chat.ui.ServerInfoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 服务器端主界面:网络聊天室服务器+UserName
 *
 */

public class ServerFrame extends JFrame {

    public static final Integer FRAME_WIDTH = 600;    /* 登录窗体宽度 */
    public static final Integer FRAME_HEIGHT = 512;   /* 登录窗体高度 */

    // 服务器参数页面(第一个选项卡)
    public ServerInfoPanel serverInfoPanel;

    // 用户列表界面(第二个选项卡)
    public OnlineUserPanel onlineUserPanel;

    public ServerFrame() {

        this.setTitle("weTalk 【V1.0】【Developed By Wyk、Pxj、Wcy、Lpg】");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        //窗体不可扩大
        setResizable(false);

        //获取屏幕
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        //屏幕居中处理
        setLocation((width - FRAME_WIDTH) / 2, (height - FRAME_HEIGHT) / 2);

        //设置窗体关闭，程序退出
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //选项卡样式
        JTabbedPane tpServer = new JTabbedPane(JTabbedPane.TOP);
        tpServer.setBackground(Color.WHITE);
        tpServer.setFont(new Font("三极瑚琏简体", 0, 18));
        // 调用CustomTabbedPaneUI来设计窗体的样式,可通过注释与否来改变
//        tpServer.setUI(new CustomTabbedPaneUI());

        // 服务器界面的两个选项卡
        /* 第一个选项卡 */
        serverInfoPanel = new ServerInfoPanel();
        tpServer.add("服务器信息", serverInfoPanel.getServerInfoPanel());
        /* 第二个选项卡 */
        onlineUserPanel = new OnlineUserPanel();
        tpServer.add("在线用户列表", onlineUserPanel.getUserPanel());

        add(tpServer);
        setVisible(true);
    }

    // 通过 ChatServer 来启动该窗口
//    public static void main(String[] args) {
//        new ServerFrame();
//    }
}
