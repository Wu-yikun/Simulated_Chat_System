package com.wyk.chat.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 数据封装用户信息
 * @Author: 57715
 * @Date: 2020/12/16 16:55
 * @Version: V1.0
 * @Email；577159462@qq.com
 */

/**
 * 消息分类:因为所有的IO流传输几乎都是通过TransferInfo消息传送的,所以有必要进行分类来判断
 * 1.登陆消息
 * 2.系统消息
 * 3.聊天消息
 * 4.窗体抖动
 * 5.用户列表
 * 6.用户退出
 */

// 通过IO流传输对象TransferInfo对象要->序列化接口
public class TransferInfo implements Serializable {

    private static final long serialVersionUID = 726188818021057995L;

    private String userName;
    private String password;

    // 聊天消息内容:包括字体样式(Contents)和图片内容(Icon->String)---采用编码<->解码的方式附载在TransferInfo对象上进行传送
    private List<FontStyle> content;

    // 系统消息
    private String notice;

    // 登录成功标志(默认为false)
    private Boolean loginSuccessFlag = false;

    // 消息类型枚举
    private ChatStatus statusEnum;

    // 在线的用户列表
    private String[] userOnlineArray;

    // 发送方
    private String sender;

    // 接收方
    private String receiver;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public List<FontStyle> getContent() {
        return content;
    }

    public void setContent(List<FontStyle> content) {
        this.content = content;
    }

    public String[] getUserOnlineArray() {
        return userOnlineArray;
    }

    public void setUserOnlineArray(String[] userOnlineArray) {
        this.userOnlineArray = userOnlineArray;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getNotice() {
        return notice;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLoginSuccessFlag() {
        return loginSuccessFlag;
    }

    public void setLoginSuccessFlag(Boolean loginSuccessFlag) {
        this.loginSuccessFlag = loginSuccessFlag;
    }

    public ChatStatus getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(ChatStatus statusEnum) {
        this.statusEnum = statusEnum;
    }
}
