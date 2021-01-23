package com.zjw.swing.salesManager;

import com.zjw.config.StaticConfiguration;
import com.zjw.domain.Goods;
import com.zjw.domain.Option;
import com.zjw.service.GoodService;
import com.zjw.service.OptionService;
import com.zjw.swing.log.OptionLogPanel;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.stockManager.StockListPanel;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.swing.utils.MySwingUtils;
import com.zjw.utils.DataUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/7 0:15
 */
@Component
@Log4j2
public class SaleStockPanel extends JPanel {

    @Autowired
    private GoodService goodService;

    @Autowired
    private SaleListPanel saleListPanel;

    @Autowired
    private SaleStockEditFrame saleStockEditFrame;

    @Autowired
    private OptionService optionService;

    @Autowired
    private OptionLogPanel optionLogPanel;

    @Autowired
    private StockListPanel stockListPanel;

    private DefaultJTable table;

    public SaleStockPanel() {
        super(null);
    }

    public void init() {

        //表格
        table = new DefaultJTable(new Object[]{"ID", "药名", "在售数量", "仓库数量"}, new DefaultTableModel());
        table.getJScrollPane().setBounds(0, 0, 1100, 600);
        this.add(table.getJScrollPane());

        //弹出菜单
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem editMenu = new JMenuItem("调整");
        JMenuItem offMenu = new JMenuItem("下架");
        jPopupMenu.add(editMenu);
        jPopupMenu.add(offMenu);

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
                StaticConfiguration.addThreadPoolTask(() -> saleStockEditFrame.run(goods, stock));
            } else if (goods == null) {
                boolean b = MessageShows.ShowMessageAboutMakeSure(this, "该商品已下架，是否上架?");
                if (!b) return;
                //上架
                goodService.offShelfGoods(stock);
                Option option = new Option(0, StaticConfiguration.getEmploy().getName(),
                        "上架 Id:" + stock.getGoodId() + "-" + stock.getGoodName()
                        , new Date());

                optionService.insertOption(option);
                this.refreshData();
                saleListPanel.refreshData();
                optionLogPanel.refreshData();
                MessageShows.ShowMessageText(this, null, "上架成功");
            }
        });

        offMenu.addActionListener(e -> {

            boolean b = MessageShows.ShowMessageAboutMakeSure(this, "是否批量下架选中药物?");
            if (!b) return;
            JPanel temp = this;
            SwingWorker<Integer, Object> worker = new SwingWorker<Integer, Object>() {
                @Override
                protected void done() {
                    MySwingUtils.ProgressBar.closeProgressBar();
                    MessageShows.ShowMessageText(temp, null, "下架成功");
                }

                @Override
                protected Integer doInBackground() throws Exception {
                    int[] rows = table.getSelectedRows();
                    log.info("下架id" + Arrays.toString(rows));
                    StringBuilder sb = new StringBuilder();
                    for (int row : rows) {
                        Integer id = (Integer) table.getValueAt(row, 0);
                        Goods goods = StaticConfiguration.getGoodsInCache(id);
                        goodService.offShelfGoods(goods);

                        sb.append("下架id:").append(goods.getGoodId()).append("-").append(goods.getGoodName()).append("\n");
                    }
                    optionService.insertOption(new Option(0, StaticConfiguration.getEmploy().getName(), sb.toString(), new Date()));
                    saleListPanel.refreshData();
                    stockListPanel.refreshData();
                    optionLogPanel.refreshData();
                    return 0;
                }
            };
            MySwingUtils.ProgressBar.showProgressBar("正在下架");
            worker.execute();
        });
    }

    public void refreshData() {
        List<Goods> stockGoods = goodService.queryAllStockGoods();
        List<Goods> goodsList = goodService.queryAll();
        Object[][] data = DataUtils.SaleStockToArray(goodsList, stockGoods);
        table.refreshData(data);
    }
}
