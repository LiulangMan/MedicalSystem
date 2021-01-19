package com.zjw.swing.salesManager;

import com.zjw.domain.Order;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import com.zjw.service.OrderService;
import com.zjw.swing.message.MessageShows;
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

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/7 15:47
 */
@Component
public class OrderFrame extends JFrame {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SaleListPanel saleGoodsPanel;

    @Autowired
    private SaleRecordPanel saleRecordPanel;

    @Autowired
    private SaleStockPanel saleStockPanel;

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
                return false;
            }
        };


        orderTable.getJScrollPane().setSize(1500, 800);
        orderTable.getJScrollPane().setLocation(0, 0);
        orderPanel.add(orderTable.getJScrollPane());

        //加载订单数据
        orderTable.refreshData(DataUtils.OrderGoodsListToObjectArray(StaticConfiguration.getOrderGoods()));

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

        //客户信息栏
        JLabel clientInformation = new JLabel("顾客信息");
        clientInformation.setSize(200, 30);
        clientInformation.setLocation(10, 810);
        orderPanel.add(clientInformation);

        JTextField customerName = new JTextField();
        customerName.setSize(200, 30);
        customerName.setLocation(70, 860);
        orderPanel.add(customerName);

        JLabel customerNameFlag = new JLabel("姓名");
        customerNameFlag.setSize(50, 30);
        customerNameFlag.setLocation(10, 860);
        orderPanel.add(customerNameFlag);

        JTextField customerPhone = new JTextField();
        customerPhone.setSize(200, 30);
        customerPhone.setLocation(70, 910);
        orderPanel.add(customerPhone);

        JLabel customerPhoneFlag = new JLabel("电话");
        customerPhoneFlag.setSize(50, 30);
        customerPhoneFlag.setLocation(10, 910);
        orderPanel.add(customerPhoneFlag);

        JTextField customerId = new JTextField();
        customerId.setSize(200, 30);
        customerId.setLocation(400, 860);
        orderPanel.add(customerId);

        JLabel customerIdFlag = new JLabel("账号");
        customerIdFlag.setSize(50, 30);
        customerIdFlag.setLocation(340, 860);
        orderPanel.add(customerIdFlag);

        //总额
        JLabel totalMoney = new JLabel("总额: 0 元");
        totalMoney.setFont(new Font(null, Font.PLAIN, 25));
        totalMoney.setSize(200, 30);
        totalMoney.setLocation(1100, 810);
        orderPanel.add(totalMoney);

        //刷新总额
        refreshTotalMoney(totalMoney, orderTable);

        //如果是顾客，刷新信息
        if (StaticConfiguration.getCustomer() != null) {
            customerName.setText(StaticConfiguration.getCustomer().getName());
            customerPhone.setText(StaticConfiguration.getCustomer().getPhone());
            customerId.setText(StaticConfiguration.getCustomer().getLoginName());
            customerId.setEditable(false);
            customerName.setEditable(false);
            customerPhone.setEditable(false);
        }


        this.setVisible(true);

        /*监听*/
        moreButton.addActionListener(e -> {
            int[] rows = orderTable.getSelectedRows();
            for (int row : rows) {
                int cnt = Integer.parseInt(String.valueOf(orderTable.getValueAt(row, 4)));
                int id = Integer.parseInt(String.valueOf(orderTable.getValueAt(row, 0)));
                double price = StaticConfiguration.getGoodsInCache(id).getGoodMoney();
                int stock = StaticConfiguration.getGoodsInCache(id).getGoodStock();
                orderTable.setValueAt(Math.min(++cnt, stock), row, 4);
                orderTable.setValueAt(cnt * price, row, 5);
            }

            refreshTotalMoney(totalMoney, orderTable);

        });

        lessButton.addActionListener(e -> {
            int[] rows = orderTable.getSelectedRows();
            for (int row : rows) {
                int cnt = Integer.parseInt(String.valueOf(orderTable.getValueAt(row, 4)));
                int id = Integer.parseInt(String.valueOf(orderTable.getValueAt(row, 0)));
                double price = StaticConfiguration.getGoodsInCache(id).getGoodMoney();
                orderTable.setValueAt(Math.max(0, --cnt), row, 4);
                orderTable.setValueAt(Math.max(0, cnt * price), row, 5);
            }
            refreshTotalMoney(totalMoney, orderTable);
        });

        okButton.addActionListener(e -> {
            //提取信息
            String customerLoginIdText = customerId.getText();
            String customerNameText = customerName.getText();
            String customerPhoneText = customerPhone.getText();
            java.util.List<GoodsIdAndGoodsCntForOrder> list = new ArrayList<>();
            for (int i = 0; i < orderTable.getRowCount(); i++) {
                int id = Integer.parseInt(String.valueOf(orderTable.getValueAt(i, 0)));
                int cnt = Integer.parseInt(String.valueOf(orderTable.getValueAt(i, 4)));
                list.add(new GoodsIdAndGoodsCntForOrder(id, cnt));
            }

            //创建订单
            Order order = new Order();
            order.setOrderId(RandomUtils.randomIdentity());
            order.setCustomerId(customerLoginIdText);
            order.setCustomerName(customerNameText);
            order.setCustomerPhone(customerPhoneText);
            order.setGoodsIdMap(list);
            if (StaticConfiguration.getEmploy() != null) {
                order.setSaleEmployName(StaticConfiguration.getEmploy().getName());
            }
            order.setOrderMoney(total);
            order.setOrderTime(new Date());

            //提交订单
            try {
                orderService.insert(order);
                MessageShows.SuccessMessage(this, "成功");
                //重新刷新
                saleGoodsPanel.refreshData();
                StaticConfiguration.addOrderInCache(order);
                saleRecordPanel.refreshRecordTable();
                saleStockPanel.refreshData();
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
}
