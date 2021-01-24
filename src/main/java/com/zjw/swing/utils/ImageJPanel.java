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

    private String imagePath;

    private Graphics g;

    public ImageJPanel(LayoutManager layout, String imagePath) {
        super(layout);
        URL url = getClass().getResource(imagePath);
        ImageIcon icon = new ImageIcon(url);
        image = icon.getImage();
        this.imagePath = imagePath;
    }

    @Override
    protected void paintComponent(Graphics g) {
        this.g = g;
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void changeImages(String imagePath) {
        URL url = getClass().getResource(imagePath);
        ImageIcon icon = new ImageIcon(url);
        image = icon.getImage();
        this.imagePath = imagePath;

        this.paintComponent(this.g);
    }
}
