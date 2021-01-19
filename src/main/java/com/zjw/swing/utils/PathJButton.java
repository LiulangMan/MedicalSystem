package com.zjw.swing.utils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/19 19:37
 */
public class PathJButton extends JButton {

    public PathJButton parentButton = null;

    public File file;

    public PathJButton(String text) {
        super(text);
    }

    @Override
    public String toString() {
        return file.getName().equals("") ? file.getPath() : file.getName();
    }
}
