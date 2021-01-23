package com.zjw.swing.selfInformation;

import com.zjw.constant.IndexConstant;
import com.zjw.controller.LoginController;
import com.zjw.domain.Customer;
import com.zjw.domain.Employ;
import com.zjw.service.CustomerService;
import com.zjw.service.EmployService;
import com.zjw.service.LoginService;
import com.zjw.swing.index.CustomerIndexFrame;
import com.zjw.swing.index.EmployIndexFrame;
import com.zjw.swing.login.ChangePasswordFrame;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.config.StaticConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 21:08
 */
@Component
public class SelfInformationFrame extends ImageJPanel {

    @Autowired
    private EmployService employService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ChangePasswordFrame.ChangePasswordByOldPassword changePasswordByOldPassword;

    @Autowired
    private EmployIndexFrame employIndexFrame;

    @Autowired
    private CustomerIndexFrame customerIndexFrame;

    @Autowired
    private LoginController loginController;

    public SelfInformationFrame() {
        super(null, "/images/login/t4.jpg");
    }

    public void init() {
        this.setSize(IndexConstant.CARD_WIDTH, IndexConstant.CARD_HIGH);

        JTextField identity = new JTextField();
        identity.setSize(300, 30);
        identity.setLocation(450, 100);
        identity.setEditable(false);
        this.add(identity);

        JLabel identityFlag = new JLabel("生成码");
        identityFlag.setSize(50, 50);
        identityFlag.setLocation(400, 100);
        this.add(identityFlag);

        JComboBox type = new JComboBox<String>(new String[]{"员工", "顾客"});
        type.setSize(300, 30);
        type.setLocation(450, 150);
        type.setEnabled(false);
        this.add(type);

        JLabel typeFlag = new JLabel("类别");
        typeFlag.setSize(50, 50);
        typeFlag.setLocation(400, 150);
        this.add(typeFlag);

        JTextField userName = new JTextField();
        userName.setSize(300, 30);
        userName.setLocation(450, 200);
        userName.setEditable(false);
        this.add(userName);

        JLabel userNameFlag = new JLabel("用户名");
        userNameFlag.setSize(50, 50);
        userNameFlag.setLocation(400, 200);
        this.add(userNameFlag);

        JTextField name = new JTextField();
        name.setSize(300, 30);
        name.setLocation(450, 250);
        name.setEditable(false);
        this.add(name);
        JLabel nameFlag = new JLabel("姓名");
        nameFlag.setSize(50, 50);
        nameFlag.setLocation(400, 250);
        this.add(nameFlag);

        JComboBox sex = new JComboBox<String>(new String[]{"女", "男"});
        sex.setSize(300, 30);
        sex.setLocation(450, 300);
        sex.setEnabled(false);
        this.add(sex);

        JLabel sexFlag = new JLabel("性别");
        sexFlag.setSize(50, 50);
        sexFlag.setLocation(400, 300);
        this.add(sexFlag);

        JTextField address = new JTextField();
        address.setSize(300, 30);
        address.setLocation(450, 350);
        address.setEditable(false);
        this.add(address);
        JLabel addressFlag = new JLabel("地址");
        addressFlag.setSize(50, 50);
        addressFlag.setLocation(400, 350);
        this.add(addressFlag);

        JTextField phone = new JTextField();
        phone.setSize(300, 30);
        phone.setLocation(450, 400);
        phone.setEditable(false);
        this.add(phone);
        JLabel phoneFlag = new JLabel("电话");
        phoneFlag.setSize(50, 50);
        phoneFlag.setLocation(400, 400);
        this.add(phoneFlag);

        JButton changeInformationButton = new JButton("修改信息");
        changeInformationButton.setSize(300, 30);
        changeInformationButton.setLocation(450, 450);
        this.add(changeInformationButton);

        JButton changePasswordButton = new JButton("修改密码");
        changePasswordButton.setSize(300, 30);
        changePasswordButton.setLocation(450, 500);
        this.add(changePasswordButton);

        JButton deleteButton = new JButton("注销账户");
        deleteButton.setSize(300, 30);
        deleteButton.setLocation(450, 550);
        this.add(deleteButton);

        JButton logoutButton = new JButton("退出登陆");
        logoutButton.setSize(300, 30);
        logoutButton.setLocation(450, 600);
        this.add(logoutButton);

        JButton okButton = new JButton("确认");
        okButton.setSize(100, 30);
        okButton.setLocation(450, 650);
        okButton.setVisible(false);
        this.add(okButton);

        JButton cancelButton = new JButton("取消");
        cancelButton.setSize(100, 30);
        cancelButton.setLocation(650, 650);
        cancelButton.setVisible(false);
        this.add(cancelButton);

        /*初始化信息*/
        boolean isEmploy = StaticConfiguration.getEmploy() != null;
        Employ employ = StaticConfiguration.getEmploy();
        Customer customer = StaticConfiguration.getCustomer();
        type.setSelectedIndex(isEmploy ? 0 : 1);
        userName.setText(isEmploy ? employ.getLoginName() : customer.getLoginName());
        name.setText(isEmploy ? employ.getName() : customer.getName());
        sex.setSelectedIndex(isEmploy ? employ.getSex() : customer.getSex());
        phone.setText(isEmploy ? employ.getPhone() : customer.getPhone());
        address.setText(isEmploy ? employ.getAddress() : customer.getAddress());
        identity.setText(isEmploy ? employ.getEmployId() : customer.getCustomerId());

        /*监听*/
        changeInformationButton.addActionListener(e -> {
            okButton.setVisible(true);
            cancelButton.setVisible(true);

            name.setEditable(true);
            if (!isEmploy) {
                //员工地址不可修改
                address.setEditable(true);
            }
            phone.setEditable(true);
            sex.setEnabled(true);
        });

        //修改密码
        changePasswordButton.addActionListener(e -> {
            StaticConfiguration.addThreadPoolTask(new Runnable() {
                @Override
                public void run() {
                    changePasswordByOldPassword.run();
                }
            });
        });

        //注销账户
        deleteButton.addActionListener(e -> {
            if (MessageShows.ShowMessageAboutMakeSure(this,"确认删除该账户吗")) {
                if (isEmploy) {
                    employService.deleteById(employ.getId());
                } else {
                    customerService.delete(customer.getId());
                }

                MessageShows.ShowMessageText(this, "success", "注销成功");
                //关闭首页
                System.exit(0);
            }
        });

        logoutButton.addActionListener(e -> {

            if (isEmploy) {
                //退出登陆
                loginService.logout(employ.getLoginName(), employ.getType());
            } else {
                loginService.logout(customer.getLoginName(), IndexConstant.LOGIN_TYPE_CUSTOMER);
            }

            //关闭首页
            System.exit(0);
        });

        okButton.addActionListener(e -> {
            String nameText = name.getText();
            int sexText = sex.getSelectedIndex();
            String addressText = address.getText();
            String phoneText = phone.getText();

            try {
                //修改信息
                if (isEmploy) {
                    employ.setName(nameText);
                    employ.setSex(sexText);
                    employ.setPhone(phoneText);

                    employService.update(employ);
                    employIndexFrame.refreshInformation();

                } else {
                    customer.setName(nameText);
                    customer.setSex(sexText);
                    customer.setAddress(addressText);
                    customer.setPhone(phoneText);

                    customerService.update(customer);
                    customerIndexFrame.refreshInformation();
                }

                MessageShows.ShowMessageText(this, "", "修改成功");

            } catch (Exception ex) {
                ex.printStackTrace();
                MessageShows.ShowMessageText(this, "", "修改失败");
            } finally {
                name.setEditable(false);
                sex.setEnabled(false);
                address.setEditable(false);
                phone.setEditable(false);
                okButton.setVisible(false);
                cancelButton.setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            name.setEditable(false);
            sex.setEnabled(false);
            address.setEditable(false);
            phone.setEditable(false);
            //还原
            userName.setText(isEmploy ? employ.getLoginName() : customer.getLoginName());
            name.setText(isEmploy ? employ.getName() : customer.getName());
            sex.setSelectedIndex(isEmploy ? employ.getSex() : customer.getSex());
            phone.setText(isEmploy ? employ.getPhone() : customer.getPhone());
            //隐藏
            okButton.setVisible(false);
            cancelButton.setVisible(false);
        });
    }
}
