package com.zjw.swing.utils;

import com.zjw.domain.Announcement;

import javax.swing.*;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/7 21:11
 */
public class FontJLabel extends JLabel {

    private Announcement announcement;

    public FontJLabel(String string, Font font, Announcement announcement) {
        super(string);
        super.setFont(font);
        this.announcement = announcement;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }
}
