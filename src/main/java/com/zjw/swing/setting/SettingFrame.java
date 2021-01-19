package com.zjw.swing.setting;

import com.zjw.constant.IndexConstant;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 21:08
 */
@Component
public class SettingFrame extends JPanel {
    public void init() {
        this.setSize(IndexConstant.CARD_WIDTH, IndexConstant.CARD_HIGH);
        this.setBackground(Color.WHITE);
    }
}
