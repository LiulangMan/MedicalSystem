package com.zjw.swing.salesManager;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.Goods;
import com.zjw.domain.Order;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import com.zjw.service.OrderService;
import com.zjw.swing.message.MessageShowByTable;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.config.StaticConfiguration;
import com.zjw.utils.DataUtils;
import com.zjw.utils.PrintUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/7 0:14
 */
@Component
@Log4j2
public class SaleRecordPanel extends ImageJPanel {

    @Autowired
    private OrderService orderService;

    private DefaultJTable recordTable;

    public SaleRecordPanel() {
        super(null, "/images/login/t4.jpg");
    }

    public void init() {
        this.setBackground(Color.CYAN);
        this.setSize(1100, 800);


        recordTable = new DefaultJTable(
                new Object[]{"订单ID", "顾客账户", "顾客姓名", "顾客电话", "出售人", "金额", "创建时间"},
                new DefaultTableModel());
        recordTable.getJScrollPane().setSize(1100, 600);
        recordTable.getJScrollPane().setLocation(0, 0);
        recordTable.setOpaque(false);
        recordTable.getJScrollPane().setOpaque(false);
        recordTable.getJScrollPane().getViewport().setOpaque(false);
        this.add(recordTable.getJScrollPane());

        JButton fullButton = new JButton("药品明细");
        fullButton.setSize(100, 30);
        fullButton.setLocation(950, 650);
        this.add(fullButton);

        JButton printButton = new JButton("打印订单");
        printButton.setSize(100, 30);
        printButton.setLocation(950, 700);
        this.add(printButton);

        /*监听*/
        fullButton.addActionListener(e -> {
            int row = recordTable.getSelectedRow();

            String orderId = String.valueOf(recordTable.getValueAt(row, 0));
            List<GoodsIdAndGoodsCntForOrder> goodsIdMap = StaticConfiguration.getOrderInCache(orderId).getGoodsIdMap();
            Object[][] objects = new Object[goodsIdMap.size()][7];
            Object[] colName = {"ID", "药名", "描述", "类型", "单价", "数量", "总价"};
            for (int i = 0; i < goodsIdMap.size(); i++) {
                Goods goods = StaticConfiguration.getGoodsInCache(goodsIdMap.get(i).getGoodsId());
                Integer cnt = goodsIdMap.get(i).getGoodsCnt();
                objects[i][0] = goods.getGoodId();
                objects[i][1] = goods.getGoodName();
                objects[i][2] = goods.getGoodText();
                objects[i][3] = goods.getGoodType() == IndexConstant.PRESCRIPTION_TYPE ? "处方药" : "非处方药";
                objects[i][4] = goods.getGoodMoney();
                objects[i][5] = cnt;
                objects[i][6] = goods.getGoodMoney() * cnt;
            }

            //展示商品信息
            MessageShowByTable.show(colName, objects);
        });

        recordTable.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    fullButton.doClick();
                }
            }
        });

        printButton.addActionListener(e -> {
            boolean b = MessageShows.ShowMessageAboutMakeSure(this, "确认打印订单？");
            if (!b) return;

            int row = recordTable.getSelectedRow();
            String orderId = (String) recordTable.getValueAt(row, 0);
            Order order = StaticConfiguration.getOrderInCache(orderId);

            //构建打印订单字符串
            StringBuilder builder = new StringBuilder();
            builder.append("医疗销售订单\n")
                    .append("- 订单号:").append(order.getOrderId()).append("\n")
                    .append("- 时间:").append(DataUtils.defaultDataFormat.format(order.getOrderTime())).append("\n")
                    .append("- 销售列表:").append("\n");
            for (GoodsIdAndGoodsCntForOrder o : order.getGoodsIdMap()) {
                Goods goods = StaticConfiguration.getGoodsInCache(o.getGoodsId());
                builder.append("---- ").append(goods.getGoodName())
                        .append(" - 数量:").append(o.getGoodsCnt())
                        .append(" - 金额:").append(goods.getGoodMoney() * o.getGoodsCnt()).append("元").append("\n");
            }
            builder.append("- 总金额:").append(order.getOrderMoney()).append("元").append("\n");
            builder.append("- 顾客:").append(order.getCustomerName());
            log.info(builder.toString());
            try {
                String path = "F://hello.pdf";
                //先创建PDF文件
                PrintUtils.creatPdf(builder.toString(), path);
                //打印PDF文件
                File file = new File(path);
                InputStream fis = new FileInputStream(file);
                fis.read(builder.toString().getBytes());
                //打印
                PrintUtils.Print(fis, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    public void refreshData() {
        List<Order> orders = StaticConfiguration.getEmploy() != null ? orderService.queryAll() :
                orderService.queryAllOnlyCustomerId(StaticConfiguration.getCustomer().getLoginName());
        StaticConfiguration.refreshOrderCache(orders);
        recordTable.refreshData(DataUtils.OrderSimpleInformationToArray(orders));
    }
}
