package com.wyk.chat.ui;

import com.wyk.chat.constants.Constants;
import com.wyk.chat.server.ServerFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 第一个选项卡的面板《服务器信息》
 */
public class ServerInfoPanel {

    // 服务器名称
    public JTextField txtServerName;
    // 服务器ip
    public JTextField txtIP;
    // 服务器在线人数
    public JTextField txtNumber;
    // 日志区域
    public static JTextPane txtLog;

    public JLabel getServerInfoPanel() {

        //整个第一个服务选项卡面板《服务器信息》，包括日志区域
        JPanel pnlServer = new JPanel();
        pnlServer.setOpaque(false);
        pnlServer.setLayout(null);
        pnlServer.setBounds(0, 0, ServerFrame.FRAME_WIDTH, ServerFrame.FRAME_HEIGHT);

        //日志标签
        JLabel lblLog = new JLabel("[服务器日志]");
        lblLog.setForeground(Color.BLACK);
        lblLog.setFont(new Font("三极瑚琏简体", 0, 16));
        lblLog.setBounds(155, 5, 100, 30);
        pnlServer.add(lblLog);

        //日志区域
        txtLog = new JTextPane();
        txtLog.setOpaque(false);
        txtLog.setFont(new Font("三极瑚琏简体", 0, 12));
        txtLog.setEditable(false);       // 设置日志区域只读、防止恶意纂改

        JScrollPane scoPaneOne = new JScrollPane(txtLog);       // 设置滚动条
        scoPaneOne.setBounds(155, 35, 415, 360);
        scoPaneOne.setOpaque(false);
        scoPaneOne.getViewport().setOpaque(false);
        pnlServer.add(scoPaneOne);

        pnlServer.add(stopBtn());

        pnlServer.add(saveLogBtn());

        pnlServer.add(getServerParam());    // 囊括当前在线人数、服务器名、服务器IP、服务器端口

        //加载窗体的背景图片
        ImageIcon imageIcon = new ImageIcon("src\\image\\bk.jpg");
        //创建一个标签并将图片添加进去
        JLabel lblBackground = new JLabel(imageIcon);
        //设置图片的位置和大小
        lblBackground.setBounds(0, 200, 300, 300);
        //添加到当前窗体中
        lblBackground.add(pnlServer);

        return lblBackground;
    }

    /**
     * 服务器参数信息界面
     *
     * @return
     */
    // 《服务器信息》选项卡下的左侧区域
    public JPanel getServerParam() {
        //服务器参数信息面板，不包括日志区域
        JPanel serverParamPanel = new JPanel();
        serverParamPanel.setOpaque(false);
        serverParamPanel.setBounds(10, 35, 135, 360);
        serverParamPanel.setFont(new Font("三极瑚琏简体", 0, 14));
        serverParamPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        JLabel lblNumber = new JLabel("当前在线人数:");
        lblNumber.setFont(new Font("三极瑚琏简体", 0, 12));
        serverParamPanel.add(lblNumber);

        txtNumber = new JTextField("0 人", 13);      // 默认为0人
        txtNumber.setFont(new Font("三极瑚琏简体", 0, 12));
        txtNumber.setEditable(false);
        serverParamPanel.add(txtNumber);

        JLabel lblServerName = new JLabel("服务器名称:");
        lblServerName.setFont(new Font("三极瑚琏简体", 0, 12));
        serverParamPanel.add(lblServerName);

        txtServerName = new JTextField(13);
        txtServerName.setFont(new Font("三极瑚琏简体", 0, 12));
        txtServerName.setEditable(false);
        serverParamPanel.add(txtServerName);

        JLabel lblIP = new JLabel("服务器IP:");
        lblIP.setFont(new Font("三极瑚琏简体", 0, 12));
        serverParamPanel.add(lblIP);

        txtIP = new JTextField(13);
        txtIP.setFont(new Font("三极瑚琏简体", 0, 12));
        txtIP.setEditable(false);
        serverParamPanel.add(txtIP);

        JLabel lblPort = new JLabel("服务器端口:");
        lblPort.setFont(new Font("三极瑚琏简体", 0, 12));
        serverParamPanel.add(lblPort);

        JTextField txtPort = new JTextField(Constants.SERVER_PORT + "", 13);
        txtPort.setFont(new Font("三极瑚琏简体", 0, 12));
        txtPort.setEditable(false);
        serverParamPanel.add(txtPort);

        return serverParamPanel;
    }

    /**
     * 关闭服务器按钮
     *
     * @return
     */
    public JButton stopBtn() {
        JButton stopBtn = new JButton("关闭服务器");
        stopBtn.setBackground(Color.WHITE);
        stopBtn.setFont(new Font("三极瑚琏简体", 0, 14));
        stopBtn.setBounds(200, 400, 110, 30);
        stopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 关闭当前服务器界面
                JOptionPane.showMessageDialog(null, "Good Bye!");
                System.exit(0);     // 待解决的Bug（服务端退出后，客户端会报错！）
            }
        });
        return stopBtn;
    }

    /**
     * 保存日志按钮
     *
     * @return
     */
    public JButton saveLogBtn() {
        JButton saveLogBtn = new JButton("保存日志");
        saveLogBtn.setBackground(Color.WHITE);
        saveLogBtn.setFont(new Font("三极瑚琏简体", 0, 14));
        saveLogBtn.setBounds(320, 400, 110, 30);
        saveLogBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "The log file has been saved!");
            }
        });
        return saveLogBtn;
    }
}
