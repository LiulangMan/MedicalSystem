package com.zjw.swing.stockManager;

import com.zjw.config.StaticConfiguration;
import com.zjw.domain.Supplier;
import com.zjw.service.SupplierService;
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

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/10 12:27
 */
@Component
public class SupplierPanel extends JPanel {

    private DefaultJTable supplierTable;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierEditFrame supplierEditFrame;

    public SupplierPanel() {
        super(null);
    }

    public void init() {

        //表格
        supplierTable = new DefaultJTable(new String[]{"供应商ID", "名字", "电话", "地址"}, new DefaultTableModel());
        supplierTable.getJScrollPane().setBounds(0, 0, 1100, 600);
        this.add(supplierTable.getJScrollPane());

        //弹出菜单
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem editButton = new JMenuItem("编辑");
        JMenuItem deleteButton = new JMenuItem("删除");
        JMenuItem addButton = new JMenuItem("添加");
        jPopupMenu.add(editButton);
        jPopupMenu.add(deleteButton);
        jPopupMenu.add(addButton);

        this.setVisible(true);

        /*监听*/

        supplierTable.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isMetaDown()) {
                    jPopupMenu.show(supplierTable, e.getX(), e.getY());
                }
            }
        });

        editButton.addActionListener(e -> {
            int row = supplierTable.getSelectedRow();
            Integer id = (Integer) supplierTable.getValueAt(row, 0);
            Supplier supplier = supplierService.queryOneById(id);
            StaticConfiguration.addThreadPoolTask(new Runnable() {
                @Override
                public void run() {
                    supplierEditFrame.run(supplier);
                }
            });
        });

        deleteButton.addActionListener(e -> {
            boolean b = MessageShows.ShowMessageAboutMakeSure(this,"确认删除该供应商？");
            if (!b) return;
            int row = supplierTable.getSelectedRow();
            Integer id = (Integer) supplierTable.getValueAt(row, 0);
            supplierService.deleteById(id);
            refreshData();
        });

        addButton.addActionListener(e -> {
            StaticConfiguration.addThreadPoolTask(new Runnable() {
                @Override
                public void run() {
                    supplierEditFrame.run(null);
                }
            });
        });
    }

    public void refreshData() {
        supplierTable.refreshData(DataUtils.SupplierToArray(supplierService.queryAll()));
    }
}
