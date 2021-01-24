package com.zjw.swing.index;

import com.zjw.swing.utils.ImageJPanel;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/24 13:42
 */
@Component
public class HeadEditFrame extends JFrame {

    private JPanel panel;

    private int current = 0;

    private int total = 10;

    private java.util.List<java.awt.Component> componentList = new ArrayList<java.awt.Component>();

    public void init() {
        this.setTitle("头像选择");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void run() {

        this.init();

        panel = new JPanel(null);
        this.setContentPane(panel);

        JButton lastButton = new JButton("上一页");
        lastButton.setBounds(100, 610, 100, 30);
        this.add(lastButton);

        JButton nextButton = new JButton("下一页");
        nextButton.setBounds(250, 610, 100, 30);
        this.add(nextButton);

        JButton diyButton = new JButton("上传头像");
        diyButton.setBounds(400, 610, 100, 30);
        this.add(diyButton);

        //加载图片
        this.refreshData(9, 3, current);

        this.setVisible(true);

        nextButton.addActionListener(e -> {
            if (this.current + 9 < total) {
                this.current += 9;
            }
            refreshData(9, 3, this.current);
        });

        lastButton.addActionListener(e -> {
            this.current = Math.max(this.current - 9, 0);
            refreshData(9, 3, this.current);
        });

        diyButton.addActionListener(e -> {

        });

    }

    public void refreshData(int pageTotal, int len, int next) {

        if (next >= total) return;

        //刷新
        panel.setVisible(false);

        for (java.awt.Component component : componentList) {
            panel.remove(component);
        }
        componentList.clear();
        for (int i = 0; i < pageTotal && (i + next) < total; i++) {
            ImageJPanel m = new ImageJPanel(null, "/images/head/t" + (i + next) + ".jpg");
            int x = i % len;
            int y = i / len;
            m.setBounds(x * 200, y * 200, 200, 200);
            panel.add(m);
            componentList.add(m);
        }

        panel.setVisible(true);
    }

    public static void main(String[] args) {
        new HeadEditFrame().run();
    }

}
