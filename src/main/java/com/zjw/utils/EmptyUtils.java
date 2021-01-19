package com.zjw.utils;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 0:24
 */
public class EmptyUtils {
    public static boolean isEmptyOfUserNameOrPwd(String username, String password) {
        return username == null || username.equals("") || password == null || password.equals("");
    }
}
