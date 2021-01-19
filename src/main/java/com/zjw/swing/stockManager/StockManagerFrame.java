package com.zjw.swing.stockManager;

import com.zjw.constant.IndexConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 21:04
 */
@Component
public class StockManagerFrame extends JPanel {

    @Autowired
    private StockListPanel stockListPanel;

    @Autowired
    private StockRecordPanel stockRecordPanel;

    @Autowired
    private SupplierPanel supplierPanel;

    private JButton lastButton;

    public StockManagerFrame(){
        super(null);
    }


    private void initFiled() {
        stockListPanel.init();
        stockRecordPanel.init();
        supplierPanel.init();
    }

    public void init() {

        //初始化域
        initFiled();

        this.setSize(IndexConstant.CARD_WIDTH, IndexConstant.CARD_HIGH);


        //菜单栏
        JPanel menu = new JPanel(new GridLayout(3, 1));
        menu.setSize(100, 800);
        menu.setLocation(0, 0);
        this.add(menu);

        JButton button0 = new JButton("采购列表");
        JButton button1 = new JButton("采购记录");
        JButton button2 = new JButton("供应商管理");

        menu.add(button0);
        menu.add(button1);
        menu.add(button2);

        //三个子菜单的卡片面板
        CardLayout cardLayout = new CardLayout();
        JPanel card = new JPanel(cardLayout);
        card.setSize(IndexConstant.CARD_WIDTH - menu.getWidth(), IndexConstant.CARD_HIGH);
        card.setLocation(menu.getWidth(), 0);
        card.add(stockListPanel, "采购列表");
        card.add(stockRecordPanel, "采购记录");
        card.add(supplierPanel, "供应商管理");
        this.add(card);


        lastButton = button0;
        /*监听*/
        button0.addActionListener(e -> {
            cardLayout.show(card, "采购列表");
            button0.setBackground(Color.GREEN);
            if (lastButton != button0) {
                lastButton.setBackground(null);
                lastButton = button0;
            }
        });

        button1.addActionListener(e -> {
            cardLayout.show(card, "采购记录");
            button1.setBackground(Color.GREEN);
            if (lastButton != button1) {
                lastButton.setBackground(null);
                lastButton = button1;
            }
        });

        button2.addActionListener(e -> {
            cardLayout.show(card, "供应商管理");
            button2.setBackground(Color.GREEN);
            if (lastButton != button2) {
                lastButton.setBackground(null);
                lastButton = button2;
            }
        });
    }


}
