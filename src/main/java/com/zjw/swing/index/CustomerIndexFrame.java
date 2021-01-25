package com.zjw.swing.index;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.Customer;
import com.zjw.service.LoginService;
import com.zjw.swing.helpCustomer.HelpOnline;
import com.zjw.swing.home.HomeFrame;
import com.zjw.swing.salesManager.SaleListPanel;
import com.zjw.swing.salesManager.SaleRecordPanel;
import com.zjw.swing.selfInformation.SelfInformationFrame;
import com.zjw.swing.setting.SettingFrame;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.config.StaticConfiguration;
import com.zjw.swing.utils.MySwingUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import com.zjw.utils.interfaceImpl.DefaultWindowsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/6 11:12
 */
@Component
public class CustomerIndexFrame extends JFrame {


    @Autowired
    private SelfInformationFrame selfInformationFrame;

    @Autowired
    private SettingFrame settingFrame;

    @Autowired
    private HomeFrame homeFrame;

    @Autowired
    private SaleListPanel goodsFrame;

    @Autowired
    private SaleRecordPanel saleRecordPanel;

    @Autowired
    private HelpOnline helpOnline;

    @Autowired
    private LoginService loginService;

    @Autowired
    private HeadEditFrame headEditFrame;

    //个人信息
    private JLabel name;
    private JLabel address;
    private JLabel phone;

    private JButton lastButton;

    private ImageJPanel head;

    private void initField() {
        homeFrame.init();
        selfInformationFrame.init();
        settingFrame.init();
        goodsFrame.init();
        saleRecordPanel.init();
        helpOnline.init();
    }

    private void refreshAllData() {
        //后台刷新
        SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
            @Override
            protected void done() {
                MySwingUtils.ProgressBar.closeProgressBar();
            }

            @Override
            protected Object doInBackground() throws Exception {
                homeFrame.refreshAnnouncementPanel();
                goodsFrame.refreshData();
                saleRecordPanel.refreshData();
                helpOnline.refreshData();

                return null;
            }
        };

