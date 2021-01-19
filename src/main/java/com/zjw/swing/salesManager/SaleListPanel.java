package com.zjw.swing.salesManager;

import com.zjw.config.StaticConfiguration;
import com.zjw.constant.IndexConstant;
import com.zjw.domain.Goods;
import com.zjw.service.GoodService;
import com.zjw.swing.message.MessageShowByText;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.utils.DataUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/7 0:11
 */
@Component
public class SaleListPanel extends JPanel {

    @Autowired
    private GoodService goodService;

    @Autowired
    private OrderFrame orderFrame;

    @Autowired
    private SaleGoodsEditFrame saleGoodsEditFrame;

    private DefaultJTable table;

    private DefaultJTable selectTable;

    //绝对布局
    public SaleListPanel() {
        super(null);
    }


    public void init() {

        //数据面板
        JPanel dataPanel = new JPanel(null);
        dataPanel.setSize(1100, 600);
        dataPanel.setLocation(0, 0);
        dataPanel.setBackground(Color.BLUE);
        this.add(dataPanel);

        //弹出菜单
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem editMenu = new JMenuItem("编辑");
        jPopupMenu.add(editMenu);


        // 创建一个表格，指定 表头 和 所有行数据
        String[] columnNames = {"ID", "药名", "描述", "库存", "单价", "类型"};
        table = new DefaultJTable(columnNames, new DefaultTableModel());
        table.getJScrollPane().setSize(dataPanel.getWidth(), dataPanel.getHeight());
        table.getJScrollPane().setLocation(0, 0);
        dataPanel.add(table.getJScrollPane());

        //刷新数据
        refreshData();

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
        JButton saleButton = new JButton("下单");
        saleButton.setSize(100, 30);
        saleButton.setLocation(1000, 750);
        this.add(saleButton);

        //搜索栏
        String[] type = {"ID", "药名", "描述", "单价"};

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

        //已选择药物

        Object[] selectColName = {"ID", "药名", "单价", "类型"};
        selectTable = new DefaultJTable(selectColName, new DefaultTableModel());
        selectTable.getJScrollPane().setSize(600, 150);
        selectTable.getJScrollPane().setLocation(10, 650);
        this.add(selectTable.getJScrollPane());

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


        /*监听*/

        //清空
        cancelButton.addActionListener(e -> {
            StaticConfiguration.clearSelectGoods();
            refreshSelectTable();
        });

        //添加
        addButton.addActionListener(e -> {
            int[] selectedRows = this.table.getSelectedRows();

            for (int es : selectedRows) {
                Object id = this.table.getValueAt(es, 0);
                Goods goods = StaticConfiguration.getGoodsInCache((Integer) id);

                if (StaticConfiguration.getCustomer() != null && goods.getGoodType() == IndexConstant.PRESCRIPTION_TYPE) {
                    //用户购买，不能自己购买处方药
                    MessageShows.ShowMessageText(this,
                            "failSelect",
                            "不可购买处方药" + " " + goods.getGoodName());
                    continue;
                }
                StaticConfiguration.addSelectGoods(goods);
            }

            //刷新
            refreshSelectTable();
        });

        //移除
        removeButton.addActionListener(e -> {
            int[] rows = selectTable.getSelectedRows();
            for (int row : rows) {
                Object id = selectTable.getValueAt(row, 0);
                Goods goods = StaticConfiguration.getGoodsInCache((Integer) id);
                StaticConfiguration.removeSelectGoods(goods);
            }
            refreshSelectTable();
        });

        //刷新数据
        refreshButton.addActionListener(e -> {
            refreshData();
        });

        //确认
        saleButton.addActionListener(e -> {
            //生成订单列表
            StaticConfiguration.transToOrderGoods();
            refreshSelectTable();
            //进入订单页面
            StaticConfiguration.addThreadPoolTask(
                    () -> orderFrame.run());
        });

        //查询
        searchButton.addActionListener(e -> {
            //0:id
            //1:药名
            //2:描述
            //3:单价:0~price之类的结果
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
                    MessageShows.ErrorInputText(this);
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
                    double price = Double.parseDouble(text);
                    refreshDataForPrice(price);
                } catch (NumberFormatException ex) {
                    MessageShows.ErrorInputText(this);
                }
            }
        });

        descriptionButton.addActionListener(e -> {
            MessageShowByText.show("药品描述",
                    (String) this.table.getValueAt(this.table.getSelectedRow(), 2),
                    new Font(null, Font.PLAIN, 20)
            );
        });

        this.table.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //明细
                    MessageShowByText.show("药品描述",
                            (String) SaleListPanel.this.table.getValueAt(SaleListPanel.this.table.getSelectedRow(), 2),
                            new Font(null, Font.PLAIN, 20)
                    );
                }
                if (e.isMetaDown() && StaticConfiguration.getLoginType() == IndexConstant.LOGIN_TYPE_ADMIN) {
                    //弹出菜单
                    jPopupMenu.show(table, e.getX(), e.getY());
                }
            }
        });

        editMenu.addActionListener(e -> {
            Integer id = (Integer) table.getValueAt(this.table.getSelectedRow(), 0);

            Goods goods = StaticConfiguration.getGoodsInCache(id);
            StaticConfiguration.addThreadPoolTask(new Runnable() {
                @Override
                public void run() {
                    saleGoodsEditFrame.run(goods);
                }
            });
        });
    }

    /**
     * 一系列刷新方法
     */
    public void refreshData() {

        //实时查询一遍
        List<Goods> goodsList = goodService.queryAll();

        //更新缓存
        StaticConfiguration.refreshGoodsCache(goodsList);

        // 表格所有行数据
        Object[][] rowData = DataUtils.GoodsListToObjectArray(goodsList);

        table.refreshData(rowData);

    }

    private void refreshDataForName(String name) {

        if (name.equals("")) {
            refreshData();
            return;
        }

        //实时查询改为在缓存中查询,下同
//        List<Goods> goodsList = goodService.queryAllForName(name);
        List<Goods> goodsList = DataUtils.queryForGoodsNameInCache(StaticConfiguration.getGoodsCache(), name);

        // 表格所有行数据
        Object[][] rowData = DataUtils.GoodsListToObjectArray(goodsList);

        table.refreshData(rowData);
    }

    private void refreshDataForDescription(String description) {
        if (description.equals("")) {
            refreshData();
            return;
        }

        //实时查询一遍
//        List<Goods> goodsList = goodService.queryAllForDescription(description);
        List<Goods> goodsList = DataUtils.queryForGoodsDescriptionInCache(StaticConfiguration.getGoodsCache(), description);

        // 表格所有行数据
        Object[][] rowData = DataUtils.GoodsListToObjectArray(goodsList);

        table.refreshData(rowData);
    }

    private void refreshDataForPrice(Double price) {
        //实时查询一遍
//        List<Goods> goodsList = goodService.queryByLeftPrice(price);
        List<Goods> goodsList = DataUtils.queryForGoodsLeftPriceInCache(StaticConfiguration.getGoodsCache(), price);

        // 表格所有行数据
        Object[][] rowData = DataUtils.GoodsListToObjectArray(goodsList);

        table.refreshData(rowData);
    }

    private void refreshDataForId(Integer id) {
        // 表头（列名）
        String[] columnNames = {"ID", "药名", "描述", "库存", "单价", "类型"};

        //实时查询一遍
//        Goods goods = goodService.queryById(id);
        Goods goods = DataUtils.queryForGoodsIdInCache(StaticConfiguration.getGoodsCache(), id);

        table.refreshData(new Object[][]{
                {goods.getGoodId(),
                        goods.getGoodName(),
                        goods.getGoodText(),
                        goods.getGoodStock(),
                        goods.getGoodMoney(),
                        goods.getGoodType() == 0 ? "处方" : "非处方",
                }
        });
    }

    //刷新选择区
    private void refreshSelectTable() {
        Object[][] data = DataUtils.SelectGoodsListToObjectArray(StaticConfiguration.getSelectGoods());
        selectTable.refreshData(data);
    }
}
