package com.zjw.utils;

import java.util.Date;
import java.util.Random;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 14:47
 */
public class RandomUtils {
    public static String randomIdentity() {
        double random = Math.random() * 1000;
        return new Date().toString().replaceAll(" ", "") + random;
    }
}
