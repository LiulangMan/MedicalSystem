package com.zjw.swing.test;

import javax.swing.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/18 15:53
 */
public class JPanelTest {

    private static JFrame jf = new JFrame("测试");

    public static void test(JPanel panel) {
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);
        jf.setContentPane(panel);

        jf.setVisible(true);
    }

    public static void setSize(int width, int high) {
        jf.setSize(width, high);
    }
}
