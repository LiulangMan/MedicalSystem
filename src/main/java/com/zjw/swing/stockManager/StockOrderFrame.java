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
import com.zjw.swing.utils.MySwingUtils;
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
        moreButton.setLocation(1100, 810);
        orderPanel.add(moreButton);

        JButton lessButton = new JButton("-");
        lessButton.setFont(new Font(null, Font.PLAIN, 25));
        lessButton.setSize(100, 30);
        lessButton.setLocation(1100, 860);
        orderPanel.add(lessButton);

        JButton numberButton = new JButton("填入");
        numberButton.setSize(100, 30);
        numberButton.setLocation(1100, 910);
        orderPanel.add(numberButton);

        JButton okButton = new JButton("提交");
        okButton.setSize(100, 30);
        okButton.setLocation(1300, 810);
        orderPanel.add(okButton);

        //总额
        JLabel totalMoney = new JLabel("总额: 0 元");
        totalMoney.setFont(new Font(null, Font.PLAIN, 25));
        totalMoney.setSize(300, 30);
        totalMoney.setLocation(800, 810);
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

            JFrame temp = this;
            SwingWorker<Integer, Object> worker = new SwingWorker<Integer, Object>() {
                @Override
                protected void done() {

                    MySwingUtils.ProgressBar.closeProgressBar();
                    try {
                        if (get() == 0) {
                            MessageShows.ShowMessageText(temp, null, "提交成功");
                            //释放资源
                            temp.setVisible(false);
                            temp.dispose();
                        } else {
                            MessageShows.ShowMessageText(temp, null, "提交失败");
                        }
                    } catch (Exception e1) {
                        MessageShows.ShowMessageText(temp, null, "提交失败");
                    }
                }

                @Override
                protected Integer doInBackground() throws Exception {
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
                        //重新刷新
                        saleListPanel.refreshData();
                        stockRecordPanel.refreshData();
                        saleStockPanel.refreshData();
                        stockListPanel.refreshData();
                        StaticConfiguration.addStockGoodsOrderCache(order);
                        return 0;
                    } catch (Exception ex) {
                        return 1;
                    }
                }
            };

            MySwingUtils.ProgressBar.showProgressBar("正在处理");
            worker.execute();

        });

        numberButton.addActionListener(e -> {
            String num = JOptionPane.showInputDialog(
                    this,
                    "请输入销售数量",
                    ""
            );

            if (num == null || num.equals("")) {
                return;
            }
            try {
                int result = Integer.parseInt(num);
                if (result < 0) {
                    MessageShows.ShowMessageText(this, null, "参数错误");
                    return;
                }
                int[] rows = orderTable.getSelectedRows();
                for (int row : rows) {
                    Integer varId = (Integer) orderTable.getValueAt(row, 0);
                    Goods varGood = StaticConfiguration.getStockGoodsInCache(varId);
                    int varCnt = Math.min(Integer.MAX_VALUE, result);
                    orderTable.setValueAt(varCnt, row, 4);
                    orderTable.setValueAt(varCnt * varGood.getGoodMoney(), row, 5);
                }

                this.refreshTotalMoney(totalMoney, orderTable);

            } catch (NumberFormatException ex) {
                MessageShows.ShowMessageText(this, null, "参数错误");
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
