package com.zjw.swing.message;

import com.zjw.config.FontConfiguration;
import com.zjw.config.StaticConfiguration;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.swing.utils.ImageJPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

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

        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setFont(FontConfiguration.getFont("宋体", 25));

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setSize(1000, 800);
        scrollPane.setLocation(0, 0);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        show.add(scrollPane);

        jf.setVisible(true);
    }
}
