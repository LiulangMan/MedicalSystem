package com.zjw.swing.userManager;

import com.zjw.config.StaticConfiguration;
import com.zjw.constant.IndexConstant;
import com.zjw.domain.Customer;
import com.zjw.domain.Employ;
import com.zjw.domain.InfoLogin;
import com.zjw.service.CustomerService;
import com.zjw.service.EmployService;
import com.zjw.service.LoginService;
import com.zjw.swing.login.RegisterFrame;
import com.zjw.swing.message.MessageShowByTable;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.utils.DataUtils;
import com.zjw.utils.MysqlUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 21:05
 */
@Component
public class UserMangerFrame extends JPanel {

    @Autowired
    private EmployService employService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterFrame registerFrame;

    @Autowired
    private PathEditJFrame pathEditJFrame;

    private DefaultJTable userTable;


    public UserMangerFrame() {
        super(null);
    }

    public void init() {

        //用户表
        userTable = new DefaultJTable(new Object[]{"唯一码", "用户名", "姓名", "性别", "地址", "电话", "类型"}, new DefaultTableModel());
        userTable.getJScrollPane().setBounds(0, 0, 1200, 600);
        this.add(userTable.getJScrollPane());

        refreshData();

        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem recordButton = new JMenuItem("登陆记录");
        JMenuItem deleteButton = new JMenuItem("删除用户");
        JMenuItem addButton = new JMenuItem("新增用户");

        jPopupMenu.add(recordButton);
        jPopupMenu.add(addButton);
        jPopupMenu.add(deleteButton);

        //搜索栏
        String[] typeF = {"用户名", "姓名", "地址"};
        JComboBox<String> typeList = new JComboBox<>(typeF);
        typeList.setSize(80, 30);
        typeList.setLocation(10, 600);
        this.add(typeList);

        //搜索域
        JTextField searchField = new JTextField();
        searchField.setSize(300, 30);
        searchField.setLocation(100, 600);
        this.add(searchField);

        JButton searchButton = new JButton("搜索");
        searchButton.setSize(100, 30);
        searchButton.setLocation(400, 600);
        this.add(searchButton);

        JButton dumpButton = new JButton("备份数据");
        dumpButton.setSize(100, 30);
        dumpButton.setLocation(1000, 600);
        this.add(dumpButton);

        JButton backupButton = new JButton("还原数据");
        backupButton.setSize(100, 30);
        backupButton.setLocation(1000, 650);
        this.add(backupButton);

        /*监听*/
        userTable.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isMetaDown()) {
                    jPopupMenu.show(userTable, e.getX(), e.getY());
                }
            }
        });

        //登陆日志
        recordButton.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            String userName = (String) userTable.getValueAt(row, 1);
            Integer type = (Integer) userTable.getValueAt(row, 6);
            List<InfoLogin> infoLogins = loginService.queryAllByUsernameAndType(userName, type);
            MessageShowByTable.show(new Object[]{"登陆账户", "登陆类型", "登陆时间"}, DataUtils.LoginInfoToArray(infoLogins));
        });

        //删除用户
        deleteButton.addActionListener(e -> {
            boolean b = MessageShows.ShowMessageAboutDeleteLogName(this);
            if (!b) return;

            int row = userTable.getSelectedRow();
            String userName = (String) userTable.getValueAt(row, 1);
            Integer type = (Integer) userTable.getValueAt(row, 6);
            if (type == IndexConstant.LOGIN_TYPE_ADMIN) {
                MessageShows.ShowMessageText(this, null, "不能删除超级管理员");
                return;
            }
            if (type == IndexConstant.LOGIN_TYPE_EMPLOY) {
                employService.deleteByUserName(userName);
            }

            if (type == IndexConstant.LOGIN_TYPE_CUSTOMER) {
                customerService.deleteByUserName(userName);
            }
            MessageShows.ShowMessageText(this, null, "删除成功");

            refreshData();
        });

        //添加用户-注册系统
        addButton.addActionListener(e -> {
            StaticConfiguration.addThreadPoolTask(new Runnable() {
                @Override
                public void run() {
                    registerFrame.run();
                }
            });
        });

        searchButton.addActionListener(e -> {
            int index = typeList.getSelectedIndex();
            String text = searchField.getText();

            if (text.equals("")) {
                refreshData();
                return;
            }
            if (index == 0) {
                //用户名搜索
                refreshDataForUserName(text);

            } else if (index == 1) {
                //姓名搜索
                refreshDataForName(text);

            } else if (index == 2) {
                //地区搜索
                refreshDataForAddress(text);
            }
        });


        dumpButton.addActionListener(e -> {
            pathEditJFrame.run(true);

        });

        backupButton.addActionListener(e -> {
            pathEditJFrame.run(false);
        });
    }

    public void refreshData() {
        List<Employ> employs = employService.queryAll();
        List<Customer> customers = customerService.queryAll();

        List<Object> list = new ArrayList<>();
        list.addAll(employs);
        list.addAll(customers);
        userTable.refreshData(DataUtils.UserToArray(list));
    }

    private void refreshDataForUserName(String username) {
        Employ employ = employService.queryByLoginNameForOne(username);
        Customer customer = customerService.queryByLoginNameForOne(username);

        List<Object> list = new ArrayList<Object>() {
            @Override
            public boolean add(Object o) {
                if (o == null) {
                    //null不添入
                    return false;
                }
                return super.add(o);
            }
        };
        list.add(employ);
        list.add(customer);
        userTable.refreshData(DataUtils.UserToArray(list));
    }

    private void refreshDataForName(String username) {
        List<Employ> employ = employService.queryAllByName(username);
        List<Customer> customer = customerService.queryAllByName(username);

        List<Object> list = new ArrayList<>();
        list.addAll(employ);
        list.addAll(customer);
        userTable.refreshData(DataUtils.UserToArray(list));
    }

    private void refreshDataForAddress(String address) {
        List<Employ> employ = employService.queryAllByAddress(address);
        List<Customer> customer = customerService.queryByAddress(address);

        List<Object> list = new ArrayList<>();
        list.addAll(employ);
        list.addAll(customer);
        userTable.refreshData(DataUtils.UserToArray(list));
    }
}
