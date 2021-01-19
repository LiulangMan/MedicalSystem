package com.zjw.swing.utils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/6 20:19
 */
public class ImageJPanel extends JPanel {

    private Image image;

    public ImageJPanel(LayoutManager layout, String imagePath) {
        super(layout);
        URL url = getClass().getResource(imagePath);
        ImageIcon icon = new ImageIcon(url);
        image = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
