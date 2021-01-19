package com.zjw.swing.stockManager;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.Goods;
import com.zjw.domain.StockOrder;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import com.zjw.service.StockOrderService;
import com.zjw.swing.message.MessageShowByTable;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.config.StaticConfiguration;
import com.zjw.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/10 12:26
 */
@Component
public class StockRecordPanel extends JPanel {

    @Autowired
    private StockOrderService stockOrderService;

    private DefaultJTable recordTable;

    public StockRecordPanel() {
        super(null);
    }

    public void init() {

        //记录表
        recordTable = new DefaultJTable(new Object[]{"采购ID", "采购人", "采购总价", "采购时间"}, new DefaultTableModel());
        recordTable.getJScrollPane().setSize(1100, 600);
        recordTable.getJScrollPane().setLocation(0, 0);
        this.add(recordTable.getJScrollPane());

        //刷新数据
        List<StockOrder> stockOrders = stockOrderService.queryAll();
        recordTable.refreshData(DataUtils.StockGoodsOrderToArray(stockOrders));
        //加载采购记录信息
        StaticConfiguration.refreshStockGoodsOrderCache(stockOrders);


        //明细按钮
        JButton descriptionButton = new JButton("明细");
        descriptionButton.setSize(100, 30);
        descriptionButton.setLocation(1000, 700);
        this.add(descriptionButton);

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
    }


    public void refreshRecordTable() {
        Collection<StockOrder> values = StaticConfiguration.getStockGoodsOrderCache().values();
        ArrayList<StockOrder> list = new ArrayList<>(values);
        list.sort((o1, o2) -> o2.getStockTime().compareTo(o1.getStockTime()));
        recordTable.refreshData(DataUtils.StockGoodsOrderToArray(list));
    }
}
