package com.zjw.swing.salesManager;

import com.zjw.config.StaticConfiguration;
import com.zjw.domain.Goods;
import com.zjw.service.GoodService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.stockManager.StockListPanel;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.utils.DataUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/7 0:15
 */
@Component
public class SaleStockPanel extends JPanel {

    @Autowired
    private GoodService goodService;

    @Autowired
    private SaleListPanel saleListPanel;

    @Autowired
    private StockListPanel stockListPanel;

    @Autowired
    private SaleStockEditFrame saleStockEditFrame;

    private DefaultJTable table;

    public SaleStockPanel() {
        super(null);
    }

    public void init() {

        //表格
        table = new DefaultJTable(new Object[]{"ID", "药名", "在售数量", "仓库数量"}, new DefaultTableModel());
        table.getJScrollPane().setBounds(0, 0, 1100, 600);
        this.add(table.getJScrollPane());

        refreshData();

        //弹出菜单
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem editMenu = new JMenuItem("调整");
        jPopupMenu.add(editMenu);

        this.setVisible(true);

        /*监听*/
        table.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isMetaDown()) {
                    jPopupMenu.show(table, e.getX(), e.getY());
                }
            }
        });

        editMenu.addActionListener(e -> {
            Integer id = (Integer) table.getValueAt(table.getSelectedRow(), 0);
            Goods goods = StaticConfiguration.getGoodsInCache(id);
            Goods stock = StaticConfiguration.getStockGoodsInCache(id);
            if (goods != null && stock != null) {
                StaticConfiguration.addThreadPoolTask(new Runnable() {
                    @Override
                    public void run() {
                        saleStockEditFrame.run(goods, stock);
                    }
                });
            } else if (goods == null) {
                boolean b = MessageShows.ShowMessageAboutOfferSale(this);
                if (!b) return;
                //上架
                Goods temp = new Goods(stock);
                temp.setGoodStock(0);
                goodService.insert(temp);
                MessageShows.ShowMessageText(this, null, "上架成功");
                this.refreshData();
                saleListPanel.refreshData();
            }
        });
    }

    public void refreshData() {
        List<Goods> stockGoods = goodService.queryAllStockGoods();
        List<Goods> goodsList = goodService.queryAll();
        Object[][] data = DataUtils.SaleStockToArray(goodsList, stockGoods);
        table.refreshData(data);
    }
}
