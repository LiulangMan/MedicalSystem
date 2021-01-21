package com.zjw.swing.index;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.Employ;
import com.zjw.service.LoginService;
import com.zjw.swing.helpEmployee.HelpEmployPlane;
import com.zjw.swing.home.HomeFrame;
import com.zjw.swing.log.LogFrame;
import com.zjw.swing.stockManager.StockManagerFrame;
import com.zjw.swing.salesManager.SalesMangerFrame;
import com.zjw.swing.selfInformation.SelfInformationFrame;
import com.zjw.swing.setting.SettingFrame;
import com.zjw.swing.userManager.UserMangerFrame;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.config.StaticConfiguration;
import com.zjw.swing.utils.MySwingUtils;
import com.zjw.utils.interfaceImpl.DefaultWindowsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 20:00
 */
@Component
public class EmployIndexFrame extends JFrame {

    @Autowired
    private LoginService loginService;

    @Autowired
    private HelpEmployPlane helpEmployPlane;

    @Autowired
    private StockManagerFrame procurementManagerFrame;

    @Autowired
    private SalesMangerFrame salesMangerFrame;

    @Autowired
    private SelfInformationFrame selfInformationFrame;

    @Autowired
    private SettingFrame settingFrame;

    @Autowired
    private UserMangerFrame userMangerFrame;

    @Autowired
    private HomeFrame homeFrame;

    @Autowired
    private LogFrame logFrame;

    private JButton lastButton;

    //展示信息
    private JLabel name;
    private JLabel address;
    private JLabel role;

