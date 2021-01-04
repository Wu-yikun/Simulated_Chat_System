package com.wyk.chat.client;

import com.wyk.chat.entity.ChatStatus;
import com.wyk.chat.entity.FontStyle;
import com.wyk.chat.entity.TransferInfo;
import com.wyk.chat.entity.User;
import com.wyk.chat.io.IOStream;
import com.wyk.chat.server.ServerHandler;
import com.wyk.chat.ulist.ImageCellRenderer;
import com.wyk.chat.ulist.ImageListModel;
import com.wyk.chat.util.FontSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.List;

/**
 * 聊天主界面:客户端聊天界面
 */
public class ChatFrame extends JFrame {

    private static final Integer FRAME_WIDTH = 750;    /* 登录窗体宽度 */
    private static final Integer FRAME_HEIGHT = 600;   /* 登录窗体高度 */

    // 客户端上面的消息屏幕
    public JTextPane acceptPane;

    // 当前在线用户列表
    public JList lstUser;

    // 字体样式下拉列表框
    public JComboBox fontFamilyCmb;

    // 实例化本类对象
    public ChatFrame chatFrame;

    // 下拉接收框
    public JComboBox receiverBox;

    // 用户姓名
    public String userName;

    public Socket socket;

    public ChatFrame(String userName, Socket socket) {

        this.setTitle("聊天室主界面【Current_User-->" + userName + "】");   // 窗口名
        chatFrame = this;                           // 本窗口
        this.userName = userName;                   // 当前用户的姓名
        this.socket = socket;                       // 方便发送方发送数据
        setSize(FRAME_WIDTH, FRAME_HEIGHT);         // 设置窗体大小
        setResizable(false);                     // 禁用窗口最大最小化
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 窗口退出即程序运行结束

        /* 获取屏幕对象的高度和宽度 */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setLocation((width - FRAME_WIDTH) / 2, (height - FRAME_HEIGHT) / 2);     // 设置窗口居中显示

        //加载窗体的背景图片
        ImageIcon imageIcon = new ImageIcon("src/image/bck.jpg");
        //创建标签、并将图片添加进去:此时frameBg为总容器
        JLabel frameBg = new JLabel(imageIcon);
        //设置图片的位置和大小
        frameBg.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        this.add(frameBg);

        //接收框
        acceptPane = new JTextPane();
        acceptPane.setOpaque(false);    // 设置透明
        acceptPane.setEditable(false);  // 设置接收框不能被编辑
        acceptPane.setFont(new Font("三极榜书", 0, 16));
        acceptPane.setSize(100, 200);

        //设置接收框滚动条
        JScrollPane scoPaneOne = new JScrollPane(acceptPane);
        scoPaneOne.setBounds(15, 20, 500, 332);
//        scoPaneOne.setOpaque(false);        // 设置背景透明
//        scoPaneOne.getViewport().setOpaque(false);
        frameBg.add(scoPaneOne);

        //当前在线用户列表
        lstUser = new JList();
        lstUser.setFont(new Font("三极榜书", 0, 14));
        lstUser.setVisibleRowCount(17);     // 可见的最大行数
        lstUser.setFixedCellWidth(180);     // 单元格的宽度
        lstUser.setFixedCellHeight(60);     // 单元格的高度

        // 添加要展示的Icon
        ImageListModel model = new ImageListModel();
        User user = new User();
        user.setUserName(userName);
        user.setNickName("昵称");
        user.setMotto("这个人很懒、什么都没留下~");
        user.setUiconPath("src/image/uicon/" + userName + ".jpg");
        // 添加该用户
        model.addElement(user);

        // JList 的模型、存放数据
        lstUser.setModel(model);

        // 自定义皮肤或样式
        lstUser.setCellRenderer(new ImageCellRenderer());


        // 声明菜单（右键用户列表项弹出的菜单）
        JPopupMenu popupMenu = new JPopupMenu();

        // 添加点击事件:需要确认是左键还是右键
        lstUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 监听是左键还是右键
                if (e.isMetaDown()) {
                    if (lstUser.getSelectedIndex() >= 0) {  // 选中的才可以弹出、否则空白区域也能弹出!
                        // 右键弹出菜单
                        popupMenu.show(lstUser, e.getX(), e.getY());
                    }
                }
            }
        });

        // 私聊按钮(右键菜单项1)
        JMenuItem privateChat = new JMenuItem("私 聊");
        privateChat.addActionListener(e -> {
            // 告诉发送消息的接收者
            Object receiverObj = lstUser.getSelectedValue();
            if (receiverObj instanceof User) {
                // 获得选中的接收方
                User user1 = (User) receiverObj;
                String receiver = user1.getUserName();
                // 添加至聊天对象的下拉选择框
                receiverBox.removeAllItems();
                receiverBox.addItem("All");
                receiverBox.addItem(receiver);
                receiverBox.setSelectedItem(receiver);
            }
        });
        popupMenu.add(privateChat);

        // 黑名单按钮(右键菜单项2)
        JMenuItem blackList = new JMenuItem("黑 名 单");
        blackList.addActionListener(e -> {
            // 暂时搁置---以后实现
        });
        popupMenu.add(blackList);

        JScrollPane spUser = new JScrollPane(lstUser);
        spUser.setFont(new Font("三极榜书", 0, 14));
        spUser.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spUser.setBounds(530, 17, 200, 507);
        frameBg.add(spUser);

        //输入框
        JTextPane sendPane = new JTextPane();
        sendPane.setOpaque(false);
        sendPane.setFont(new Font("三极榜书", 0, 16));

        JScrollPane scoPane = new JScrollPane(sendPane);     // 设置滚动条
        scoPane.setBounds(15, 400, 500, 122);
