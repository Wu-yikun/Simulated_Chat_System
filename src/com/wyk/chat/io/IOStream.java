package com.wyk.chat.io;

import java.io.*;
import java.net.Socket;

/**
 * @Description: IO流工具类---减少代码的冗余
 * @Author: 57715
 * @Date: 2020/12/16 15:53
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class IOStream {

    /**
     * 从 Socket 中读取对象消息TransferInfo
     *
     * @param socket
     * @return
     */
    public static Object readMessage(Socket socket) {
        Object obj = null;
        try {
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 根据 Socket 管道写出对象消息TransferInfo
     *
     * @param socket
     */
    public static void writerMessage(Socket socket, Object msg) {
        try {
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(msg);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
