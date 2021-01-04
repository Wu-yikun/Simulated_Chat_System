package com.wyk.chat.client;

/**
 * @Description: ---完成抖动效果
 * @Author: 57715
 * @Date: 2020/12/19 0:50
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class Shake extends Thread {

    // 选择要进行抖动的窗体
    public ChatFrame frame;

    public Shake(ChatFrame frame) {
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 3; i++) {
                frame.setLocation(frame.getX() - 10, frame.getY());
                Thread.sleep(30);
                frame.setLocation(frame.getX() - 20, frame.getY());
                Thread.sleep(30);
                frame.setLocation(frame.getX(), frame.getY() - 10);
                Thread.sleep(30);
                frame.setLocation(frame.getX(), frame.getY() - 20);
                Thread.sleep(30);
                frame.setLocation(frame.getX() + 10, frame.getY());
                Thread.sleep(30);
                frame.setLocation(frame.getX() + 20, frame.getY());
                Thread.sleep(30);
                frame.setLocation(frame.getX(), frame.getY() + 10);
                Thread.sleep(30);
                frame.setLocation(frame.getX(), frame.getY() + 20);
                Thread.sleep(30);
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
