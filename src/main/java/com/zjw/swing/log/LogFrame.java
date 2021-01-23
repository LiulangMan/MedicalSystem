package com.zjw.swing.log;

import com.zjw.constant.IndexConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/22 18:02
 */
@Component
public class LogFrame extends JPanel {

    private JButton lastButton;

    @Autowired
    private LoginLogPanel loginLogPanel;

    @Autowired
    private OptionLogPanel optionLogPanel;

    public LogFrame(){
        super(null);
    }

    private void initFiled(){
       loginLogPanel.init();
       optionLogPanel.init();
    }

    public void refreshAllData() {
        loginLogPanel.refreshData();
        optionLogPanel.refreshData();
    }

    public void init() {

        this.initFiled();

        this.setSize(IndexConstant.CARD_WIDTH, IndexConstant.CARD_HIGH);
        this.setBackground(Color.yellow);

        //菜单栏
        JPanel menu = new JPanel(new GridLayout(3, 1));
        menu.setSize(100, 800);
        menu.setLocation(0, 0);
        this.add(menu);

        JButton button0 = new JButton("登陆日志");
        JButton button1 = new JButton("操作日志");

        menu.add(button0);
        menu.add(button1);

        //三个子菜单的卡片面板
        CardLayout cardLayout = new CardLayout();
        JPanel card = new JPanel(cardLayout);
        card.setSize(IndexConstant.CARD_WIDTH - menu.getWidth(), IndexConstant.CARD_HIGH);
        card.setLocation(menu.getWidth(),0);
        card.add(loginLogPanel,"登陆日志");
        card.add(optionLogPanel,"操作日志");
        this.add(card);

        lastButton = button0;
        button0.setBackground(Color.GREEN);

        /*监听*/
        button0.addActionListener(e -> {
            cardLayout.show(card,"登陆日志");
            button0.setBackground(Color.GREEN);
            if (lastButton != button0) {
                lastButton.setBackground(null);
                lastButton = button0;
            }
        });

        button1.addActionListener(e -> {
            cardLayout.show(card,"操作日志");
            button1.setBackground(Color.GREEN);
            if (lastButton != button1) {
                lastButton.setBackground(null);
                lastButton = button1;
            }
        });

    }

}
