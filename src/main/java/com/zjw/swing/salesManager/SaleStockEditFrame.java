package com.zjw.swing.salesManager;

import com.zjw.config.FontConfiguration;
import com.zjw.domain.Goods;
import com.zjw.service.GoodService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.stockManager.StockListPanel;
import com.zjw.swing.utils.ImageJPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/17 19:44
 */
@Component
public class SaleStockEditFrame extends JFrame {

    @Autowired
    private GoodService goodService;

    @Autowired
    private SaleListPanel saleListPanel;

    @Autowired
    private StockListPanel stockListPanel;

    @Autowired
    private SaleStockPanel saleStockPanel;

    public void run(Goods goods, Goods stockGoods) {
        this.setSize(800, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("销售库存设置: ID=" + goods.getGoodId() + " Name=" + goods.getGoodName());
        this.setResizable(false);

        ImageJPanel panel = new ImageJPanel(null, "/images/index/t5.jpg");
        this.setContentPane(panel);

        JTextField sale = new JTextField();
        sale.setBounds(100, 100, 200, 30);
        sale.setEditable(false);
        panel.add(sale);

        JLabel saleF = new JLabel("销售量");
        saleF.setHorizontalAlignment(SwingConstants.CENTER);
        saleF.setBounds(100, 70, 200, 30);
        panel.add(saleF);

        JTextField stock = new JTextField();
        stock.setEditable(false);
        stock.setBounds(300, 100, 200, 30);
        panel.add(stock);

        JLabel stockF = new JLabel("库存量");
        stockF.setHorizontalAlignment(SwingConstants.CENTER);
        stockF.setBounds(300, 70, 200, 30);
        panel.add(stockF);

        //数量按钮
        Font font = FontConfiguration.getFont(null, 25);
        JButton moreButton = new JButton("+");
        moreButton.setFont(font);
        moreButton.setBounds(500, 100, 50, 30);
        panel.add(moreButton);

        JButton lessButton = new JButton("-");
        lessButton.setFont(font);
        lessButton.setBounds(550, 100, 50, 30);
        panel.add(lessButton);

        JButton numButton = new JButton("填入");
        numButton.setBounds(600, 100, 100, 30);
        panel.add(numButton);

        //确认按钮
        JButton okButton = new JButton("确认");
        okButton.setBounds(700, 100, 100, 30);
        panel.add(okButton);

        //数据
        sale.setText(String.valueOf(goods.getGoodStock()));
        stock.setText(String.valueOf(stockGoods.getGoodStock()));


        this.setVisible(true);

        /*监听*/
        //总数
        int total = goods.getGoodStock() + stockGoods.getGoodStock();

        moreButton.addActionListener(e -> {
            int newSale = Math.min(total, goods.getGoodStock() + 1);
            int newStock = total - newSale;
            sale.setText(String.valueOf(newSale));
            stock.setText(String.valueOf(newStock));
            goods.setGoodStock(newSale);
            stockGoods.setGoodStock(newStock);
        });

        lessButton.addActionListener(e -> {
            int newSale = Math.max(0, goods.getGoodStock() - 1);
            int newStock = total - newSale;
            sale.setText(String.valueOf(newSale));
            stock.setText(String.valueOf(newStock));
            goods.setGoodStock(newSale);
            stockGoods.setGoodStock(newStock);
        });

        numButton.addActionListener(e -> {
            String result = JOptionPane.showInputDialog(this, "输入调整到销售的数量", "");
            if (result == null || result.equals("")) {
                return;
            }
            try {
                int newSale = Math.min(Integer.parseInt(result), stockGoods.getGoodStock());
                sale.setText(String.valueOf(newSale));
                stock.setText(String.valueOf(total - newSale));
                goods.setGoodStock(newSale);
                stockGoods.setGoodStock(total - newSale);
            } catch (NumberFormatException ex) {
                MessageShows.ShowMessageText(this, null, "参数错误");
            }
        });

        okButton.addActionListener(e -> {
            goodService.updateByGoodsId(goods);
            goodService.updateStockGoodsById(stockGoods);

            MessageShows.ShowMessageText(this, null, "调整成功");

            saleListPanel.refreshData();
            stockListPanel.refreshData();
            saleStockPanel.refreshData();
            this.setVisible(false);
            this.dispose();
        });
    }
}
