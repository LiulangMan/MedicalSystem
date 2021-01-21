package com.zjw.swing.message;

import com.zjw.config.FontConfiguration;
import com.zjw.config.StaticConfiguration;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.swing.utils.ImageJPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/9 13:21
 */
public class MessageShowByText {
    public static void show(String title, String text, Font font) {

        JFrame jf = new JFrame(title);
        jf.setSize(1000, 800);
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);

        JPanel show = new ImageJPanel(null, "/images/login/t4.jpg");
        jf.setContentPane(show);

        JTextPane label = new JTextPane();
        label.setText(text);
        label.setFont(font);
        label.setSize(1000, 800);
        label.setLocation(0, 0);
        label.setBackground(null);
        label.setOpaque(false);
        label.setEditable(false);
        label.setFont(FontConfiguration.getFont("宋体", 25));
        show.add(label);

        jf.setVisible(true);
    }
}
