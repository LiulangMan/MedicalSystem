package com.zjw.swing.stockManager;

import com.zjw.domain.Supplier;
import com.zjw.service.SupplierService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.ImageJPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/17 15:55
 */
@Component
public class SupplierEditFrame extends JFrame {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierPanel supplierPanel;

    public void run(Supplier supplier) {
        this.setSize(1500, 1000);
        this.setLocationRelativeTo(null);
        this.setTitle("供应商编辑");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        ImageJPanel panel = new ImageJPanel(null, "/images/index/t5.jpg");
        this.setContentPane(panel);

        //id
        JTextField id = new JTextField();
        id.setBounds(100, 10, 1000, 30);
        panel.add(id);

        JLabel idF = new JLabel("ID");
        idF.setBounds(10, 10, 80, 30);
        panel.add(idF);

        JButton autoIdButton = new JButton("自动生成可用ID");
        autoIdButton.setBounds(1200, 10, 200, 30);
        panel.add(autoIdButton);

        //name
        JTextField name = new JTextField();
        name.setBounds(100, 60, 1000, 30);
        panel.add(name);

        JLabel nameF = new JLabel("名字");
        nameF.setBounds(10, 60, 80, 30);
        panel.add(nameF);

        //phone
        JTextField phone = new JTextField();
        phone.setBounds(100, 110, 1000, 30);
        panel.add(phone);

        JLabel phoneF = new JLabel("电话");
        phoneF.setBounds(10, 110, 80, 30);
        panel.add(phoneF);

        //address
        JTextField address = new JTextField();
        address.setBounds(100, 160, 1000, 30);
        panel.add(address);

        JLabel addressF = new JLabel("地址");
        addressF.setBounds(10, 160, 80, 30);
        panel.add(addressF);

        //编辑模式
        if (supplier != null) {
            id.setText(String.valueOf(supplier.getSupplierId()));
            id.setEditable(false);
            name.setText(supplier.getName());
            phone.setText(supplier.getPhone());
            address.setText(supplier.getAddress());
        }


        //检测按钮：是否存在重复id，重复药名
        JButton checkButton = new JButton("检测");
        checkButton.setBounds(100, 600, 200, 30);
        panel.add(checkButton);

        JButton okButton = new JButton("确认");
        okButton.setBounds(100, 650, 200, 30);
        panel.add(okButton);

        this.setVisible(true);

        /*监听*/
        autoIdButton.addActionListener(e -> {
            int i = supplierService.queryMaxId() + 1;
            id.setText(String.valueOf(i));
        });

        checkButton.addActionListener(e -> {

            if (id.getText().equals("") || name.getText().equals("")) {
                MessageShows.ShowMessageText(this, null, "Id或者名字为空");
                return;
            }
            int idText = Integer.parseInt(id.getText());
            String nameText = name.getText();
            boolean b = supplierService.checkIdOrNameInDataBase(idText, nameText);
            if (b) {
                MessageShows.ShowMessageText(this, null, "Id或者名字已存在");
            } else {
                MessageShows.ShowMessageText(this, null, "信息可用");
            }
        });
        okButton.addActionListener(e -> {
            if (id.getText().equals("") || name.getText().equals("")) {
                MessageShows.ShowMessageText(this, null, "Id或者名字为空");
                return;
            }
            int idText = Integer.parseInt(id.getText());
            String nameText = name.getText();
            boolean b = supplierService.checkIdOrNameInDataBase(idText, nameText);
            String phoneText = phone.getText();
            String addressText = address.getText();
            if (supplier == null && b) {
                MessageShows.ShowMessageText(this, null, "Id或者名字已存在");
                return;
            }

            try {
                if (supplier == null) {
                    Supplier supplierVar = new Supplier(idText, nameText, phoneText, addressText);
                    supplierService.insert(supplierVar);
                    MessageShows.ShowMessageText(this, null, "添加成功");
                } else {
                    supplier.setName(nameText);
                    supplier.setAddress(addressText);
                    supplier.setPhone(phoneText);
                    supplierService.update(supplier);

                    MessageShows.ShowMessageText(this, null, "编辑成功");
                }
                this.setVisible(false);
                this.dispose();
                supplierPanel.refreshData();
            } catch (Exception ex) {
                MessageShows.ShowMessageText(this,null,"出现错误");
                ex.printStackTrace();
            }
        });
    }
}