        MySwingUtils.ProgressBar.showProgressBar("正在加载数据");
        worker.execute();
    }

    private void init() {
        //初始化
        initField();
        //初始化配置
        this.setSize(IndexConstant.CLIENT_WIDTH, IndexConstant.CLIENT_HIGH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("医疗销售管理系统-顾客版");
        this.addWindowListener(new DefaultWindowsListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                Customer customer = StaticConfiguration.getCustomer();
                loginService.logout(customer.getLoginName(), IndexConstant.LOGIN_TYPE_CUSTOMER);
            }
        });

    }


    public void run() {

        init();
        //主板
        JPanel mainPanel = new ImageJPanel(null, "/images/index/t5.jpg");
        this.setContentPane(mainPanel);

        //菜单面板
        JPanel menu = new JPanel(new GridLayout(8, 1));
        menu.setSize(200, 800);
        menu.setLocation(50, 100);
        mainPanel.add(menu);
        menu.setBackground(null);
        menu.setOpaque(false);

        JButton button0 = new JButton("主页");
        JButton button1 = new JButton("药物浏览");
        JButton button2 = new JButton("购买记录");
        JButton button3 = new JButton("问题咨询");
        JButton button4 = new JButton("个人信息");
        JButton button5 = new JButton("个性设置");

        menu.add(button0);
        menu.add(button1);
        menu.add(button2);
        menu.add(button3);
        menu.add(button4);
        menu.add(button5);

        //对应8个菜单的卡片面板
        CardLayout cardLayout = new CardLayout();
        JPanel card = new JPanel(cardLayout);
        card.setSize(IndexConstant.CARD_WIDTH, IndexConstant.CARD_HIGH);
        card.setLocation(250, 100);
        mainPanel.add(card);

        card.add(homeFrame, "主页");
        card.add(goodsFrame, "药物浏览");
        card.add(saleRecordPanel, "购买记录");
        card.add(helpOnline, "问题咨询");
        card.add(selfInformationFrame, "个人信息");
        card.add(settingFrame, "个性设置");

        //顶部信息面板
        JPanel top = new JPanel(null);
        top.setBackground(null);
        top.setOpaque(false);
        top.setLocation(0, 0);
        top.setSize(1500, 90);
        mainPanel.add(top);

        //头像
        boolean isDiy = StaticConfiguration.getCustomer().getImagesPath() != null &&
                StaticConfiguration.getCustomer().getImagesPath().startsWith("./");

        if (isDiy) {
            assert StaticConfiguration.getCustomer().getImagesPath() != null;
            head = new ImageJPanel(null, new File(StaticConfiguration.getCustomer().getImagesPath()));
        } else {
            String defaultHead = StaticConfiguration.getCustomer().getImagesPath() != null ?
                    StaticConfiguration.getCustomer().getImagesPath() : "/images/index/t5.jpg";
            head = new ImageJPanel(null, defaultHead);
        }
        head.setSize(80, 80);
        head.setLocation(100, 5);
        top.add(head);

        //标题
        JLabel title = new JLabel("医疗销售管理系统");
        title.setSize(300, 30);
        title.setLocation(600, 10);
        title.setFont(new Font(null, Font.PLAIN, 25));
        top.add(title);

        //个人信息部分展示
        JPanel information = new JPanel(new GridLayout(3, 1));
        information.setSize(300, 80);
        information.setLocation(1200, 5);
        information.setBackground(null);
        information.setOpaque(false);
        top.add(information);

        name = new JLabel("姓名：" + StaticConfiguration.getCustomer().getName());
        information.add(name);
        address = new JLabel("地区：" + StaticConfiguration.getCustomer().getAddress());
        information.add(address);
        phone = new JLabel("电话：" + StaticConfiguration.getCustomer().getPhone());
        information.add(phone);


        this.setVisible(true);

        lastButton = button0;
        button0.setBackground(Color.GREEN);

        /*菜单监听*/
        button0.addActionListener(e -> {
            cardLayout.show(card, "主页");
            button0.setBackground(Color.GREEN);
            if (lastButton != button0) {
                lastButton.setBackground(null);
                lastButton = button0;
            }
        });

        button1.addActionListener(e -> {
            cardLayout.show(card, "药物浏览");
            button1.setBackground(Color.GREEN);
            if (lastButton != button1) {
                lastButton.setBackground(null);
                lastButton = button1;
            }
        });

        button2.addActionListener(e -> {
            cardLayout.show(card, "购买记录");
            button2.setBackground(Color.GREEN);
            if (lastButton != button2) {
                lastButton.setBackground(null);
                lastButton = button2;
            }
        });

        button3.addActionListener(e -> {
            cardLayout.show(card, "问题咨询");
            button3.setBackground(Color.GREEN);
            if (lastButton != button3) {
                lastButton.setBackground(null);
                lastButton = button3;
            }
        });

        button4.addActionListener(e -> {
            cardLayout.show(card, "个人信息");
            button4.setBackground(Color.GREEN);
            if (lastButton != button4) {
                lastButton.setBackground(null);
                lastButton = button4;
            }
        });

        button5.addActionListener(e -> {
            cardLayout.show(card, "个性设置");
            button5.setBackground(Color.GREEN);
            if (lastButton != button5) {
                lastButton.setBackground(null);
                lastButton = button5;
            }
        });

        head.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //更换头像
                StaticConfiguration.addThreadPoolTask(() -> headEditFrame.run());
            }
        });


        //加载数据
        StaticConfiguration.addThreadPoolTask(this::refreshAllData);
    }

    public void refreshInformation() {
        name.setText("姓名：" + StaticConfiguration.getCustomer().getName());
        address.setText("地区：" + StaticConfiguration.getCustomer().getAddress());
        phone.setText("电话：" + StaticConfiguration.getCustomer().getPhone());
    }

    void changeImages(String imagesPath) {
        head.setVisible(false);
        head.changeImages(imagesPath);
        head.setVisible(true);
    }

    void changeImages(File file) {
        head.setVisible(false);
        head.changeImages(file);
        head.setVisible(true);
    }
}
