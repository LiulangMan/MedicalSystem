package com.zjw.swing.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.Arrays;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/8 17:14
 */
public class DefaultJTable extends JTable {

    private DefaultTableModel tableModel;

    private JScrollPane jScrollPane;

    private Object[] colName;

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public DefaultJTable(Object[] colName, DefaultTableModel tableModel) {
        super(tableModel);
        this.tableModel = tableModel;
        this.colName = colName;

        //设置列名
        this.tableModel.setDataVector(null, colName);

        // 设置行高
        this.setRowHeight(30);

        //不可拖动
        this.getTableHeader().setReorderingAllowed(false);

        // 列宽设置
        this.getColumnModel().getColumn(0).setPreferredWidth(40);

        this.jScrollPane = new JScrollPane(this);

    }

    public JScrollPane getJScrollPane() {
        return jScrollPane;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void refreshData(Object[][] data) {
        this.tableModel.setDataVector(data, colName);
    }
}
