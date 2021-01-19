package com.zjw.swing.salesManager;

import com.zjw.config.StaticConfiguration;
import com.zjw.domain.Goods;
import com.zjw.service.GoodService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.stockManager.StockListPanel;
import com.zjw.swing.utils.ImageJPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/16 16:44
 */
@Component
public class SaleGoodsEditFrame extends JFrame {
    @Autowired
    private GoodService goodService;

    @Autowired
    private SaleListPanel saleListPanel;

    @Autowired
    private SaleStockPanel saleStockPanel;

    @Autowired
    private StockListPanel stockListPanel;

    @Autowired
    private SaleStockEditFrame saleStockEditFrame;

    public void run(Goods goods) {

        ImageJPanel panel = new ImageJPanel(null, "/images/index/t7.jpg");

        this.setTitle("药物信息录入");
        this.setContentPane(panel);
        this.setSize(1500, 1000);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        //id
        JTextField id = new JTextField();
        id.setBounds(100, 10, 1000, 30);
        id.setEditable(false);
        panel.add(id);

        JLabel idF = new JLabel("ID");
        idF.setBounds(10, 10, 80, 30);
        panel.add(idF);


        //name
        JTextField name = new JTextField();
        name.setBounds(100, 60, 1000, 30);
        panel.add(name);

        JLabel nameF = new JLabel("药名");
        nameF.setBounds(10, 60, 80, 30);
        panel.add(nameF);

        //type
        JComboBox<String> typeList = new JComboBox<>(new String[]{"处方", "非处方"});
        typeList.setBounds(100, 110, 1000, 30);
        typeList.setEnabled(false);
        panel.add(typeList);

        JLabel typeListF = new JLabel("类型");
        typeListF.setBounds(10, 100, 80, 30);
        panel.add(typeListF);

        //描述
        JTextPane descriptionText = new JTextPane();
        descriptionText.setFont(new Font(null, Font.PLAIN, 25));
        JScrollPane scrollPane = new JScrollPane(descriptionText);
        scrollPane.setBounds(100, 160, 1000, 300);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        JLabel descriptionF = new JLabel("描述");
        descriptionF.setBounds(10, 160, 80, 30);
        panel.add(descriptionF);

        //价格
        JTextField price = new JTextField();
        price.setBounds(100, 510, 1000, 30);
        panel.add(price);

        JLabel priceF = new JLabel("销售价(元)");
        priceF.setBounds(10, 510, 80, 30);
        panel.add(priceF);

        //供应商
        JComboBox<Object> supplierList = new JComboBox<>(new Integer[]{goods.getSupplierId()});
        supplierList.setBounds(100, 550, 1000, 30);
        supplierList.setEnabled(false);
        panel.add(supplierList);

        JLabel supplierF = new JLabel("供应商");
        supplierF.setBounds(10, 550, 80, 30);
        panel.add(supplierF);


        //价格
        JTextField stock = new JTextField();
        stock.setEditable(false);
        stock.setBounds(100, 600, 1000, 30);
        panel.add(stock);

        JLabel stockF = new JLabel("库存");
        stockF.setBounds(10, 600, 80, 30);
        panel.add(stockF);


        JButton okButton = new JButton("确认修改");
        okButton.setBounds(100, 650, 200, 30);
        panel.add(okButton);

        JButton deleteButton = new JButton("下架药品");
        deleteButton.setBounds(400, 650, 200, 30);
        panel.add(deleteButton);

        JButton stockButton = new JButton("销售数量");
        stockButton.setBounds(700, 650, 200, 30);
        panel.add(stockButton);


        //填入原始数据
        id.setText(String.valueOf(goods.getGoodId()));
        name.setText(goods.getGoodName());
        descriptionText.setText(goods.getGoodText());
        price.setText(String.valueOf(goods.getGoodMoney()));
        typeList.setSelectedIndex(goods.getGoodType());
        stock.setText(String.valueOf(goods.getGoodStock()));


        setVisible(true);

        okButton.addActionListener(e -> {
            try {
                String description = descriptionText.getText();
                double priceText = Double.parseDouble(price.getText());
                int stockText = Integer.parseInt(stock.getText());

                goods.setGoodStock(stockText);
                goods.setGoodMoney(priceText);
                goods.setGoodText(description);

                goodService.updateByGoodsId(goods);

                MessageShows.ShowMessageText(this, null, "修改成功");
                saleListPanel.refreshData();
                this.setVisible(false);
                this.dispose();
            } catch (NumberFormatException ex) {
                MessageShows.ShowMessageText(this, null, "错误参数");
                ex.printStackTrace();
            }
        });

        deleteButton.addActionListener(e -> {
            boolean b = MessageShows.ShowMessageAboutDeleteGoods(this);
            if (!b) return;
            goodService.deleteByGoodsId(goods.getGoodId());
            //更新库存
            Goods stockGoods = StaticConfiguration.getStockGoodsInCache(goods.getGoodId());
            stockGoods.setGoodStock(stockGoods.getGoodStock() + goods.getGoodStock());
            goodService.updateStockGoodsById(stockGoods);
            MessageShows.ShowMessageText(this, null, "下架成功");
            //更新列表
            saleListPanel.refreshData();
            stockListPanel.refreshData();
            saleStockPanel.refreshData();
            this.setVisible(false);
            this.dispose();
        });

        stockButton.addActionListener(e -> {
            int idText = Integer.parseInt(id.getText());
            Goods var1 = StaticConfiguration.getGoodsInCache(idText);
            Goods var2 = StaticConfiguration.getStockGoodsInCache(idText);
            StaticConfiguration.addThreadPoolTask(new Runnable() {
                @Override
                public void run() {
                    saleStockEditFrame.run(var1, var2);
                }
            });
        });
    }
}
