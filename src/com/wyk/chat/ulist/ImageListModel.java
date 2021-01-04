package com.wyk.chat.ulist;

import com.wyk.chat.entity.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Jlist模型类、这个模型类最终提供调用
 * @Author: 57715
 * @Date: 2020/12/20 19:20
 * @Version: V1.0
 * @Email；577159462@qq.com
 */

public class ImageListModel extends AbstractListModel<User> {

    private List<User> list = new ArrayList<>();

    // 添加List元素
    public void addElement(User user) {
        list.add(user);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public User getElementAt(int index) {
        return list.get(index);
    }
}
