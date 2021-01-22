package com.zjw.swing.login;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.*;
import com.zjw.service.*;
import com.zjw.swing.index.CustomerIndexFrame;
import com.zjw.swing.index.EmployIndexFrame;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.config.StaticConfiguration;
import com.zjw.swing.utils.MySwingUtils;
import com.zjw.utils.EmptyUtils;
import com.zjw.utils.Md5Utils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/4 18:00
 */
@Component
public class LoginFrame extends JFrame {


    //登陆服务，连接数据库
    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployService employService;

    @Autowired
    private GoodService goodService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockOrderService stockOrderService;

    //注册账号
    @Autowired
    private RegisterFrame registerFrame;

    //修改密码
    @Autowired
    private ChangePasswordFrame.ChangePasswordByPhone changePasswordByPhone;

    //主页
    @Autowired
    private EmployIndexFrame employIndexFrame;

    @Autowired
    private CustomerIndexFrame customerIndexFrame;

    @Autowired
    private LoginService loginService;


    public void init() {

        this.setTitle("医疗销售管理系统");
        this.setSize(1000, 1000);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        //绝对布局
        JPanel inner = new JPanel(null);
        inner.setSize(600, 600);
        inner.setLocation(200, 200);
        inner.setBackground(null);
        inner.setOpaque(false);

        //服务器地址
        JTextField serviceAddress = new JTextField();
        serviceAddress.setText("zjw.life");
        serviceAddress.setSize(200, 30);
        serviceAddress.setLocation(200, 150);
        inner.add(serviceAddress);

        JLabel serviceAddressFlag = new JLabel("服务器");
        serviceAddressFlag.setSize(50, 30);
        serviceAddressFlag.setLocation(160, 150);
        inner.add(serviceAddressFlag);

        //输入框
        JTextField username = new JTextField("admin");
        username.setSize(200, 30);
        username.setLocation(200, 200);
        inner.add(username);

        JLabel usernameFlag = new JLabel("账号");
        usernameFlag.setSize(50, 30);
        usernameFlag.setLocation(170, 200);
        inner.add(usernameFlag);

        //密码框
        JPasswordField password = new JPasswordField("123456");
        password.setSize(200, 30);
        password.setLocation(200, 250);
        inner.add(password);

        JLabel passwordFlag = new JLabel("密码");
        passwordFlag.setSize(50, 30);
        passwordFlag.setLocation(170, 250);
        inner.add(passwordFlag);

        //单选按钮
        JRadioButton employButton = new JRadioButton("员工");
        JRadioButton customerButton = new JRadioButton("顾客");
        ButtonGroup group = new ButtonGroup();
        group.add(employButton);
        group.add(customerButton);
        employButton.setSelected(true);
        employButton.setLocation(200, 300);
        employButton.setSize(100, 50);
        customerButton.setLocation(300, 300);
        customerButton.setSize(100, 50);
        inner.add(employButton);
        inner.add(customerButton);

        //登陆按钮
        JButton loginButton = new JButton("登陆");
        loginButton.setSize(200, 30);
        loginButton.setLocation(200, 400);
        inner.add(loginButton);

        //注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.setSize(200, 30);
        registerButton.setLocation(200, 450);
        inner.add(registerButton);

        //忘记密码
        JLabel forgetPassword = new JLabel("忘记密码？");
        forgetPassword.setSize(200, 30);
        forgetPassword.setLocation(300, 500);
        inner.add(forgetPassword);

        //外部面板设置
        JPanel outer = new ImageJPanel(null, "/images/login/t3.jpg");
        outer.add(inner);

        //标题
        JLabel title = new JLabel("医疗销售管理系统");
        title.setSize(200, 50);
        title.setLocation(400, 100);
        title.setFont(new Font(null, Font.PLAIN, 25));
        outer.add(title);

        this.setContentPane(outer);
        this.setVisible(true);


        //监听
        JFrame temp = this;
        /*登陆*/
        loginButton.addActionListener(e -> {

            //加载数据
            SwingWorker<Integer, Object> worker = new SwingWorker<Integer, Object>() {
                @Override
                protected void done() {
                    MySwingUtils.ProgressBar.closeProgressBar();
                    try {
                        Integer result = get();
                        if (result == 0) {
                            temp.setVisible(false);
                            temp.dispose();

                        } else if (result == 1) {
                            MessageShows.ShowMessageText(temp, null, "用户名或者密码不能为空");
                        } else if (result == 2) {
                            MessageShows.ShowMessageText(temp, null, "用户名不存在或者密码错误");
                        } else if (result == 3) {
                            MessageShows.ShowMessageText(temp, null, "该账户已经登陆");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                protected Integer doInBackground() throws Exception {

                    String serviceIp = serviceAddress.getText();
                    String usernameText = username.getText();
                    char[] passwordText = password.getPassword();

                    if (EmptyUtils.isEmptyOfUserNameOrPwd(usernameText, Arrays.toString(passwordText))) {
                        //弹出用户名或者密码不能为空
                        //MessageShows.EmptyUserOrPwd(this);
                        return 1;
                    }

                    //密文
                    String pwd = Md5Utils.Md5(String.valueOf(passwordText));
                    try {
                        if (employButton.isSelected()) {
                            Employ employ = employService.queryByLoginNameForOne(usernameText);
                            if (employ == null || !employ.getLoginPassword().equals(pwd)) {
                                //抛出用户名不存在或者密码错误
                                //MessageShows.ErrorUserOrPwd(this);
                                return 2;
                            }

                            //记录登陆信息
                            boolean isNotOnline = loginService.recordLogin(new InfoLogin(employ.getLoginName(), new Date(), employ.getType()));
                            if (!isNotOnline) {
                                //MessageShows.ShowMessageText(this, null, "该账户已经登陆");
                                return 3;
                            }

                            //进入employ系统
                            StaticConfiguration.setEmploy(employ);
                            StaticConfiguration.setLoginType(employ.getType());
                            StaticConfiguration.addThreadPoolTask(() -> employIndexFrame.run());
                        } else {
                            Customer customer = customerService.queryByLoginNameForOne(usernameText);
                            if (customer == null || !customer.getLoginPassword().equals(pwd)) {
                                //抛出用户名不存在或者密码错误
                                //MessageShows.ErrorUserOrPwd(this);
                                return 2;
                            }

                            //记录登陆信息
                            boolean isNotOnline = loginService.recordLogin(new InfoLogin(customer.getLoginName(), new Date(), IndexConstant.LOGIN_TYPE_CUSTOMER));
                            if (!isNotOnline) {
                                //MessageShows.ShowMessageText(this, null, "该账户已经登陆");
                                return 3;
                            }

                            //进入customer系统
                            StaticConfiguration.setCustomer(customer);
                            StaticConfiguration.setLoginType(IndexConstant.LOGIN_TYPE_CUSTOMER);
                            StaticConfiguration.addThreadPoolTask(new Runnable() {
                                @Override
                                public void run() {
                                    customerIndexFrame.run();
                                }
                            });
                        }
                        //登陆成功
                        return 0;

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        MessageShows.ShowMessageText(temp, "failLogin", "登陆出现错误");
                        loginService.logout(usernameText, StaticConfiguration.getLoginType());
                    }

                    return null;
                }
            };

            //登陆逻辑
            MySwingUtils.ProgressBar.showProgressBar("正在登陆");
            worker.execute();

        });

        /*注册*/
        registerButton.addActionListener(e -> {
            //跳转注册界面
            StaticConfiguration.addThreadPoolTask(() -> registerFrame.run());
        });

        /*忘记密码*/
        forgetPassword.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StaticConfiguration.addThreadPoolTask(() -> changePasswordByPhone.run());
            }
        });
    }
}
