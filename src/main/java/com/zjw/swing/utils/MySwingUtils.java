package com.zjw.swing.utils;

import com.zjw.config.StaticConfiguration;

import javax.swing.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/21 14:31
 */
public class MySwingUtils {

    //进度条
    public static class ProgressBar {
        static ProgressBarJPanel progressBarJPanel = new ProgressBarJPanel();
        static JFrame jf = new JFrame("wait...");


        public static void showProgressBar(String title) {
            StaticConfiguration.addThreadPoolTask(new Runnable() {
                @Override
                public void run() {
                    creatUI(title);
                }
            });
        }

        private static void creatUI(String title) {
            jf.setSize(400, 200);
            jf.setResizable(false);
            jf.setContentPane(progressBarJPanel);
            jf.setLocationRelativeTo(null);
            jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            progressBarJPanel.setTitle(title);
            jf.setVisible(true);
        }

        public static void closeProgressBar() {
            jf.setVisible(false);
            jf.dispose();
        }
    }

    public static void main(String[] args) {
        MySwingUtils.ProgressBar.showProgressBar("正在加载数据");
    }
}
