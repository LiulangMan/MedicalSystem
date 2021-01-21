package com.zjw.swing.utils;

import com.zjw.config.FontConfiguration;
import com.zjw.swing.test.JPanelTest;
import com.zjw.swing.test.JSpinnerTest;

import javax.swing.*;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/21 14:04
 */
public class ProgressBarJPanel extends JPanel {

    private JLabel label = new JLabel();

    private JProgressBar progressBar = new JProgressBar();

    public ProgressBarJPanel(String message) {
        super(null);
        this.setSize(400,200);
        label.setText(message);
        label.setFont(FontConfiguration.getFont("宋体", 38));
        label.setBounds(10,10,380,90);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        //进度条--设置不确定模式
        progressBar.setIndeterminate(true);
        progressBar.setBounds(10,110,380,30);
        this.add(label);
        this.add(progressBar);
    }

    public ProgressBarJPanel() {
        super(null);
        this.setSize(400,200);
        label.setFont(FontConfiguration.getFont("宋体", 38));
        label.setBounds(10,10,380,90);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        //进度条--设置不确定模式
        progressBar.setIndeterminate(true);
        progressBar.setBounds(10,110,380,30);
        this.add(label);
        this.add(progressBar);
    }

    public void setTitle(String title) {
        label.setText(title);
    }


    public static void main(String[] args) {
        ProgressBarJPanel panel = new ProgressBarJPanel();
        panel.setTitle("正在备份数据库");
        JPanelTest.setSize(400, 200);
        JPanelTest.test(panel);
    }

}
