package com.zjw.swing.userManager;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.Customer;
import com.zjw.domain.Employ;
import com.zjw.service.CustomerService;
import com.zjw.service.EmployService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.Md5Utils;
import javafx.scene.shape.Mesh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/26 11:42
 */
@Component
public class UserEditFrame extends JFrame {

    @Autowired
    private EmployService employService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserMangerFrame userMangerFrame;

    public void init() {
        this.setTitle("用户编辑");
        this.setSize(1000, 1000);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void run(Object object) {
        init();

        //内部板
        JPanel inner = new JPanel(null);
        inner.setSize(600, 800);
        inner.setLocation(200, 100);
        inner.setBackground(null);
        inner.setOpaque(false);

        //用户名
        JTextField username = new JTextField();
        username.setSize(200, 30);
        username.setLocation(200, 200);
        username.setEditable(false);
        inner.add(username);

        JLabel usernameFlag = new JLabel("账号");
        usernameFlag.setSize(50, 30);
        usernameFlag.setLocation(170, 200);
        inner.add(usernameFlag);

        //姓名
        JTextField name = new JTextField();
        name.setSize(200, 30);
        name.setLocation(200, 250);
        inner.add(name);

        JLabel nameFlag = new JLabel("姓名");
        nameFlag.setSize(50, 30);
        nameFlag.setLocation(170, 250);
        inner.add(nameFlag);

        //手机号
        JTextField phone = new JTextField();
        phone.setSize(200, 30);
        phone.setLocation(200, 300);
        inner.add(phone);

        JLabel phoneFlag = new JLabel("手机号码");
        phoneFlag.setSize(60, 30);
        phoneFlag.setLocation(140, 300);
        inner.add(phoneFlag);

        //住址
        JTextField address = new JTextField();
        address.setSize(200, 30);
        address.setLocation(200, 350);
        inner.add(address);

        JLabel addressFlag = new JLabel("住址");
        addressFlag.setSize(50, 30);
        addressFlag.setLocation(170, 350);
        inner.add(addressFlag);

        //性别
        JRadioButton boy = new JRadioButton("先生");
        JRadioButton girl = new JRadioButton("女士");
        ButtonGroup group2 = new ButtonGroup();
        group2.add(boy);
        group2.add(girl);
        boy.setSelected(true);
        boy.setLocation(200, 400);
        boy.setSize(100, 50);
        girl.setLocation(300, 400);
        girl.setSize(100, 50);
        inner.add(boy);
        inner.add(girl);

        //类型
        JRadioButton employButton = new JRadioButton("员工");
        JRadioButton customerButton = new JRadioButton("顾客");
        ButtonGroup group = new ButtonGroup();
        group.add(employButton);
        group.add(customerButton);
        employButton.setSelected(true);
        employButton.setLocation(200, 450);
        employButton.setSize(100, 50);
        customerButton.setLocation(300, 450);
        customerButton.setSize(100, 50);
        employButton.setEnabled(false);
        customerButton.setEnabled(false);
        inner.add(employButton);
        inner.add(customerButton);

        JButton pwdButton = new JButton("修改密码");
        pwdButton.setBounds(200, 550, 200, 30);
        inner.add(pwdButton);

        JButton okButton = new JButton("修改");
        okButton.setBounds(200, 600, 200, 30);
        inner.add(okButton);

        JButton cancelButton = new JButton("取消");
        cancelButton.setBounds(200, 650, 200, 30);
        inner.add(cancelButton);

        //外部面板设置
        JPanel outer = new ImageJPanel(null, "/images/login/register1.jpg");
        outer.setBackground(Color.GREEN);
        outer.add(inner);

        //标题
        JLabel title = new JLabel("医疗销售管理系统");
        title.setSize(200, 50);
        title.setLocation(400, 20);
        title.setFont(new Font(null, Font.PLAIN, 25));
        outer.add(title);

        this.setContentPane(outer);

        this.setVisible(true);

        //加载数据
        if (object instanceof Employ) {

            Employ employ = (Employ) object;
            username.setText(employ.getLoginName());
            name.setText(employ.getName());
            address.setText(employ.getAddress());
            phone.setText(employ.getPhone());
            employButton.setSelected(true);
            boy.setSelected(employ.getSex().equals(IndexConstant.BOY_TYPE));

        } else if (object instanceof Customer) {
            Customer customer = (Customer) object;

            username.setText(customer.getLoginName());
            name.setText(customer.getName());
            address.setText(customer.getAddress());
            phone.setText(customer.getPhone());
            customerButton.setSelected(true);
            boy.setSelected(customer.getSex().equals(IndexConstant.BOY_TYPE));
        }

        okButton.addActionListener(e -> {
            String nameText = name.getText();
            String addressText = address.getText();
            String phoneText = phone.getText();
            int sex = boy.isSelected() ? IndexConstant.BOY_TYPE : IndexConstant.GIRL_TYPE;
            if (object instanceof Employ) {

                Employ employ = (Employ) object;
                employ.setName(nameText);
                employ.setAddress(addressText);
                employ.setPhone(phoneText);
                employ.setSex(sex);
                employService.update(employ);

            } else if (object instanceof Customer) {
                Customer customer = (Customer) object;

                customer.setName(nameText);
                customer.setAddress(addressText);
                customer.setPhone(phoneText);
                customer.setSex(sex);
                customerService.update(customer);
            }

            userMangerFrame.refreshData();
            this.setVisible(false);
            this.dispose();
            MessageShows.ShowMessageText(this, null, "修改成功");
        });

        cancelButton.addActionListener(e -> {
            this.setVisible(false);
            this.dispose();
        });

        pwdButton.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(this, "输入新密码");
            String pwd = Md5Utils.Md5(s);

            if (object instanceof Employ) {

                Employ employ = (Employ) object;
                employ.setLoginPassword(pwd);
                employService.update(employ);

                MessageShows.ShowMessageText(this, null, "修改成功");

            } else if (object instanceof Customer) {
                Customer customer = (Customer) object;

                customer.setLoginPassword(pwd);
                customerService.update(customer);

                MessageShows.ShowMessageText(this, null, "修改成功");
            }

        });
    }
}
