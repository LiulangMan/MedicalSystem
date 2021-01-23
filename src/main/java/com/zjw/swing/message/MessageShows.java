package com.zjw.swing.message;

import javax.swing.*;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 13:59
 */
public class MessageShows {

    public static void ShowMessageText(Component component, String title, String text) {
        JOptionPane.showMessageDialog(component,
                text,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    public static boolean ShowMessageAboutMakeSure(Component component,String text) {
        /*
         * 返回用户点击的选项, 值为下面三者之一:
         *     是:   JOptionPane.YES_OPTION
         *     否:   JOptionPane.NO_OPTION
         *     取消: JOptionPane.CANCEL_OPTION
         *     关闭: JOptionPane.CLOSED_OPTION
         */
        int result = JOptionPane.showConfirmDialog(
                component,
                text,
                "提示",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        return result == 0;
    }
}
