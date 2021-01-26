package com.zjw.swing.stockManager;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.Goods;
import com.zjw.service.GoodService;
import com.zjw.service.SupplierService;
import com.zjw.swing.message.MessageShowByText;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.config.StaticConfiguration;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.DataUtils;
import com.zjw.utils.OptionUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/10 12:25
 */
@Component
public class StockListPanel extends ImageJPanel {


    private DefaultJTable stockTable;

    private DefaultJTable selectTable;

    @Autowired
    private GoodService goodService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private StockOrderFrame orderFrame;

    @Autowired
    private StockGoodsEditFrame stockGoodsEditFrame;

    public StockListPanel() {
        super(null, "/images/index/t7.jpg");
    }

    public void init() {

        //表格
        stockTable = new DefaultJTable(
                new Object[]{"药品ID", "供应商ID", "药名", "类型", "描述", "采购价", "库存"},
                new DefaultTableModel());

        stockTable.getJScrollPane().setLocation(0, 0);
        stockTable.getJScrollPane().setSize(1100, 600);
        this.add(stockTable.getJScrollPane());

        //搜索栏
        String[] type = {"药品ID", "药名", "描述", "供应商ID"};
        JComboBox<String> typeList = new JComboBox<>(type);
        typeList.setSize(80, 30);
        typeList.setLocation(10, 600);
        this.add(typeList);

        JTextField searchField = new JTextField();
        searchField.setSize(300, 30);
        searchField.setLocation(100, 600);
        this.add(searchField);

        JButton searchButton = new JButton("搜索");
        searchButton.setSize(100, 30);
        searchButton.setLocation(400, 600);
        this.add(searchButton);

        selectTable = new DefaultJTable(new Object[]{"ID", "药名", "采购价", "类型"}, new DefaultTableModel());
        selectTable.getJScrollPane().setSize(600, 150);
        selectTable.getJScrollPane().setLocation(10, 650);
        this.add(selectTable.getJScrollPane());

        //刷新按钮
        JButton refreshButton = new JButton("刷新");
        refreshButton.setSize(100, 30);
        refreshButton.setLocation(1000, 650);
        this.add(refreshButton);

        //明细按钮
        JButton descriptionButton = new JButton("明细");
        descriptionButton.setSize(100, 30);
        descriptionButton.setLocation(1000, 700);
        this.add(descriptionButton);

        //销售按钮
        JButton stockButton = new JButton("采购");
        stockButton.setSize(100, 30);
        stockButton.setLocation(1000, 750);
        this.add(stockButton);

        //添加
        JButton addButton = new JButton("添加");
        addButton.setSize(100, 30);
        addButton.setLocation(620, 650);
        this.add(addButton);

        //移除
        JButton removeButton = new JButton("移除");
        removeButton.setSize(100, 30);
        removeButton.setLocation(620, 700);
        this.add(removeButton);

        //清空
        JButton cancelButton = new JButton("清空");
        cancelButton.setSize(100, 30);
        cancelButton.setLocation(620, 750);
        this.add(cancelButton);

        //弹出菜单
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem addStockButton = new JMenuItem("新增");
        JMenuItem editButton = new JMenuItem("编辑");
        JMenuItem deleteButton = new JMenuItem("移除");
        jPopupMenu.add(editButton);
        jPopupMenu.add(deleteButton);
        jPopupMenu.add(addStockButton);


        /*监听*/

        stockTable.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    MessageShowByText.show("药品描述",
                            (String) stockTable.getValueAt(stockTable.getSelectedRow(), 4),
                            new Font(null, Font.PLAIN, 20)
                    );
                }
                if (e.isMetaDown()) {
                    jPopupMenu.show(stockTable, e.getX(), e.getY());
                }
            }
        });

        editButton.addActionListener(e -> {
            int row = stockTable.getSelectedRow();
            Integer id = (Integer) stockTable.getValueAt(row, 0);
            StaticConfiguration.addThreadPoolTask(() -> stockGoodsEditFrame.run(StaticConfiguration.getStockGoodsInCache(id)));
        });

        deleteButton.addActionListener(e -> {
            int row = stockTable.getSelectedRow();
            Integer id = (Integer) stockTable.getValueAt(row, 0);
            if (StaticConfiguration.getGoodsInCache(id) != null) {
                MessageShows.ShowMessageText(this, null, "药品正在销售，无法移除");
                return;
            }
            boolean b = MessageShows.ShowMessageAboutMakeSure(this, "确认移除该药品？");
            if (!b) return;
            goodService.deleteByStockGoodsId(id);
            OptionUtils.recordCurrentOption("移除了采购药物 id:" + id);
            MessageShows.ShowMessageText(this, null, "删除成功");
            refreshData();
        });

        //清空
        cancelButton.addActionListener(e -> {
            StaticConfiguration.clearSelectStockGoods();
            refreshSelectTable();
        });

        //添加
        addButton.addActionListener(e -> {
            int[] selectedRows = stockTable.getSelectedRows();

            for (int es : selectedRows) {
                Object id = stockTable.getValueAt(es, 0);
                Goods goods = StaticConfiguration.getStockGoodsInCache((Integer) id);
                StaticConfiguration.addSelectStockGoods(goods);
            }

            //刷新
            refreshSelectTable();
        });

        //移除
        removeButton.addActionListener(e -> {
            int[] rows = selectTable.getSelectedRows();
            for (int row : rows) {
                Object id = selectTable.getValueAt(row, 0);
                Goods goods = StaticConfiguration.getStockGoodsInCache((Integer) id);
                StaticConfiguration.removeSelectStockGoods(goods);
            }
            refreshSelectTable();

        });

        //刷新数据
        refreshButton.addActionListener(e -> {
            refreshData();
        });

        //采购
        stockButton.addActionListener(e -> {
            StaticConfiguration.transToStockOrderGoods();
            refreshSelectTable();

            //进入订单页面
            StaticConfiguration.addThreadPoolTask(() -> orderFrame.run());

        });

        //查询
        searchButton.addActionListener(e -> {
            //0:id
            //1:药名
            //2:描述
            //3:供应商ID
            int index = typeList.getSelectedIndex();
            String text = searchField.getText();

            if (text.equals("")) {
                refreshData();
                return;
            }

            if (index == 0) {
                try {
                    int id = Integer.parseInt(text);
                    refreshDataForId(id);
                    return;
                } catch (NumberFormatException ex) {
                    MessageShows.ShowMessageText(this, null, "参数错误");
                }
            }

            if (index == 1) {
                refreshDataForName(text);
                return;
            }

            if (index == 2) {
                refreshDataForDescription(text);
                return;
            }

            if (index == 3) {
                try {
                    int id = Integer.parseInt(text);
                    refreshDataForSupplierId(id);
                } catch (NumberFormatException ex) {
                    MessageShows.ShowMessageText(this, null, "参数错误");
                }
            }
        });

        //描述
        descriptionButton.addActionListener(e -> {
            MessageShowByText.show("药品描述",
                    (String) stockTable.getValueAt(stockTable.getSelectedRow(), 4),
                    new Font(null, Font.PLAIN, 20)
            );
        });

        //添加采购组
        addStockButton.addActionListener(e -> {
            StaticConfiguration.addThreadPoolTask(new Runnable() {
                @Override
                public void run() {
                    stockGoodsEditFrame.run(null);
                }
            });
        });
    }

    private void refreshDataForSupplierId(Integer id) {

    }

    private void refreshDataForDescription(String text) {
        List<Goods> goodsList = DataUtils.queryForGoodsDescriptionInCache(StaticConfiguration.getStockGoodsCache(), text);
        stockTable.refreshData(DataUtils.StockGoodsToArray(goodsList));

    }

    private void refreshDataForName(String text) {
        List<Goods> goodsList = DataUtils.queryForGoodsNameInCache(StaticConfiguration.getStockGoodsCache(), text);
        stockTable.refreshData(DataUtils.StockGoodsToArray(goodsList));
    }

    private void refreshDataForId(int id) {
        Goods goods = DataUtils.queryForGoodsIdInCache(StaticConfiguration.getStockGoodsCache(), id);

        stockTable.refreshData((new Object[][]{
                {goods.getGoodId(),
                        goods.getGoodId(),
                        goods.getGoodName(),
                        goods.getGoodType() == IndexConstant.PRESCRIPTION_TYPE ? "处方" : "非处方",
                        goods.getGoodText(),
                        goods.getGoodMoney(),
                        goods.getGoodStock(),
                }
        }));
    }

    //刷新选择区
    private void refreshSelectTable() {
        Object[][] data = DataUtils.SelectGoodsListToObjectArray(StaticConfiguration.getSelectStockGoods());
        this.selectTable.refreshData(data);
    }

    //刷新数据
    public void refreshData() {
        List<Goods> stockList = goodService.queryAllStockGoods();

        //更新缓存
        StaticConfiguration.refreshStockGoodsCache(stockList);
        Object[][] data = DataUtils.StockGoodsToArray(stockList);
        this.stockTable.refreshData(data);
    }
}
