package com.zjw.swing.userManager;

import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/19 15:38
 */
@Component
public class PathEditJFame extends JFrame {

    private void init() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void run() {

        init();

        JPanel panel = new JPanel(null);
        this.setContentPane(panel);

        //目录
        JTextField path = new JTextField();
        path.setBounds(10, 10, 200, 30);
        panel.add(path);

        //文件
        JTextField file = new JTextField();
        file.setBounds(10, 60, 200, 30);
        panel.add(file);

        this.setVisible(true);


    }

    public static void main(String[] args) {
        new PathEditJFame().run();
    }
}
