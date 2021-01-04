package com.wyk.chat.entity;

import java.awt.*;
import java.io.Serializable;

/**
 * @Description: 用于存放每一个字体的包装对象(序列化对象)+图片实体的包装------字体(Content)、图片(Icon)
 * @Author: 57715
 * @Date: 2020/12/19 17:16
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class FontStyle implements Serializable {
    /**
     * 字体属性:内容、字体样式、字体大小、字体颜色、字体类型-->打包成类传输给服务器、然后由
     * 服务器进行转发（sendAll方法）该包装了该类的 TransferInfo 对象
     */
    private static final long serialVersionUID = 1;

    // 图片路径
    private String path;

    // 字的内容
    private String content;

    // 字的字体
    private String fontFamily;

    // 字的大小
    private int size;

    // 字的颜色
    private Color color;

    // 字的样式
    private int fontStyle;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }
}
