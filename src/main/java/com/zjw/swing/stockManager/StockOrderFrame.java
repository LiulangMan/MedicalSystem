package com.zjw.swing.stockManager;

import com.zjw.domain.Goods;
import com.zjw.domain.StockOrder;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import com.zjw.service.StockOrderService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.salesManager.OrderFrame;
import com.zjw.swing.salesManager.SaleListPanel;
import com.zjw.swing.salesManager.SaleStockPanel;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.config.StaticConfiguration;
import com.zjw.utils.DataUtils;
import com.zjw.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/11 12:54
 */
@Component
public class StockOrderFrame extends JFrame {

    @Autowired
    private StockOrderService orderService;

    @Autowired
    private SaleListPanel saleListPanel;

    @Autowired
    private StockRecordPanel stockRecordPanel;

    @Autowired
    private SaleStockPanel saleStockPanel;

    @Autowired
    private StockListPanel stockListPanel;

    private double total;

    private Object[] colName = {"ID", "药名", "类型", "单价", "数量", "总计"};

    public void init() {
        this.setSize(1500, 1000);
        this.setLocationRelativeTo(null);
        this.setTitle("订单系统");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void run() {

        init();

        //订单板
        JPanel orderPanel = new ImageJPanel(null, "/images/index/t7.jpg");
        this.setContentPane(orderPanel);

        orderPanel.setBackground(Color.CYAN);
        DefaultJTable orderTable = new DefaultJTable(colName, new DefaultTableModel()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        orderTable.getJScrollPane().setSize(1500, 800);
        orderTable.getJScrollPane().setLocation(0, 0);
        orderPanel.add(orderTable.getJScrollPane());

        //加载订单数据
        Map<Integer, Goods> goodsMap = StaticConfiguration.getStockOrderGoods();
        orderTable.refreshData(DataUtils.OrderStockGoodsListToObjectArray(new ArrayList<>(goodsMap.values())));

        JButton moreButton = new JButton("+");
        moreButton.setFont(new Font(null, Font.PLAIN, 25));
        moreButton.setSize(100, 30);
        moreButton.setLocation(1300, 810);
        orderPanel.add(moreButton);

        JButton lessButton = new JButton("-");
        lessButton.setFont(new Font(null, Font.PLAIN, 25));
        lessButton.setSize(100, 30);
        lessButton.setLocation(1300, 860);
        orderPanel.add(lessButton);

        JButton okButton = new JButton("提交");
        okButton.setSize(100, 30);
        okButton.setLocation(1300, 910);
        orderPanel.add(okButton);

        //总额
        JLabel totalMoney = new JLabel("总额: 0 元");
        totalMoney.setFont(new Font(null, Font.PLAIN, 25));
        totalMoney.setSize(200, 30);
        totalMoney.setLocation(1100, 810);
        orderPanel.add(totalMoney);

        //刷新总额
        refreshTotalMoney(totalMoney, orderTable);
        this.setVisible(true);

        /*监听*/
        moreButton.addActionListener(e -> {
            int[] rows = orderTable.getSelectedRows();
            for (int row : rows) {
                int cnt = Integer.parseInt(String.valueOf(orderTable.getValueAt(row, 4)));
                int id = Integer.parseInt(String.valueOf(orderTable.getValueAt(row, 0)));
                double price = StaticConfiguration.getStockGoodsInCache(id).getGoodMoney();
                orderTable.setValueAt(++cnt, row, 4);
                orderTable.setValueAt(cnt * price, row, 5);
            }

            refreshTotalMoney(totalMoney, orderTable);

        });

        lessButton.addActionListener(e -> {
            int[] rows = orderTable.getSelectedRows();
            for (int row : rows) {
                int cnt = Integer.parseInt(String.valueOf(orderTable.getValueAt(row, 4)));
                int id = Integer.parseInt(String.valueOf(orderTable.getValueAt(row, 0)));
                double price = StaticConfiguration.getStockGoodsInCache(id).getGoodMoney();
                orderTable.setValueAt(Math.max(0, --cnt), row, 4);
                orderTable.setValueAt(Math.max(0, cnt * price), row, 5);
            }
            refreshTotalMoney(totalMoney, orderTable);
        });

        okButton.addActionListener(e -> {
            //提取信息
            java.util.List<GoodsIdAndGoodsCntForOrder> list = new ArrayList<>();
            for (int i = 0; i < orderTable.getRowCount(); i++) {
                int id = Integer.parseInt(String.valueOf(orderTable.getValueAt(i, 0)));
                int cnt = Integer.parseInt(String.valueOf(orderTable.getValueAt(i, 4)));
                list.add(new GoodsIdAndGoodsCntForOrder(id, cnt));

                StaticConfiguration.getStockOrderGoods().get(id).setGoodStock(cnt);
            }

            //创建采购订单
            StockOrder order = new StockOrder();
            order.setStockId(RandomUtils.randomIdentity());
            order.setStockMoney(total);
            order.setGoodsIdMap(list);
            order.setStockEmploy(StaticConfiguration.getEmploy().getName());
            order.setStockTime(new Date());

            //提交订单
            try {
                orderService.insert(order);
                MessageShows.SuccessMessage(this, "成功");
                //重新刷新
                saleListPanel.refreshData();
                StaticConfiguration.addStockGoodsOrderCache(order);
                stockRecordPanel.refreshData();
                saleStockPanel.refreshData();
                stockListPanel.refreshData();

                //释放资源
                this.setVisible(false);
                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void refreshTotalMoney(JLabel totalMoney, JTable table) {
        int count = table.getRowCount();
        total = 0;
        for (int i = 0; i < count; i++) {
            double v = Double.parseDouble(String.valueOf(table.getValueAt(i, 5)));
            total += v;
        }
        totalMoney.setText("总额: " + total + " 元");
    }

    public static void main(String[] args) {
        new OrderFrame().run();
    }
}
