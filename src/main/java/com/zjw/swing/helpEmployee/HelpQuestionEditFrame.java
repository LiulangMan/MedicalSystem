package com.zjw.swing.helpEmployee;

import com.zjw.config.FontConfiguration;
import com.zjw.config.StaticConfiguration;
import com.zjw.domain.Question;
import com.zjw.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

import java.util.Date;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/20 13:21
 */
@Component
public class HelpQuestionEditFrame extends JFrame {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HelpEmployPlane employPlane;

    private void init() {
        this.setSize(1000, 1000);
        this.setLocationRelativeTo(null);
        this.setTitle("问题");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void run(Question q) {
        init();

        JPanel panel = new JPanel(null);
        this.setContentPane(panel);

        JTextField question = new JTextField();
        question.setEditable(false);
        question.setBounds(10, 10, 980, 30);
        panel.add(question);

        //回答框
        JTextPane answer = new JTextPane();
        answer.setFont(FontConfiguration.getFont("宋体", 20));
        JScrollPane jScrollPane = new JScrollPane(answer);
        jScrollPane.setBounds(10, 50, 980, 750);
        jScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(jScrollPane);

        JButton okButton = new JButton("确定");
        okButton.setBounds(300, 850, 100, 30);
        panel.add(okButton);

        JButton cancelButton = new JButton("取消");
        cancelButton.setBounds(600, 850, 100, 30);
        panel.add(cancelButton);

        //刷新数据
        question.setText(q.getQuestion());

        this.setVisible(true);

        okButton.addActionListener(e -> {
            String text = answer.getText();
            q.setAnswer(text);
            q.setAnswerTime(new Date());
            q.setResponseUsername(StaticConfiguration.getEmploy().getLoginName());
            questionService.updateQuestionById(q);
            employPlane.refreshData();
            this.setVisible(false);
            this.dispose();
        });

        cancelButton.addActionListener(e -> {
            this.setVisible(false);
            this.dispose();
        });
    }
}
