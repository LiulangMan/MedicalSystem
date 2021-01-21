package com.zjw.swing.salesManager;

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
public class SalesMangerFrame extends JPanel {

    @Autowired
    private SaleListPanel saleGoodsPanel;

    @Autowired
    private SaleRecordPanel saleRecordPanel;

    @Autowired
    private SaleStockPanel saleStockPanel;

    private JButton lastButton;

    //绝对布局
    public SalesMangerFrame() {
        super(null);
    }

    private void initFiled(){
        saleGoodsPanel.init();
        saleRecordPanel.init();
        saleStockPanel.init();
    }

    public void refreshAllData() {
        saleGoodsPanel.refreshData();
        saleRecordPanel.refreshData();
        saleStockPanel.refreshData();
    }

    public void init() {

        initFiled();

        this.setSize(IndexConstant.CARD_WIDTH, IndexConstant.CARD_HIGH);
        this.setBackground(Color.yellow);

        //菜单栏
        JPanel menu = new JPanel(new GridLayout(3, 1));
        menu.setSize(100, 800);
        menu.setLocation(0, 0);
        this.add(menu);

        JButton button0 = new JButton("在售药物");
        JButton button1 = new JButton("销售记录");
        JButton button2 = new JButton("销售调整");

        menu.add(button0);
        menu.add(button1);
        menu.add(button2);

        //三个子菜单的卡片面板
        CardLayout cardLayout = new CardLayout();
        JPanel card = new JPanel(cardLayout);
        card.setSize(IndexConstant.CARD_WIDTH - menu.getWidth(), IndexConstant.CARD_HIGH);
        card.setLocation(menu.getWidth(),0);
        card.add(saleGoodsPanel,"在售药物");
        card.add(saleRecordPanel,"销售记录");
        card.add(saleStockPanel,"药物管理");
        this.add(card);

        lastButton = button0;

        /*监听*/
        button0.addActionListener(e -> {
            cardLayout.show(card,"在售药物");
            button0.setBackground(Color.GREEN);
            if (lastButton != button0) {
                lastButton.setBackground(null);
                lastButton = button0;
            }
        });

        button1.addActionListener(e -> {
            cardLayout.show(card,"销售记录");
            button1.setBackground(Color.GREEN);
            if (lastButton != button1) {
                lastButton.setBackground(null);
                lastButton = button1;
            }
        });

        button2.addActionListener(e -> {
            cardLayout.show(card,"药物管理");
            button2.setBackground(Color.GREEN);
            if (lastButton != button2) {
                lastButton.setBackground(null);
                lastButton = button2;
            }
        });
    }

}
