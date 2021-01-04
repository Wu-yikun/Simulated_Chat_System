package com.wyk.chat.entity;

/**
 * @Description: 服务器参数信息类:封装主机名、IP地址、端口号
 * @Author: 57715
 * @Date: 2020/12/19 6:53
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class ServerInfoBean {

    private String hostName;
    private String ip;
    private Integer port;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
