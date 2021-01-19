package com.zjw.swing.login;

import com.zjw.service.CustomerService;
import com.zjw.service.EmployService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.config.StaticConfiguration;
import com.zjw.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Arrays;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 18:47
 */
@Component
public class ChangePasswordFrame {

    @Component
    public static class ChangePasswordByPhone extends JFrame {
        //通过手机号码修改密码
        public void run() {
            this.setSize(1000, 1000);
            this.setTitle("修改密码");
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            JPanel content = new ImageJPanel(null, "/images/login/t4.jpg");

            //服务器
            JTextField serviceAddress = new JTextField("zjw.life");
            serviceAddress.setSize(200, 30);
            serviceAddress.setLocation(400, 400);
            content.add(serviceAddress);

            JLabel serviceAddressFlag = new JLabel("服务器");
            serviceAddressFlag.setSize(100, 30);
            serviceAddressFlag.setLocation(350, 400);
            content.add(serviceAddressFlag);

            //手机号
            JTextField phone = new JTextField();
            phone.setSize(200, 30);
            phone.setLocation(400, 450);
            content.add(phone);

            JLabel phoneFlag = new JLabel("手机号");
            phoneFlag.setSize(100, 30);
            phoneFlag.setLocation(350, 450);
            content.add(phoneFlag);

            //验证码
            JTextField checkId = new JTextField();
            checkId.setSize(200, 30);
            checkId.setLocation(400, 500);
            content.add(checkId);

            JLabel checkIdFlag = new JLabel("验证码");
            checkIdFlag.setSize(100, 30);
            checkIdFlag.setLocation(350, 500);
            content.add(checkIdFlag);

            //验证码
            JTextField newPwd = new JTextField();
            newPwd.setSize(200, 30);
            newPwd.setLocation(400, 550);
            content.add(newPwd);

            JLabel newPwdFlag = new JLabel("新密码");
            newPwdFlag.setSize(100, 30);
            newPwdFlag.setLocation(350, 550);
            content.add(newPwdFlag);

            //发送验证码按钮
            JButton checkButton = new JButton("发送验证码");
            checkButton.setSize(100, 30);
            checkButton.setLocation(400, 600);
            content.add(checkButton);

            //确认修改
            JButton okButton = new JButton("确认修改");
            okButton.setSize(100, 30);
            okButton.setLocation(400, 650);
            content.add(okButton);

            this.setContentPane(content);
            this.setVisible(true);

            /*监听*/
            checkButton.addActionListener(e -> {
                System.out.println("发送验证码");
            });

            okButton.addActionListener(e -> {
                String check = checkId.getText();
                System.out.println("核对验证码:" + check);
                MessageShows.ErrorCheckId(this);
            });
        }
    }

    @Component
    public static class ChangePasswordByOldPassword extends JFrame {

        @Autowired
        private EmployService employService;

        @Autowired
        private CustomerService customerService;

        //通过旧密码修改密码
        public void run() {
            this.setSize(1000, 1000);
            this.setTitle("修改密码");
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            JPanel content = new ImageJPanel(null, "/images/login/t3.jpg");

            //服务器
            JTextField serviceAddress = new JTextField("zjw.life");
            serviceAddress.setSize(200, 30);
            serviceAddress.setLocation(400, 400);
            serviceAddress.setEditable(false);
            content.add(serviceAddress);

            JLabel serviceAddressFlag = new JLabel("服务器");
            serviceAddressFlag.setSize(100, 30);
            serviceAddressFlag.setLocation(350, 400);
            content.add(serviceAddressFlag);

            //用户名
            JTextField userName = new JTextField();
            userName.setSize(200, 30);
            userName.setLocation(400, 450);
            userName.setEditable(false);
            content.add(userName);
            userName.setText(StaticConfiguration.getEmploy() != null ? StaticConfiguration.getEmploy().getLoginName() :
                    StaticConfiguration.getCustomer().getLoginName());

            JLabel phoneFlag = new JLabel("账号");
            phoneFlag.setSize(100, 30);
            phoneFlag.setLocation(350, 450);
            content.add(phoneFlag);

            //旧密码
            JPasswordField oldPwd = new JPasswordField();
            oldPwd.setSize(200, 30);
            oldPwd.setLocation(400, 500);
            content.add(oldPwd);

            JLabel oldPwdFlag = new JLabel("旧密码");
            oldPwdFlag.setSize(100, 30);
            oldPwdFlag.setLocation(350, 500);
            content.add(oldPwdFlag);

            //新密码
            JPasswordField newPwd = new JPasswordField();
            newPwd.setSize(200, 30);
            newPwd.setLocation(400, 550);
            content.add(newPwd);

            JLabel newPwdFlag = new JLabel("新密码");
            newPwdFlag.setSize(100, 30);
            newPwdFlag.setLocation(350, 550);
            content.add(newPwdFlag);

            //确认按钮
            JButton okButton = new JButton("确认修改");
            okButton.setSize(100, 30);
            okButton.setLocation(450, 600);
            content.add(okButton);

            this.setContentPane(content);
            this.setVisible(true);


            okButton.addActionListener(e -> {
                try {
                    String pwd = Md5Utils.Md5(String.valueOf(oldPwd.getPassword()));
                    if (StaticConfiguration.getEmploy() != null) {
                        if (!StaticConfiguration.getEmploy().getLoginPassword().equals(pwd)) {
                            MessageShows.ShowMessageText(this, "fail", "原密码错误");
                            return;
                        }

                        StaticConfiguration.getEmploy().setLoginPassword(Md5Utils.Md5(String.valueOf(newPwd.getPassword())));
                        employService.update(StaticConfiguration.getEmploy());
                    } else {
                        if (!StaticConfiguration.getCustomer().getLoginPassword().equals(pwd)) {
                            MessageShows.ShowMessageText(this, "fail", "原密码错误");
                            return;
                        }

                        StaticConfiguration.getCustomer().setLoginPassword(Md5Utils.Md5(String.valueOf(newPwd.getPassword())));
                        customerService.update(StaticConfiguration.getCustomer());
                    }

                    MessageShows.ShowMessageText(this, "success", "修改成功");
                } catch (Exception ex) {
                    MessageShows.ShowMessageText(this, "fail", "修改失败");
                    ex.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) {
        new ChangePasswordFrame.ChangePasswordByPhone().run();
        new ChangePasswordFrame.ChangePasswordByOldPassword().run();
    }
}
