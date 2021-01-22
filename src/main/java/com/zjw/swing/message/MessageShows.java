package com.zjw.swing.message;

import com.zjw.swing.stockManager.SupplierPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 13:59
 */
public class MessageShows {

    public static void SuccessRegister(Component component) {
        JOptionPane.showMessageDialog(component,
                "注册成功",
                "success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void InconsistentPwd(Component component) {
        JOptionPane.showMessageDialog(component,
                "两次密码不一致",
                "fail",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void ExistUsername(Component component) {
        JOptionPane.showMessageDialog(component,
                "用户名已存在",
                "fail",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void ErrorRegisterId(Component component) {
        JOptionPane.showMessageDialog(component,
                "注册码错误",
                "fail",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void ErrorCheckId(Component component) {
        JOptionPane.showMessageDialog(component,
                "验证码错误",
                "fail",
                JOptionPane.INFORMATION_MESSAGE
        );
    }



    public static void ErrorInputText(Component component) {
        JOptionPane.showMessageDialog(component,
                "参数错误",
                "fail",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void SuccessMessage(Component component, String message) {
        JOptionPane.showMessageDialog(component,
                message,
                "success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void ShowMessageText(Component component, String title, String text) {
        JOptionPane.showMessageDialog(component,
                text,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static boolean ShowMessageAboutDeleteLogName(Component component) {
        /*
         * 返回用户点击的选项, 值为下面三者之一:
         *     是:   JOptionPane.YES_OPTION
         *     否:   JOptionPane.NO_OPTION
         *     取消: JOptionPane.CANCEL_OPTION
         *     关闭: JOptionPane.CLOSED_OPTION
         */
        int result = JOptionPane.showConfirmDialog(
                component,
                "确认删除该账户吗？",
                "提示",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        return result == 0;
    }

    public static boolean ShowMessageAboutDeleteAnnouncement(Component component) {
        /*
         * 返回用户点击的选项, 值为下面三者之一:
         *     是:   JOptionPane.YES_OPTION
         *     否:   JOptionPane.NO_OPTION
         *     取消: JOptionPane.CANCEL_OPTION
         *     关闭: JOptionPane.CLOSED_OPTION
         */
        int result = JOptionPane.showConfirmDialog(
                component,
                "确认删除该公告吗？",
                "提示",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        return result == 0;
    }

    public static boolean ShowMessageAboutDeleteGoods(Component component) {
        /*
         * 返回用户点击的选项, 值为下面三者之一:
         *     是:   JOptionPane.YES_OPTION
         *     否:   JOptionPane.NO_OPTION
         *     取消: JOptionPane.CANCEL_OPTION
         *     关闭: JOptionPane.CLOSED_OPTION
         */
        int result = JOptionPane.showConfirmDialog(
                component,
                "确认下架该药品？",
                "提示",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        return result == 0;
    }

    public static boolean ShowMessageAboutDeleteSupplier(Component component) {
        /*
         * 返回用户点击的选项, 值为下面三者之一:
         *     是:   JOptionPane.YES_OPTION
         *     否:   JOptionPane.NO_OPTION
         *     取消: JOptionPane.CANCEL_OPTION
         *     关闭: JOptionPane.CLOSED_OPTION
         */
        int result = JOptionPane.showConfirmDialog(
                component,
                "确认删除该供应商？",
                "提示",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        return result == 0;
    }

    public static boolean ShowMessageAboutOfferSale(Component component) {
        /*
         * 返回用户点击的选项, 值为下面三者之一:
         *     是:   JOptionPane.YES_OPTION
         *     否:   JOptionPane.NO_OPTION
         *     取消: JOptionPane.CANCEL_OPTION
         *     关闭: JOptionPane.CLOSED_OPTION
         */
        int result = JOptionPane.showConfirmDialog(
                component,
                "该商品已下架，是否上架?",
                "提示",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        return result == 0;
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