    private void initField() {
        homeFrame.init();
        helpEmployPlane.init();
        procurementManagerFrame.init();
        salesMangerFrame.init();
        selfInformationFrame.init();
        settingFrame.init();
        if (StaticConfiguration.getEmploy().getType() == IndexConstant.LOGIN_TYPE_ADMIN) {
            userMangerFrame.init();
            logFrame.init();
        }
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
                helpEmployPlane.refreshData();
                salesMangerFrame.refreshAllData();
                procurementManagerFrame.refreshAllData();

                if (StaticConfiguration.getEmploy().getType() == IndexConstant.LOGIN_TYPE_ADMIN) {
                    userMangerFrame.refreshData();
                    logFrame.refreshData();
                }
                return null;
            }
        };

        MySwingUtils.ProgressBar.showProgressBar("正在加载数据");
        worker.execute();
    }

    private void init() {

        initField();
        //初始化配置
        this.setSize(IndexConstant.CLIENT_WIDTH, IndexConstant.CLIENT_HIGH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("医疗销售管理系统-员工版");
        this.addWindowListener(new DefaultWindowsListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                //关闭窗口，退出登陆
                Employ employ = StaticConfiguration.getEmploy();
                loginService.logout(employ.getLoginName(), employ.getType());
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
        menu.setBackground(null);
        menu.setOpaque(false);
        mainPanel.add(menu);

        JButton button0 = new JButton("主页");
        JButton button1 = new JButton("销售管理");
        JButton button2 = new JButton("采购管理");
        JButton button3 = new JButton("用户管理");
        JButton button4 = new JButton("问题答疑");
        JButton button5 = new JButton("登陆日志");
        JButton button6 = new JButton("个人信息");
        JButton button7 = new JButton("个性设置");

        menu.add(button0);
        menu.add(button1);
        menu.add(button2);
        menu.add(button4);
        menu.add(button6);
        menu.add(button7);

        //对应8个菜单的卡片面板
        CardLayout cardLayout = new CardLayout();
        JPanel card = new JPanel(cardLayout);
        card.setSize(IndexConstant.CARD_WIDTH, IndexConstant.CARD_HIGH);
        card.setLocation(250, 100);
        mainPanel.add(card);

        card.add(homeFrame, "主页");
        card.add(salesMangerFrame, "销售管理");
        card.add(procurementManagerFrame, "采购管理");
        card.add(helpEmployPlane, "问题答疑");
        card.add(selfInformationFrame, "个人信息");
        card.add(settingFrame, "个性设置");

        //超级管理员模块
        if (StaticConfiguration.getEmploy().getType() == IndexConstant.LOGIN_TYPE_ADMIN) {
            menu.add(button3);
            menu.add(button5);
            card.add(userMangerFrame, "用户管理");
            card.add(logFrame, "登陆日志");
        }

        //顶部信息面板
        JPanel top = new JPanel(null);
        top.setLocation(0, 0);
        top.setSize(1500, 90);
        top.setBackground(null);
        top.setOpaque(false);
        mainPanel.add(top);

        //标题
        JLabel title = new JLabel("医疗销售管理系统");
        title.setSize(300, 30);
        title.setLocation(600, 10);
        title.setFont(new Font(null, Font.PLAIN, 25));
        top.add(title);

        //头像
        JPanel head = new ImageJPanel(null, "/images/login/t1.jpg");
        head.setSize(80, 80);
        head.setLocation(100, 5);
        top.add(head);

        //个人信息部分展示
        JPanel information = new JPanel(new GridLayout(3, 1));
        information.setSize(300, 80);
        information.setLocation(1200, 5);
        information.setBackground(null);
        information.setOpaque(false);
        top.add(information);

        name = new JLabel("姓名：" + StaticConfiguration.getEmploy().getName());
        information.add(name);
        address = new JLabel("地区：" + StaticConfiguration.getEmploy().getAddress());
        information.add(address);
        role = new JLabel("角色：" + (StaticConfiguration.getEmploy().getType() == IndexConstant.LOGIN_TYPE_ADMIN ? "超级管理员" : "员工"));
        information.add(role);

        MySwingUtils.ProgressBar.closeProgressBar();

        //颜色变化
        lastButton = button0;
        button0.setBackground(Color.GREEN);

        this.setVisible(true);

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
            cardLayout.show(card, "销售管理");
            button1.setBackground(Color.GREEN);
            if (lastButton != button1) {
                lastButton.setBackground(null);
                lastButton = button1;
            }
        });

        button2.addActionListener(e -> {
            cardLayout.show(card, "采购管理");
            button2.setBackground(Color.GREEN);
            if (lastButton != button2) {
                lastButton.setBackground(null);
                lastButton = button2;
            }
        });

        button3.addActionListener(e -> {
            cardLayout.show(card, "用户管理");
            button3.setBackground(Color.GREEN);
            if (lastButton != button3) {
                lastButton.setBackground(null);
                lastButton = button3;
            }
        });

        button4.addActionListener(e -> {
            cardLayout.show(card, "问题答疑");
            button4.setBackground(Color.GREEN);
            if (lastButton != button4) {
                lastButton.setBackground(null);
                lastButton = button4;
            }
        });

        button5.addActionListener(e -> {
            cardLayout.show(card, "登陆日志");
            button5.setBackground(Color.GREEN);
            if (lastButton != button5) {
                lastButton.setBackground(null);
                lastButton = button5;
            }
        });

        button6.addActionListener(e -> {
            cardLayout.show(card, "个人信息");
            button6.setBackground(Color.GREEN);
            if (lastButton != button6) {
                lastButton.setBackground(null);
                lastButton = button6;
            }
        });

        button7.addActionListener(e -> {
            cardLayout.show(card, "个性设置");
            button7.setBackground(Color.GREEN);
            if (lastButton != button7) {
                lastButton.setBackground(null);
                lastButton = button7;
            }
        });

        //加载数据
        StaticConfiguration.addThreadPoolTask(new Runnable() {
            @Override
            public void run() {
                refreshAllData();
            }
        });
    }

    public void refreshInformation() {
        name.setText("姓名：" + StaticConfiguration.getEmploy().getName());
        address.setText("地区：" + StaticConfiguration.getEmploy().getAddress());
        role.setText("角色：" + (StaticConfiguration.getEmploy().getType() == IndexConstant.LOGIN_TYPE_ADMIN ? "超级管理员" : "员工"));
    }

}
