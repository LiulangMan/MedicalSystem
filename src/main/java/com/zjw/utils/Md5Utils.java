package com.zjw.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/4 23:16
 */
public class Md5Utils {

    public static String Md5(Object source) {
        return new Md5Hash(source, "this is salt", 2).toString();
    }

}
