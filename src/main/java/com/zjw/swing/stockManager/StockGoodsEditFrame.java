package com.zjw.swing.stockManager;

import com.zjw.domain.Goods;
import com.zjw.domain.Supplier;
import com.zjw.service.GoodService;
import com.zjw.service.SupplierService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.OptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

import java.awt.*;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/16 15:04
 */

@Component
public class StockGoodsEditFrame extends JFrame {

    @Autowired
    private GoodService goodService;

    @Autowired
    private StockListPanel stockListPanel;

    @Autowired
    private SupplierService supplierService;

    public void run(Goods goods) {
        //传入参数为null 代表新建
        //否则，为编辑模式

        ImageJPanel panel = new ImageJPanel(null, "/images/index/t7.jpg");

        this.setTitle("药物信息录入");
        this.setContentPane(panel);
        this.setSize(1500, 1000);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

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

        JLabel nameF = new JLabel("药名");
        nameF.setBounds(10, 60, 80, 30);
        panel.add(nameF);

        //type
        JComboBox<String> typeList = new JComboBox<>(new String[]{"处方", "非处方"});
        typeList.setBounds(100, 110, 1000, 30);
        panel.add(typeList);

        JLabel typeListF = new JLabel("类型");
        typeListF.setBounds(10, 100, 80, 30);
        panel.add(typeListF);

        //描述
        JTextPane descriptionText = new JTextPane();
        descriptionText.setFont(new Font(null, Font.PLAIN, 25));
        JScrollPane scrollPane = new JScrollPane(descriptionText);
        scrollPane.setBounds(100, 160, 1000, 300);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        descriptionText.setText("【药效】\n" +
                "\n" + "\n" +
                "【适用人群】\n" +
                "\n" + "\n" +
                "【副作用】\n");
        panel.add(scrollPane);

        JLabel descriptionF = new JLabel("描述");
        descriptionF.setBounds(10, 160, 80, 30);
        panel.add(descriptionF);

        //价格
        JTextField price = new JTextField();
        price.setBounds(100, 510, 1000, 30);
        panel.add(price);

        JLabel priceF = new JLabel("采购价(元)");
        priceF.setBounds(10, 510, 80, 30);
        panel.add(priceF);


        //供应商
        List<Supplier> suppliers = supplierService.queryAll();
        String[] suppliersItem = new String[suppliers.size()];
        for (int i = 0; i < suppliers.size(); i++) {
            suppliersItem[i] = suppliers.get(i).getSupplierId() + " " + suppliers.get(i).getName();
        }
        JComboBox<String> supplierList = new JComboBox<>(suppliersItem);
        supplierList.setBounds(100, 550, 1000, 30);
        panel.add(supplierList);

        JLabel supplierF = new JLabel("供应商");
        supplierF.setBounds(10, 550, 80, 30);
        panel.add(supplierF);


        //检测按钮：是否存在重复id，重复药名
        JButton checkButton = new JButton("检测");
        checkButton.setBounds(100, 600, 200, 30);
        panel.add(checkButton);

        JButton okButton = new JButton("确认");
        okButton.setBounds(100, 650, 200, 30);
        panel.add(okButton);

        //编辑模式
        if (goods != null) {
            id.setText(String.valueOf(goods.getGoodId()));
            id.setEditable(false);
            name.setText(goods.getGoodName());
            descriptionF.setText(goods.getGoodText());
            typeList.setSelectedIndex(goods.getGoodType());
            supplierList.setSelectedIndex(goods.getSupplierId() == null ? 0 : goods.getSupplierId());
            price.setText(String.valueOf(goods.getGoodMoney()));
        }

        setVisible(true);

        /*监听*/
        autoIdButton.addActionListener(e -> {
            int varId = goodService.queryMaxStockGoodsId() + 1;
            id.setText(String.valueOf(varId));
        });

        checkButton.addActionListener(e -> {
            try {
                String idText1 = id.getText();
                String nameText = name.getText();
                if (nameText.equals("") || idText1.equals("")) {
                    MessageShows.ShowMessageText(this, null, "ID或者药名不能为空");
                    return;
                }
                int idText = Integer.parseInt(idText1);
                boolean b = goodService.checkIdOrNameInDataBase(idText, nameText);
                if (b) {
                    MessageShows.ShowMessageText(this, null, "ID或者药名已经存在");
                } else {
                    MessageShows.ShowMessageText(this, null, "ID药名信息可用");
                }
            } catch (NumberFormatException ex) {
                MessageShows.ShowMessageText(this, null, "错误参数");
                ex.printStackTrace();
            }
        });

        okButton.addActionListener(e -> {
            try {
                int idText = Integer.parseInt(id.getText());
                String nameText = name.getText();
                int typeIndex = typeList.getSelectedIndex();
                String description = descriptionText.getText();
                double priceText = Double.parseDouble(price.getText());
                int supplierId = Integer.parseInt(((String) supplierList.getSelectedItem()).split(" ")[0]);
                boolean b = goodService.checkIdOrNameInDataBase(idText, nameText);
                if (goods == null && b) {
                    MessageShows.ShowMessageText(this, null, "ID或者药名已经存在");
                    return;
                }

                if (goods == null) {
                    Goods stockGoods = new Goods(idText, nameText, 0, description, priceText, typeIndex, supplierId);
                    goodService.insertStockList(stockGoods);
                    OptionUtils.recordCurrentOption("新增了采购药物:id " + idText + "-" + nameText);
                    MessageShows.ShowMessageText(this, null, "添加成功");
                } else {
                    goods.setGoodName(nameText);
                    goods.setGoodType(typeIndex);
                    goods.setGoodText(description);
                    goods.setGoodMoney(priceText);
                    goods.setSupplierId(supplierId);
                    goodService.updateStockGoodsById(goods);
                    OptionUtils.recordCurrentOption("编辑了采购药物:id " + idText + "-" + nameText);

                    MessageShows.ShowMessageText(this, null, "编辑成功");
                }
                stockListPanel.refreshData();
                this.setVisible(false);
                this.dispose();
            } catch (Exception ex) {
                MessageShows.ShowMessageText(this,"failed","参数错误");
            }
        });
    }

}