//        scoPane.setOpaque(false);
//        scoPane.getViewport().setOpaque(false);
        frameBg.add(scoPane);

        //添加表情选择
        JLabel lblface = new JLabel(new ImageIcon("src/image/face.png"));
        lblface.setBounds(14, 363, 25, 25);
        lblface.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new FaceFrame(sendPane);        // 跳出表情选择框
            }
        });
        frameBg.add(lblface);

        //添加抖动效果:事件监听
        JLabel lbldoudong = new JLabel(new ImageIcon("src/image/doudong.png"));
        lbldoudong.setBounds(43, 363, 25, 25);
        lbldoudong.addMouseListener(new MouseAdapter() {

            // MouseAdapter():重写父类、减少子类不必要的覆写工作
            @Override
            public void mouseClicked(MouseEvent e) {

                // 消息分类:抖动功能的实现
                TransferInfo tfi = new TransferInfo();
                // 标志tfi
                tfi.setStatusEnum(ChatStatus.DD);
                tfi.setSender(userName);
                String receiver = "All";    // 若无修改、则默认为All
                // 发送给私聊者
                Object receiverObj = receiverBox.getSelectedItem();
                if (receiverObj != null) {
                    receiver = String.valueOf(receiverObj);     // 私聊
                }
                tfi.setReceiver(receiver);
                IOStream.writerMessage(socket, tfi);
            }

        });
        frameBg.add(lbldoudong);

        //设置字体选择:事件监听
        JLabel lblfontChoose = new JLabel(new ImageIcon("src/image/ziti.png"));
        lblfontChoose.setBounds(44, 363, 80, 25);
        lblfontChoose.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JColorChooser colorChooser = new JColorChooser();
                Color color = colorChooser.showDialog(ChatFrame.this, "字体颜色", Color.BLACK);
                // 字体改变
                FontSupport.setFont(sendPane, color, fontFamilyCmb.getSelectedItem().toString(), Font.BOLD, 16);
            }

        });
        frameBg.add(lblfontChoose);

        //发送按钮
        JButton send = new JButton("发 送");
        send.setBounds(15, 533, 125, 25);
        send.addActionListener(e -> {

            TransferInfo tfi = new TransferInfo();

            // 包装了所有文字对应的属性,文字编码
            List<FontStyle> fontStyles = FontSupport.fontEncode(sendPane);
            tfi.setContent(fontStyles);     // 包装文字|图片

            // 发送方(每次发送消息都会设置发送方的姓名)！
            tfi.setSender(userName);    // 发送方设置为当前窗口的用户

            // 如果未点击私聊-->则默认receiver:所有人
            String receiver = "All";
            // 获取当前私聊对象-->进行下一步通信操作(receiver:receiverObj)
            Object receiverObj = receiverBox.getSelectedItem();
            if (receiverObj != null) {
                receiver = String.valueOf(receiverObj);     // 覆盖"所有人"
            }
            // 在TransferInfo对象中设置接收方!
            tfi.setReceiver(receiver);
            // 本次处理的消息为聊天
            tfi.setStatusEnum(ChatStatus.CHAT);
            IOStream.writerMessage(socket, tfi);

            // 发送后->清空输入框
            sendPane.setText("");
        });
        frameBg.add(send);

        //字体下拉选项
        fontFamilyCmb = new JComboBox();
        //直接让系统调用、而不是手动输入
//        fontFamilyCmb.addItem("宋体");
//        fontFamilyCmb.addItem("楷体");
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();        // 获取系统环境
        String[] str = graphicsEnvironment.getAvailableFontFamilyNames();       // 获取字体数组
        for (String string : str) {
            fontFamilyCmb.addItem(string);  // 逐一添加字体到下拉列表框
        }
        fontFamilyCmb.setSelectedItem("三极榜书");
        fontFamilyCmb.setBounds(104, 363, 150, 25);
        frameBg.add(fontFamilyCmb);

        /* 客户端右下角选择聊天对象的下拉选择框 */
        // 聊天对象
        JLabel receiverLabel = new JLabel("聊天对象");
        Font font = new Font("三极榜书", Font.PLAIN, 12);
        receiverLabel.setFont(font);
        receiverLabel.setForeground(Color.WHITE);
        receiverLabel.setBounds(304, 363, 80, 25);
        frameBg.add(receiverLabel);

        // 下拉选择框
        receiverBox = new JComboBox();
        receiverBox.setSelectedItem("All");     // 初始聊天对象为All
        receiverBox.addItem("All");
        receiverBox.setBounds(365, 363, 150, 25);
        frameBg.add(receiverBox);

        // 客户端关闭窗体退出 (解决客户端退出的异常)
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    System.out.println(userName + "退出群聊~");
                    TransferInfo tfi = new TransferInfo();
                    tfi.setStatusEnum(ChatStatus.QUIT);
                    tfi.setUserName(userName);
                    tfi.setNotice("\n" + userName + "已离开聊天室...");
                    IOStream.writerMessage(socket, tfi);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

//    public static void main(String[] args) {
//        new ChatFrame();
//    }
}
