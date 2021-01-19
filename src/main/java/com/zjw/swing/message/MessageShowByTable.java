package com.zjw.swing.message;

import com.zjw.swing.utils.DefaultJTable;
import com.zjw.swing.utils.ImageJPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/9 13:00
 */
public class MessageShowByTable {
    public static DefaultJTable show(Object[] colName, Object[][] data) {
        JFrame jf = new JFrame();
        jf.setSize(1000, 800);
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);

        JPanel show = new ImageJPanel(null, "/images/login/t4.jpg");
        jf.setContentPane(show);

        DefaultJTable defaultJTable = new DefaultJTable(colName, new DefaultTableModel());
        defaultJTable.getJScrollPane().setSize(1000, 800);
        defaultJTable.getJScrollPane().setLocation(0, 0);

        //透明化表格
        defaultJTable.setOpaque(false);
        defaultJTable.getJScrollPane().setOpaque(false);
        defaultJTable.getJScrollPane().getViewport().setOpaque(false);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

        show.add(defaultJTable.getJScrollPane());

        defaultJTable.refreshData(data);

        jf.setVisible(true);

        //返回table可以添加监听器
        return defaultJTable;
    }

}
