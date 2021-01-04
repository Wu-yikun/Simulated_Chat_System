package com.wyk.chat.entity;

/**
 * @Description: 消息类型枚举
 * @Author: 57715
 * @Date: 2020/12/16 21:03
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public enum ChatStatus {

    LOGIN(1, "登录消息"),
    NOTICE(2, "系统消息"),
    CHAT(3, "聊天消息"),
    DD(4, "抖动消息"),
    ULIST(5, "在线用户列表"),
    QUIT(6, "退出");

    private Integer status;     // 消息分类码
    private String desc;        // 消息描述

    private ChatStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
