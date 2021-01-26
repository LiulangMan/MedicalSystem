package com.zjw.swing.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

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

    @Override
    public java.awt.Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

        java.awt.Component c = super.prepareRenderer(renderer, row, column);
        if (c instanceof JComponent) {
            ((JComponent) c).setOpaque(false);
        }
        return c;
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

        //透明化表格
        this.setOpaque(false);
        this.jScrollPane.setOpaque(false);
        this.jScrollPane.getViewport().setOpaque(false);

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
