package com.zjw.swing.log;

import com.zjw.config.FontConfiguration;
import com.zjw.config.StaticConfiguration;
import com.zjw.service.OptionService;
import com.zjw.swing.message.MessageShowByText;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.test.JPanelTest;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.utils.DataUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/22 18:06
 */
@Component
public class OptionLogPanel extends JPanel {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    private DefaultJTable optionTable;

    @Autowired
    private OptionService optionService;

    public OptionLogPanel() {
        super(null);
    }


    @SuppressWarnings("deprecation")
    public void init() {

        optionTable = new DefaultJTable(new String[]{"ID", "操作员", "内容", "时间"}, new DefaultTableModel());
        optionTable.getJScrollPane().setBounds(0, 0, 1100, 600);
        this.add(optionTable.getJScrollPane());

        //搜索栏
        String[] type = {"操作员", "内容", "时间"};

        JComboBox<String> typeList = new JComboBox<>(type);
        typeList.setSize(80, 30);
        typeList.setLocation(10, 600);
        this.add(typeList);

        JTextField searchField = new JTextField();
        searchField.setSize(300, 30);
        searchField.setLocation(100, 600);
        this.add(searchField);

        JButton searchButton = new JButton("搜索");
        searchButton.setSize(100, 30);
        searchButton.setLocation(400, 600);
        this.add(searchButton);

        //弹出菜单
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem refreshButton = new JMenuItem("刷新");
        JMenuItem deleteButton = new JMenuItem("删除");
        jPopupMenu.add(refreshButton);
        jPopupMenu.add(deleteButton);

        //清空日志
        String[] clear = {"一周以上", "一个月以上", "三个月以上", "一年以上"};

        JComboBox<String> clearList = new JComboBox<>(clear);
        clearList.setSize(100, 30);
        clearList.setLocation(900, 600);
        this.add(clearList);

        JButton clearButton = new JButton("清除");
        clearButton.setSize(100, 30);
        clearButton.setLocation(1000, 600);
        this.add(clearButton);

        /*监听*/
        typeList.addItemListener(e -> {
            int index = typeList.getSelectedIndex();
            if (index == 2) {
                Date date = new Date();
                String dataText = format.format(date);
                searchField.setText(dataText + "       " + dataText);
            } else {
                searchField.setText("");
            }
        });

        searchButton.addActionListener(e -> {
            String text = searchField.getText();
            if (text.equals("")) {
                refreshData();
                return;
            }
            int index = typeList.getSelectedIndex();
            if (index == 0) {

                optionTable.refreshData(DataUtils.OptionToArray(optionService.queryAllByName(text)));

            } else if (index == 1) {
                optionTable.refreshData(DataUtils.OptionToArray(optionService.queryAllByDescription(text)));

            } else if (index == 2) {
                try {
                    String[] date = text.split("[ ]+");
                    String from = date[0];
                    String to = date[1];
                    Date fromTime = format.parse(from);
                    Date toTime = format.parse(to);
                    fromTime.setHours(0);
                    fromTime.setMinutes(0);
                    fromTime.setSeconds(0);
                    toTime.setHours(23);
                    toTime.setMinutes(59);
                    toTime.setSeconds(59);
                    optionTable.refreshData(DataUtils.OptionToArray(optionService.queryAllByTime(fromTime, toTime)));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }

        });

        refreshButton.addActionListener(e -> {
            this.refreshData();
        });

        deleteButton.addActionListener(e -> {
            boolean b = MessageShows.ShowMessageAboutMakeSure(this, "确认删除选择记录?");
            if (!b) return;

            int[] rows = optionTable.getSelectedRows();
            for (int row : rows) {
                Integer id = (Integer) optionTable.getValueAt(row, 0);
                this.optionService.deleteById(id);
            }
            this.refreshData();
        });

        optionTable.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = optionTable.getSelectedRow();
                    String text = (String) optionTable.getValueAt(row, 2);
                    StaticConfiguration.addThreadPoolTask(() ->
                            MessageShowByText.show("操作内容", text, FontConfiguration.getFont("宋体", 20))
                    );

                } else if (e.isMetaDown()) {
                    jPopupMenu.show(optionTable, e.getX(), e.getY());
                }
            }
        });

        clearButton.addActionListener(e -> {
            boolean b = MessageShows.ShowMessageAboutMakeSure(this, "确定清除" + clearList.getSelectedItem() + "日志?");
            if (!b) return;

            int index = clearList.getSelectedIndex();
            Calendar cal = Calendar.getInstance();
            if (index == 0) {
                cal.add(Calendar.DATE, -7);
            } else if (index == 1) {
                cal.add(Calendar.MONTH, -1);
            } else if (index == 2) {
                cal.add(Calendar.MONTH, -3);
            } else if (index == 3) {
                cal.add(Calendar.YEAR, -1);
            }
            optionService.deleteAllBefore(cal.getTime());
            MessageShows.ShowMessageText(this, null, "清除成功");
            this.refreshData();
        });
    }

    public void refreshData() {
        optionTable.refreshData(DataUtils.OptionToArray(optionService.queryAll()));
    }
}
