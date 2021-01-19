package com.zjw.swing.login;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.Customer;
import com.zjw.domain.Employ;
import com.zjw.service.CustomerService;
import com.zjw.service.EmployService;
import com.zjw.service.RegisterService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.Md5Utils;
import com.zjw.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/4 23:40
 */
@Component
public class RegisterFrame extends JFrame {


    //登陆服务，连接数据库
    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployService employService;

    @Autowired
    private RegisterService registerService;

    private void init() {
        this.setTitle("账号注册");
        this.setSize(1000, 1000);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);


    }

    public void run() {

        //初始化配置
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
        username.setLocation(200, 100);
        inner.add(username);

        JLabel usernameFlag = new JLabel("账号");
        usernameFlag.setSize(50, 30);
        usernameFlag.setLocation(170, 100);
        inner.add(usernameFlag);

        //密码框1
        JPasswordField password1 = new JPasswordField();
        password1.setSize(200, 30);
        password1.setLocation(200, 150);
        inner.add(password1);

        JLabel passwordFlag = new JLabel("密码");
        passwordFlag.setSize(50, 30);
        passwordFlag.setLocation(170, 150);
        inner.add(passwordFlag);

        //密码框2
        JPasswordField password2 = new JPasswordField();
        password2.setSize(200, 30);
        password2.setLocation(200, 200);
        inner.add(password2);

        JLabel password2Flag = new JLabel("再次输入密码");
        password2Flag.setSize(80, 30);
        password2Flag.setLocation(120, 200);
        inner.add(password2Flag);

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
        inner.add(employButton);
        inner.add(customerButton);

        //员工注册码
        JTextField registerId = new JTextField();
        registerId.setSize(200, 30);
        registerId.setLocation(200, 550);
        inner.add(registerId);

        JLabel registerIdFlag = new JLabel("员工注册码");
        registerIdFlag.setSize(100, 30);
        registerIdFlag.setLocation(120, 550);
        inner.add(registerIdFlag);

        //注册
        JButton registerButton = new JButton("注册");
        registerButton.setSize(200, 30);
        registerButton.setLocation(200, 500);
        inner.add(registerButton);

        //注释
        JLabel text = new JLabel("注：员工住址需填店铺住址,注册码由店长给。");
        text.setSize(500, 30);
        text.setLocation(150, 600);
        inner.add(text);

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

        //员工注册码显示
        employButton.addChangeListener(e -> {
            if (employButton.isSelected()) {
                registerId.setVisible(true);
                registerIdFlag.setVisible(true);
            } else {
                registerId.setVisible(false);
                registerIdFlag.setVisible(false);
            }
        });

        //注册监听
        registerButton.addActionListener(e -> {
            String usernameText = username.getText();
            char[] password = password1.getPassword();
            char[] password2Text = password2.getPassword();
            String nameText = name.getText();
            String addressText = address.getText();
            String phoneText = phone.getText();
            boolean isBoy = boy.isSelected();
            boolean isEmploy = employButton.isSelected();

            //判断两次密码是否一样
            if (!Arrays.equals(password, password2Text)) {
                MessageShows.InconsistentPwd(this);
                password1.setText("");
                password2.setText("");
                return;
            }

            try {
                if (isEmploy) {
                    Employ select = employService.selectByLoginNameForOne(usernameText);
                    if (select != null) {
                        //用户名已经存在
                        MessageShows.ExistUsername(this);
                        return;
                    }

                    //检验注册码
                    String registerIdText = registerId.getText();
                    if (!registerService.checkRegisterId(registerIdText, addressText)) {
                        MessageShows.ErrorRegisterId(this);
                        return;
                    }

                    //加密
                    String pwd = Md5Utils.Md5(String.valueOf(password));

                    Employ employ = new Employ(0, RandomUtils.randomIdentity(), usernameText, pwd,
                            nameText, isBoy ? IndexConstant.BOY_TYPE : IndexConstant.GIRL_TYPE, addressText, phoneText, IndexConstant.LOGIN_TYPE_EMPLOY);

                    employService.insert(employ);
                } else {
                    //客户
                    Customer select = customerService.selectByLoginNameForOne(usernameText);
                    if (select != null) {
                        //用户名已经存在
                        MessageShows.ExistUsername(this);
                        return;
                    }

                    //加密
                    String pwd = Md5Utils.Md5(String.valueOf(password));
                    Customer customer = new Customer(0, RandomUtils.randomIdentity(), usernameText, pwd,
                            nameText, isBoy ? IndexConstant.BOY_TYPE : IndexConstant.GIRL_TYPE, addressText, phoneText);

                    customerService.insert(customer);
                }

                MessageShows.SuccessRegister(this);
                //关闭窗口
                this.setVisible(false);
                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
