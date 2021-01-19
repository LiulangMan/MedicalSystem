package com.zjw.controller;

import com.zjw.config.StaticConfiguration;
import com.zjw.domain.StockOrder;
import com.zjw.service.*;
import com.zjw.swing.login.LoginFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/4 23:06
 */
@Controller
public class LoginController {

    @Autowired
    private LoginFrame loginFrame;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployService employService;

    @Autowired
    private GoodService goodService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private StockOrderService stockOrderService;

    public void start() {
        loginFrame.init();
    }
}
