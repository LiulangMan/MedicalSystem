package com.zjw.swing.stockManager;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.Goods;
import com.zjw.domain.Order;
import com.zjw.domain.StockOrder;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import com.zjw.service.StockOrderService;
import com.zjw.swing.message.MessageShowByTable;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.config.StaticConfiguration;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.DataUtils;
import com.zjw.utils.PrintUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/10 12:26
 */
@Component
public class StockRecordPanel extends ImageJPanel {

    @Autowired
    private StockOrderService stockOrderService;

    private DefaultJTable recordTable;

    public StockRecordPanel() {
        super(null,"/images/index/t7.jpg");
    }

    public void init() {

        //记录表
        recordTable = new DefaultJTable(new Object[]{"采购ID", "采购人", "采购总价", "采购时间"}, new DefaultTableModel());
        recordTable.getJScrollPane().setSize(1100, 600);
        recordTable.getJScrollPane().setLocation(0, 0);
        this.add(recordTable.getJScrollPane());

        //明细按钮
        JButton descriptionButton = new JButton("药品明细");
        descriptionButton.setSize(100, 30);
        descriptionButton.setLocation(950, 650);
        this.add(descriptionButton);

        JButton printButton = new JButton("打印订单");
        printButton.setSize(100, 30);
        printButton.setLocation(950, 700);
        this.add(printButton);

        this.setVisible(true);


        /*监听*/
        descriptionButton.addActionListener(e -> {

            int row = recordTable.getSelectedRow();
            String orderId = String.valueOf(recordTable.getValueAt(row, 0));
            List<GoodsIdAndGoodsCntForOrder> goodsIdMap = StaticConfiguration.getStockGoodsOrderInCache(orderId).getGoodsIdMap();
            Object[][] objects = new Object[goodsIdMap.size()][7];
            Object[] colName = {"ID", "药名", "描述", "类型", "采购价", "数量", "总价"};
            for (int i = 0; i < goodsIdMap.size(); i++) {
                Goods goods = StaticConfiguration.getStockGoodsInCache(goodsIdMap.get(i).getGoodsId());
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
                if (e.getClickCount() == 2){
                    descriptionButton.doClick();
                }
            }
        });

        printButton.addActionListener(e -> {

            boolean b = MessageShows.ShowMessageAboutMakeSure(this, "确认打印订单？");
            if (!b) return;

            int row = recordTable.getSelectedRow();
            String orderId = (String) recordTable.getValueAt(row, 0);
            StockOrder order = StaticConfiguration.getStockGoodsOrderInCache(orderId);

            //构建打印订单字符串
            StringBuilder builder = new StringBuilder();
            builder.append("医疗采购订单\n")
                    .append("- 订单号:").append(order.getStockId()).append("\n")
                    .append("- 时间:").append(DataUtils.defaultDataFormat.format(order.getStockTime())).append("\n")
                    .append("- 销售列表:").append("\n");
            for (GoodsIdAndGoodsCntForOrder o : order.getGoodsIdMap()) {
                Goods goods = StaticConfiguration.getStockGoodsInCache(o.getGoodsId());
                builder.append("---- ").append(goods.getGoodName())
                        .append(" - 数量:").append(o.getGoodsCnt())
                        .append(" - 金额:").append(goods.getGoodMoney() * o.getGoodsCnt()).append("元").append("\n");
            }
            builder.append("- 总金额:").append(order.getStockMoney()).append("元").append("\n");
            builder.append("- 采购人:").append(order.getStockEmploy());
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
        //刷新数据
        List<StockOrder> stockOrders = stockOrderService.queryAll();
        //加载采购记录信息
        StaticConfiguration.refreshStockGoodsOrderCache(stockOrders);
        recordTable.refreshData(DataUtils.StockGoodsOrderToArray(stockOrders));
    }
}
