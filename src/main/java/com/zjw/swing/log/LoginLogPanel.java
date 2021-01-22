package com.zjw.swing.log;

import com.zjw.constant.IndexConstant;
import com.zjw.domain.InfoLogin;
import com.zjw.service.LoginService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 21:20
 */
@Component
public class LoginLogPanel extends JPanel {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    public LoginLogPanel() {
        super(null);
    }

    @Autowired
    private LoginService loginService;

    private DefaultJTable recordTable;


    /*使用废弃的Data.setHour() setMinute setSecond方法*/
    @SuppressWarnings("deprecation")
    public void init() {

        //登陆日志表
        recordTable = new DefaultJTable(new Object[]{"登陆账户", "登陆类型", "登陆时间"}, new DefaultTableModel());
        recordTable.getJScrollPane().setSize(1200, 600);
        recordTable.getJScrollPane().setLocation(0, 0);
        this.add(recordTable.getJScrollPane());

        //搜索栏
        String[] type = {"用户名", "类型", "时间"};

        JComboBox<String> typeList = new JComboBox<>(type);
        typeList.setSize(80, 30);
        typeList.setLocation(10, 600);
        this.add(typeList);

        //搜索域
        JTextField searchField = new JTextField();
        searchField.setSize(300, 30);
        searchField.setLocation(100, 600);
        this.add(searchField);

        JComboBox<String> searchList = new JComboBox<>(new String[]{"超级管理员", "员工", "顾客"});
        searchList.setSize(300, 30);
        searchList.setLocation(100, 600);
        searchList.setVisible(false);
        this.add(searchList);

        JButton searchButton = new JButton("搜索");
        searchButton.setSize(100, 30);
        searchButton.setLocation(400, 600);
        this.add(searchButton);

        /*监听*/
        typeList.addItemListener(e -> {
            int index = typeList.getSelectedIndex();
            if (index == 0 || index == 2) {
                searchList.setVisible(false);
                searchField.setVisible(true);
                if (index == 0) {
                    searchField.setText("");
                }
                if (index == 2) {
                    Date date = new Date();
                    String dataText = format.format(date);
                    searchField.setText(dataText + "       " + dataText);
                }
            } else if (index == 1) {
                searchList.setVisible(true);
                searchField.setVisible(false);
            }
        });

        searchButton.addActionListener(e -> {
            try {
                int index = typeList.getSelectedIndex();
                String text = searchField.getText();

                if (index != 1 && text.equals("")) {
                    recordTable.refreshData(DataUtils.LoginInfoToArray(loginService.queryAll()));
                    return;
                }
                if (index == 0) {
                    recordTable.refreshData(DataUtils.LoginInfoToArray(loginService.queryAllByUsername(text)));
                } else if (index == 1) {
                    recordTable.refreshData(DataUtils.LoginInfoToArray(loginService.queryAllByType(searchList.getSelectedIndex())));
                } else if (index == 2) {
                    String[] date = text.split("[ ]+");
                    String from = date[0];
                    String to = date[1];
                    Date fromTime = format.parse(from);
                    Date toTime = format.parse(to);
                    toTime.setHours(23);
                    toTime.setMinutes(59);
                    toTime.setSeconds(59);
                    recordTable.refreshData(DataUtils.LoginInfoToArray(loginService.queryAllByTime(fromTime, toTime)));
                }
            } catch (Exception ex) {
                MessageShows.ErrorInputText(this);
                ex.printStackTrace();
            }
        });
    }

    public void refreshData(){
        List<InfoLogin> loginList = loginService.queryAll();
        recordTable.refreshData(DataUtils.LoginInfoToArray(loginList));
    }
}
