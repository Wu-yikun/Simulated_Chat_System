package com.wyk.chat.client;

import com.wyk.chat.entity.ChatStatus;
import com.wyk.chat.entity.FontStyle;
import com.wyk.chat.entity.TransferInfo;
import com.wyk.chat.entity.User;
import com.wyk.chat.io.IOStream;
import com.wyk.chat.ulist.ImageCellRenderer;
import com.wyk.chat.ulist.ImageListModel;
import com.wyk.chat.util.FontSupport;

import javax.swing.*;
import java.net.Socket;
import java.util.List;

/**
 * @Description: 客户端开辟不断读取消息的线程
 * @Author: 57715
 * @Date: 2020/12/16 14:54
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class ClientHandler extends Thread {

    Socket socket;

    // 登录窗体
    public LoginFrame loginFrame;

    // 聊天主窗体
    public ChatFrame chatFrame;

    public ClientHandler(Socket socket, LoginFrame loginFrame) {
        this.socket = socket;
        this.loginFrame = loginFrame;
    }

    @Override
    public void run() {
        while (true) {
            // 模拟不停获取消息，产生阻塞
            try {
                Object obj = IOStream.readMessage(socket);  // ---使用IOStream工具类实现代码复用
                if (obj instanceof TransferInfo) {
                    TransferInfo tfi = (TransferInfo) obj;
                    // 消息分类判断
                    if (tfi.getStatusEnum() == ChatStatus.LOGIN) {
                        // 登录消息
                        loginResult(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.CHAT) {
                        // 聊天消息
                        chatResult(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.NOTICE) {
                        // 系统消息
                        noticeResult(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.ULIST) {
                        // 刷新当前在线用户列表
                        onlineUsersResult(tfi);
                    } else if (tfi.getStatusEnum() == ChatStatus.DD) {
                        // 抖动消息
                        shakeResult(tfi);
                    }
                } else {
                    // 拿到的对象不是TransferInfo---不做处理
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 抖动消息的处理:接收从服务器发来的抖动信息
     *
     * @param tfi
     */
    private void shakeResult(TransferInfo tfi) {
        // 调用 Shake 类进行多线程抖动
        Shake shake = new Shake(chatFrame);
        shake.start();
    }

    /**
     * 聊天消息的处理
     *
     * @param tfi
     */
    public void chatResult(TransferInfo tfi) {
        // 获取发消息的用户名字
        String sender = tfi.getSender();
        List<FontStyle> contents = tfi.getContent();     // 返回文字列表
        // 文字解析类进行解析处理---解析到收到该聊天消息的客户端chatFrame.acceptPane
        FontSupport.fontDecode(chatFrame.acceptPane, contents, sender);


//        // 用户发送的消息置于公屏(摒弃---只能处理纯文本消息)
//        String text = chatFrame.acceptPane.getText();
//        chatFrame.acceptPane.setText(text + '\n' + sender + ":" + contents);
    }

    /**
     * 登陆消息的处理
     *
     * @param tfi
     */
    public void loginResult(TransferInfo tfi) {
        // tfi.getLoginSuccessFlag()-->用服务器返回的验证结果(是否已注册的用户)来进行下一步操作
        if (tfi.getLoginSuccessFlag()) {

            // 根据实体类取出用户名
            String userName = tfi.getUserName();
            // 登录成功,打开聊天室主界面
            chatFrame = new ChatFrame(userName, socket);    // 同时传递登录用户的姓名给聊天主界面
            // 初始化聊天室时---公屏上的消息
            chatFrame.acceptPane.setText("入群请修改群名片为:学号-姓名!");
            loginFrame.dispose();        //同时关闭登陆界面
        } else {
            JOptionPane.showMessageDialog(null, "您输入在账户名或密码不正确!");
        }
    }

    /**
     * 系统消息的处理
     *
     * @param tfi
     */
    public void noticeResult(TransferInfo tfi) {
        // 往公屏chatFrame.acceptPane上投射系统消息tfi.getNotice()
        FontSupport.contentAppend(chatFrame.acceptPane, tfi.getNotice());

        // 若使用下面两行代码---则会导致字体样式紊乱---因为追加的是非编码的字体("...加入群聊")
//        StringBuffer text = new StringBuffer(chatFrame.acceptPane.getText());
//        chatFrame.acceptPane.setText(String.valueOf(text.append(tfi.getNotice())));
    }

    /**
     * 刷新当前界面(客户端)的用户列表
     *
     * @param tfi
     */
    public void onlineUsersResult(TransferInfo tfi) {
        String[] userOnlineArray = tfi.getUserOnlineArray();
        // 展示
        ImageListModel model = new ImageListModel();
        for (String userName : userOnlineArray) {
            User user = new User();
            user.setUserName(userName);
            user.setNickName("昵称");
            user.setMotto("这个人很懒,什么都没留下~");
            user.setUiconPath("src/image/uicon/" + userName + ".jpg");
            model.addElement(user);
        }

        // JList 的模型、存放数据
        chatFrame.lstUser.setModel(model);

        // 自定义皮肤或样式
        chatFrame.lstUser.setCellRenderer(new ImageCellRenderer());

    }
}
