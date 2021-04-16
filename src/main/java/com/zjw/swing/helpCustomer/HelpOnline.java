package com.zjw.swing.helpCustomer;

import com.zjw.config.FontConfiguration;
import com.zjw.config.StaticConfiguration;
import com.zjw.constant.IndexConstant;
import com.zjw.domain.Question;
import com.zjw.service.QuestionService;
import com.zjw.swing.helpEmployee.HelpQuestionEditFrame;
import com.zjw.swing.message.MessageShowByText;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.DataUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 21:09
 */

@Component
public class HelpOnline extends ImageJPanel {
    private DefaultJTable questionTable;

    @Autowired
    private QuestionService questionService;

    public HelpOnline() {
        super(null, "/images/index/t7.jpg");
    }

    public void init() {

        //问题表
        questionTable = new DefaultJTable(new Object[]{"ID", "问题", "提问时间", "回答", "回答时间", "状态"}, new DefaultTableModel());
        questionTable.getJScrollPane().setBounds(0, 0, 1200, 600);

        this.add(questionTable.getJScrollPane());
        refreshData();

        //搜索栏
        JLabel searchF = new JLabel("问题");
        searchF.setSize(80, 30);
        searchF.setLocation(10, 600);
        this.add(searchF);

        JTextField searchField = new JTextField();
        searchField.setSize(300, 30);
        searchField.setLocation(100, 600);
        this.add(searchField);

        JButton searchButton = new JButton("搜索");
        searchButton.setSize(100, 30);
        searchButton.setLocation(400, 600);
        this.add(searchButton);

        JTextField baiduText = new JTextField();
        baiduText.setSize(200, 30);
        baiduText.setLocation(780, 700);
        this.add(baiduText);

        JButton urlButton = new JButton("百度一下");
        urlButton.setSize(100, 30);
        urlButton.setLocation(1000, 700);
        this.add(urlButton);

        //提问
        JButton questionButton = new JButton("提问");
        questionButton.setBounds(1000, 600, 100, 30);
        this.add(questionButton);

        //提问
        JButton myQuestionButton = new JButton("我的问题");
        myQuestionButton.setBounds(1000, 650, 100, 30);
        this.add(myQuestionButton);


        /*监听*/
        questionTable.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = questionTable.getSelectedRow();
                    String questionText = (String) questionTable.getValueAt(row, 1);
                    String answerText = (String) questionTable.getValueAt(row, 3);
                    MessageShowByText.show(questionText, answerText, FontConfiguration.getFont("宋体", 25));
                }
            }
        });

        urlButton.addActionListener(e -> {
            try {
                URI uri = URI.create("https://www.baidu.com/s?wd=" + baiduText.getText());
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                } else {
                    MessageShows.ShowMessageText(this, "", "不支持浏览器");
                }

            } catch (Exception e1) {
                MessageShows.ShowMessageText(this, "", e1.toString());
            }
        });


        searchButton.addActionListener(e -> {
            String text = searchField.getText();
            if (text.equals("")) {
                this.refreshData();
                return;
            }
            java.util.List<Question> questions = questionService.queryAllByQuestion(text);
            questionTable.refreshData(DataUtils.QuestionToArray(questions));
        });

        questionButton.addActionListener(e -> {
            // 显示输入对话框, 返回输入的内容
            String questionText = JOptionPane.showInputDialog(
                    this,
                    "输入你的问题:",
                    ""
            );

            if (questionText.equals("")) return;

            Question question = new Question(0, questionText, null, new Date(), null, 0,
                    StaticConfiguration.getCustomer().getLoginName(), null);

            questionService.insertQuestion(question);
            MessageShows.ShowMessageText(this, null, "提问成功，请耐心等待回答");
            this.refreshData();
        });
        myQuestionButton.addActionListener(e -> {
            List<Question> questionList = questionService.queryAllByQuestionUserName(StaticConfiguration.getCustomer().getLoginName());
            questionTable.refreshData(DataUtils.QuestionToArray(questionList));
        });
    }

    public void refreshData() {
        List<Question> questions = questionService.queryAll();
        questionTable.refreshData(DataUtils.QuestionToArray(questions));
    }

}
