package com.wyk.chat.util;

import com.wyk.chat.entity.FontStyle;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 实体类包装---序列流传输
 * @Author: 57715
 * @Date: 2020/12/19 16:27
 * @Version: V1.0
 * @Email；577159462@qq.com
 */
public class FontSupport {

    /**
     * @param txtSent    需要作用的文本框
     * @param color      颜色
     * @param fontFamily 字体
     * @param fontStyle  样式
     * @param fontSize   大小
     */
    public static void setFont(JTextPane txtSent, Color color, Object fontFamily, int fontStyle, int fontSize) {
        // 得到编辑器中的文档
        Document document = txtSent.getDocument();
        try {
            // 添加一个可以设置样式的类
            StyleContext sc = StyleContext.getDefaultStyleContext();    // 初始为默认的样式选择器
            // 为所添加的样式类添加字体颜色
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);      // 添加属性
            // 为添加的样式类添加字体
            Font font = new Font((String) fontFamily, fontStyle, fontSize);
            aset = sc.addAttribute(aset, StyleConstants.Family, font.getFamily());
            // 设置字体的大小
            aset = sc.addAttribute(aset, StyleConstants.FontSize, fontSize);
            // 根据 fontStyle 设置样式
            if (fontStyle == Font.BOLD) {
                aset = sc.addAttribute(aset, StyleConstants.Bold, true);
            } else if (fontStyle == Font.ITALIC) {
                aset = sc.addAttribute(aset, StyleConstants.Italic, true);
            } else if (fontStyle == Font.PLAIN) {
                aset = sc.addAttribute(aset, StyleConstants.Bold, false);
                aset = sc.addAttribute(aset, StyleConstants.Italic, false);
            }
            int start = txtSent.getSelectionStart();        // 起始位置
            int end = txtSent.getSelectionEnd();            // 末尾位置
            String str = document.getText(start, end - start);
            // 由于 JAVA 没有提供直接设置所选字的方法，只有先移除原来的字符串
            document.remove(start, end - start);
            // 然后重新插入字符串，并按新设置的样式进行插入
            document.insertString(start, str, aset);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 内容编码:可能是字体、也可能是图片
     *
     * @param textPane
     * @return
     */
    public static List<FontStyle> fontEncode(JTextPane textPane) {

        Document document = textPane.getDocument();
        List<FontStyle> list = new ArrayList<FontStyle>();
        for (int i = 0; i < document.getLength(); i++) {
            try {
                StyledDocument sd = textPane.getStyledDocument();   // 得到样式文档对象
                FontStyle font = new FontStyle();
                Element e = sd.getCharacterElement(i);      // 获取每个字符
                if (e instanceof AbstractDocument.LeafElement) {     // 属性匹配
                    // 匹配内容文字
                    if (e.getName().equals("content")) {
                        AttributeSet as = e.getAttributes();
                        font.setContent(sd.getText(i, 1));
                        font.setFontFamily(as.getAttribute(StyleConstants.Family).toString());
                        font.setSize((Integer) as.getAttribute(StyleConstants.FontSize));
                        font.setFontStyle((Integer) as.getAttribute(StyleConstants.FontSize));
                        font.setColor((Color) as.getAttribute(StyleConstants.Foreground));
                        if (StyleConstants.isBold(as)) {
                            font.setFontStyle(Font.BOLD);
                        } else if (StyleConstants.isItalic(as)) {
                            font.setFontStyle(Font.ITALIC);
                        } else {
                            font.setFontStyle(Font.PLAIN);
                        }
                    } else if (e.getName().equals("icon")) {
                        // 匹配图片内容:设置发送时的图片路径,服务器接收到后转发出去
                        font.setPath((e.getAttributes().getAttribute(StyleConstants.IconAttribute)).toString());
                    }
                }
                list.add(font);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    static StyleContext sc = null;

    /**
     * 内容解码
     *
     * @return
     */
    public static void fontDecode(JTextPane textPane, List<FontStyle> list, String sender) {
        // 文档对象 doc --- 并使用FontSupport提供的文本追加方法
        Document doc = contentAppend(textPane, "\n" + sender + "：");
        sc = StyleContext.getDefaultStyleContext();
        // 将每个文字/图片进行遍历
        for (FontStyle zi : list) {
            if (zi != null) {
                // 得到编辑器中的文档
                if (zi.getContent() != null) {
                    try {
                        // 为所添加的样式类添加字体颜色(aset:属性集)
                        Color color = zi.getColor();
                        if (color == null) {
                            color = Color.BLACK;
                        }
                        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
                        Font font = new Font(zi.getFontFamily(), zi.getFontStyle(), zi.getSize());
                        // 为添加的样式类添加字体
                        aset = sc.addAttribute(aset, StyleConstants.Family, font.getFamily());
                        // 设置字体的大小
                        aset = sc.addAttribute(aset, StyleConstants.FontSize, zi.getSize());
                        System.out.println(zi.getSize());
                        if (zi.getFontStyle() == Font.BOLD) {
                            aset = sc.addAttribute(aset, StyleConstants.Bold, true);
                        } else if (zi.getFontStyle() == Font.ITALIC) {
                            aset = sc.addAttribute(aset, StyleConstants.Italic, true);
                        } else if (zi.getFontStyle() == Font.PLAIN) {
                            aset = sc.addAttribute(aset, StyleConstants.Bold, false);
                            aset = sc.addAttribute(aset, StyleConstants.Italic, false);
                        }
                        // 将解码后的文字显示到文档中(弹幕上)
                        doc.insertString(doc.getLength(), zi.getContent(), aset);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    // 非字即为图
                    // 解析图片至接收框
                    textPane.setCaretPosition(doc.getLength());
                    textPane.insertIcon(new ImageIcon(zi.getPath()));
                }
            }
        }
    }

    /**
     * 用于给面板追加内容(存在的原因:{防止覆盖时进行的处理->就是进行追加->但是那处地方追加的并非编码对象而是字符串便会导致出错})
     *
     * @param textPane
     * @param content
     */
    public static Document contentAppend(JTextPane textPane, String content) {
        // 取得文档对象
        Document doc = textPane.getDocument();
        // 添加一个可以设置样式的类
        StyleContext sc = StyleContext.getDefaultStyleContext();
        // 为所添加的样式类添加字体颜色
        AttributeSet asetLine = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.black);
        try {
            doc.insertString(doc.getLength(), content, asetLine);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
