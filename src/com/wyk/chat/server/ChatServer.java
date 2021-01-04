package com.wyk.chat.server;

import com.wyk.chat.constants.Constants;
import com.wyk.chat.entity.ServerInfoBean;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器启动入口:然后进入ServerFrame类设计的服务器窗口
 */

public class ChatServer {
    /**
     * ChatServer---ServerHandler
     */

    // Socket---User
    static Map<String, Socket> userSocketMap = new HashMap<>();

    // 服务器的界面---ServerFrame
    public ServerFrame serverFrame;

    public ChatServer() {

        try {
            // 建立服务器的Socket监听,监听请求
            ServerSocket sso = new ServerSocket(Constants.SERVER_PORT);  // 可能出现端口占用的情况，抛出异常
            serverFrame = new ServerFrame();        // 启动服务器窗口

            // 初始化服务器参数表项
            ServerInfoBean serverInfo = getServerIP();
            loadServerInfo(serverInfo);

            // while(true)实现功能--->服务器同时可接入多个客户端
            while (true) {

                // 等待连接，阻塞实现，socket等待客户端的连接
                Socket socket = sso.accept();

                // 连接之后、便可开始处理传入的消息（开启服务端线程---一个客户一个Socket）
                ServerHandler serverHandler = new ServerHandler(socket, serverFrame);
                serverHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化加载服务器选项卡中的 ip、hostName、port
     *
     * @param serverInfo
     */
    public void loadServerInfo(ServerInfoBean serverInfo) {

        // Load ip
        serverFrame.serverInfoPanel.txtIP.setText(serverInfo.getIp());
        // Load hostName
        serverFrame.serverInfoPanel.txtServerName.setText(serverInfo.getHostName());
        // Initialize the log
        serverFrame.serverInfoPanel.txtLog.setText("服务器已启动···");
    }

    /**
     * 获取服务器的主机名和IP地址
     *
     * @param
     */
    public ServerInfoBean getServerIP() {
        ServerInfoBean sib = null;
        try {
            InetAddress serverAddress = InetAddress.getLocalHost();
            byte[] ipAddress = serverAddress.getAddress();
            sib = new ServerInfoBean();
            sib.setIp(serverAddress.getHostAddress());
            sib.setHostName(serverAddress.getHostName());
            sib.setPort(Constants.SERVER_PORT);
            // console 上输出服务端IP地址
            System.out.println("Server has started···\nThe server IP is: " + (ipAddress[0] & 0xff) +
                    "." + (ipAddress[1] & 0xff) + "." + (ipAddress[2] & 0xff) +
                    "." + (ipAddress[3] & 0xff));
        } catch (Exception e) {
            System.out.println("###Could not get Server IP." + e);
        }
        return sib;
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}
