package com.wyk.chat.server;

import com.wyk.chat.entity.ChatStatus;
import com.wyk.chat.entity.FontStyle;
import com.wyk.chat.io.IOStream;
import com.wyk.chat.entity.TransferInfo;
import com.wyk.chat.util.FontSupport;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * @Description: 服务器开辟线程不断处理读取来的消息、同时检查客户端传来的登录消息是否匹配后台数据库(这里直接使用文件.txt模拟)
 * @Author: 57715
 * @Date: 2020/12/22 15:35
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class ServerHandler extends Thread {

    // 服务端连接传来的套接字
    public Socket socket;
    // 服务器的界面
    public static ServerFrame serverFrame;

    public ServerHandler(Socket socket, ServerFrame serverFrame) {
        this.socket = socket;
        this.serverFrame = serverFrame;
    }

    // static -> 公共列表!(才能刷新到每个客户端)
    static List<String> onlineUsers = new ArrayList<>();    // 在线用户列表
    static List<Socket> onlineSockets = new ArrayList<>();  // 用于群发消息

    @Override
    public void run() {
        while (true) {
            try {
                // 模拟不断获取消息，产生阻塞
                Object obj = IOStream.readMessage(socket);  // ---使用IOStream工具类实现代码复用
                // 匹配信息
                if (obj instanceof TransferInfo) {
                    TransferInfo tfi = (TransferInfo) obj;
                    if (tfi.getStatusEnum() == ChatStatus.LOGIN) {
                        // 登录消息---loginHandler(...)
                        loginHandler(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.CHAT) {
                        // 聊天消息---chatHandler(...)
                        chatHandler(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.DD) {
                        // 抖动消息---ShakeHandle()
                        ShakeHandle(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.QUIT) {
                        // 退出处理
                        loginOut(tfi);

                        // 休眠1秒后,关闭当前Socket
                        Thread.sleep(1000);
                        socket.close();
                        // 关闭当前线程
                        this.interrupt();
                        // 跳出循环
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用户退出处理,清理在线人数,刷新用户列表,通知所有人你已离开
     *
     * @param tfi
     */
    private void loginOut(TransferInfo tfi) {
        // 先获取该退出用户的用户名
        String userName = tfi.getUserName();
        // 然后将该用户从用户列表中删除---onlineUsers
        Iterator<String> userIter = onlineUsers.iterator();
        while (userIter.hasNext()) {
            if (userIter.next().equals(userName)) {   // 匹配成功
                userIter.remove();
            }
        }

        // 将该用户从socket集合移除---onlineSockets
        Iterator<Socket> socketIter = onlineSockets.iterator();
        while (socketIter.hasNext()) {
            Socket next = socketIter.next();
            if (socket == next) {
                socketIter.remove();
            }
        }

        // 将user与socket的关系从Map中移除
        ChatServer.userSocketMap.remove(userName);

        // 刷新服务器面板的用户列表
        flushOnlineUserList();

        // 给所有在线的用户发送下线消息
        tfi.setStatusEnum(ChatStatus.NOTICE);
        sendAll(tfi);

        // 告诉其他用户刷新用户列表
        tfi.setUserOnlineArray(onlineUsers.toArray(new String[onlineUsers.size()]));
        tfi.setStatusEnum(ChatStatus.ULIST);
        sendAll(tfi);

    }

    /**
     * 发送抖动消息到客户端
     * <p>
     * 根据发送来的tfi.receiver来决定群发或者私发
     *
     * @param tfi
     */
    private void ShakeHandle(TransferInfo tfi) {
        // 服务端转发给其他用户
        String receiver = tfi.getReceiver();
        if ("All".equals(receiver)) {
            // 发送给所有人
            sendAll(tfi);
            // 记录日志
            log("{Shaking} " + tfi.getSender() + "发送了一个抖动窗口");
        } else {
            // 私聊:预留功能
            sendPrivate(tfi);
        }
    }

    /**
     * 处理客户端的聊天请求---消息分类(聊天消息)
     * <p>
     * 根据发送来的tfi.receiver来决定群发或者私发
     *
     * @param
     */
    private void chatHandler(TransferInfo tfi) {
        // 服务端转发给其他用户
        String receiver = tfi.getReceiver();
        if ("All".equals(receiver)) {
            // 发送给所有人
            sendAll(tfi);
            // 记录日志:日志内容同时也应该是编码后的样式、而非纯文本内容
            List<FontStyle> contents = tfi.getContent();
            // 转发的过程中同时记录、解析到JTextPane这个文本框中:日志区域(因为服务器作为中转站---且日志区域位于服务器上)
            FontSupport.fontDecode(serverFrame.serverInfoPanel.txtLog, contents, tfi.getSender());
        } else {
            // 私聊:预留功能
            sendPrivate(tfi);
        }
    }

    /**
     * 处理客户端的登录请求---消息分类(登陆消息)
     *
     * @param tfi
     */
    private void loginHandler(TransferInfo tfi) throws Exception {
        boolean flag = checkUserLogin(tfi);
        if (flag) {
            // 返回"登陆成功"给客户端
            tfi.setLoginSuccessFlag(true);      // tfi 设置登录标志Flag为true
            tfi.setStatusEnum(ChatStatus.LOGIN);
            IOStream.writerMessage(socket, tfi);
            String userName = tfi.getUserName();

            /* 统计(添加)在线人数 */
            // 登记进入聊天室的用户的姓名---List<String>
            onlineUsers.add(userName);
            // 登记进入聊天室的用户socket---List<Socket>
            onlineSockets.add(socket);

            // 建立在线用户和管道流之间的对应关系;一旦用户登录、便记录该Socket-User键值对
            ChatServer.userSocketMap.put(userName, socket);     //Socket在开辟线程时带入、而非在TransferInfo中

            // 发消息给客户端，该用户已上线
            tfi = new TransferInfo();
            tfi.setStatusEnum(ChatStatus.NOTICE);
            tfi.setNotice("\n欢迎`" + userName + "`加入软工三班群聊!");
            sendAll(tfi);       // 用户登录时--群发消息

            // 发送最新用户列表给所有客户端
            tfi = new TransferInfo();
            tfi.setUserOnlineArray(onlineUsers.toArray(new String[onlineUsers.size()]));
            tfi.setStatusEnum(ChatStatus.ULIST);
            sendAll(tfi);

            // 刷新服务端在线用户列表---先客户端再服务端
            flushOnlineUserList();

            // 登陆成功时的日志记录方法调用
            String notice = "{Login} " + userName + "加入群聊!";
            log(notice);

        } else {
            // 返回"登录失败"给客户端
            tfi.setLoginSuccessFlag(false);     // 设置登录标志flag为false
            IOStream.writerMessage(socket, tfi);
            // 登录失败时的日志记录
            log("{Login} " + tfi.getUserName() + "登陆失败!");
        }
    }

    /**
     * 记录日志的方法:日期+日志内容(Contents或Icon)、所以追加时要用contentAppend()进行追加
     * 而不能单纯使用setText()来追加服务器日志！！！
     * ---此处花费一小时查找Bug(新用户登陆时-之前的聊天记录中{含有表情}的记录将会为空)
     * <p>
     */
    public void log(String notice) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);

        // Bug---Repair!!!
//        JTextPane txtLog = serverFrame.serverInfoPanel.txtLog;
//        txtLog.setText(txtLog.getText() + "\n[" + dateStr + "] " + notice);
        notice = "\n[" + dateStr + "] " + notice;
        FontSupport.contentAppend(serverFrame.serverInfoPanel.txtLog, notice);
    }

    /**
     * 刷新服务端在线成员列表:添加在线成员!
     */
    public void flushOnlineUserList() {
        // 获取服务器的用户列表
        JList lstUser = serverFrame.onlineUserPanel.lstUser;
        // onlineUsers对象数组转换成String数组--->即可添加公共在线用户列表到服务器端
        String[] userArray = onlineUsers.toArray(new String[onlineUsers.size()]);
        // 添加服务器端第二选项卡的在线成员表项
        lstUser.setListData(userArray);
        // 修改服务器面板第一选项卡的当前在线人数
        serverFrame.serverInfoPanel.txtNumber.setText(userArray.length + "");

    }


    /**
     * 发送消息给所有人:可用于登陆消息、发送(转发)消息
     *
     * @param tfi
     */
    public void sendAll(TransferInfo tfi) {
        for (int i = 0; i < onlineSockets.size(); i++) {
            // 循环发送消息给每一个用户
            Socket tempSocket = onlineSockets.get(i);
            IOStream.writerMessage(tempSocket, tfi);
        }
    }

    /**
     * 私聊方法!!!--->解决了私聊无法传到服务器的Bug！
     */
    public void sendPrivate(TransferInfo tfi) {

        // 发送给私聊的接收者
        String receiver = tfi.getReceiver();
        String sender = tfi.getSender();

        // 根据receiver拿到Socket管道(用户名为键-管道为值)
        Socket socket_R = ChatServer.userSocketMap.get(receiver);
        IOStream.writerMessage(socket_R, tfi);

        // 发送方窗口应该也要有消息
        Socket socket_S = ChatServer.userSocketMap.get(sender);
        IOStream.writerMessage(socket_S, tfi);

        // 转发的过程中同时记录、解析到JTextPane这个文本框中:日志区域(因为服务器作为中转站---且日志区域位于服务器上)
        // serverFrame.serverInfoPanel.txtLog--->必须设置为static--->Bug-Repair!!!!!!!!!------>将私聊内容记录到服务器上
        FontSupport.contentAppend(serverFrame.serverInfoPanel.txtLog, "\n" + tfi.getSender() + "对" + tfi.getReceiver() + "说: ***");

//        // 根据sender拿到Socket管道;仅有发送方和私聊的接收方可以收到
//        Socket socket_S = ChatServer.userSocketMap.get(sender);
//        IOStream.writerMessage(socket_S, tfi);

//        // 服务器日志记录
//        log(tfi.getSender() + " 对" + tfi.getReceiver() + "说:" + tfi.getContent());
    }

    /**
     * 检查登录信息是否正确:并将确认信息(boolean)返回给客户端
     *
     * @param tfi
     * @return
     */
    public boolean checkUserLogin(TransferInfo tfi) {
        try {
            String userName = tfi.getUserName();
            String password = tfi.getPassword();
            FileInputStream fis = new FileInputStream(new File("src/com/wyk/chat/registeredUser.txt"));
            DataInputStream dis = new DataInputStream(fis);
            String registeredUser = null;
            while ((registeredUser = dis.readLine()) != null) {
                // 从文件中读取单行已注册用户的信息
                if ((userName + "|" + password).equals(registeredUser)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
