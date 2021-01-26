package com.zjw.swing.home;

import com.zjw.domain.Announcement;
import com.zjw.service.AnnouncementService;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.ImageJPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/15 20:06
 */
@Component
public class HomeEditFrame extends JFrame {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private HomeFrame homeFrame;

    public void run(Announcement announcement) {
        ImageJPanel panel = new ImageJPanel(null, "/images/index/t7.jpg");

        this.setContentPane(panel);
        this.setSize(1500, 1000);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        //编辑区
        Font font = new Font(null, Font.PLAIN, 25);

        //标题
        JTextField title = new JTextField();
        title.setSize(1480, 30);
        title.setLocation(10, 10);
        title.setFont(font);
        title.setText(announcement == null ? "" : announcement.getTitle());
        panel.add(title);

        //正文
        JTextPane textTemp = new JTextPane();
        JScrollPane text = new JScrollPane(textTemp);
        text.setSize(1480, 800);
        text.setLocation(10, 50);
        textTemp.setText(announcement == null ? "" : announcement.getText());
        textTemp.setFont(font);
        //禁止水平滚动
        text.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(text);

        //确认按钮
        JButton okButton = new JButton("发布");
        okButton.setBounds(1300, 850, 100, 30);
        panel.add(okButton);

        //插入图片
        JButton imageButton = new JButton("插入图片");
        imageButton.setBounds(1100, 850, 100, 30);
        panel.add(imageButton);

        this.setVisible(true);

        //监听
        okButton.addActionListener(e -> {
            String textText = textTemp.getText();
            String titleText = title.getText();
            try {
                if (announcement == null) {
                    //发布新内容
                    Announcement newAnnouncement = new Announcement(titleText, textText, new Date());
                    announcementService.insert(newAnnouncement);
                    MessageShows.ShowMessageText(this, null, "发布成功");
                } else {
                    //编辑内容
                    announcement.setText(textText);
                    announcement.setTitle(titleText);
                    announcementService.updateAnnouncement(announcement);
                    MessageShows.ShowMessageText(this, null, "发布成功");
                }

                homeFrame.refreshAnnouncementPanel();
            } catch (Exception ex) {
                MessageShows.ShowMessageText(this, null, "未知错误");
                ex.printStackTrace();
            }
            this.setVisible(false);
            this.dispose();
        });

        imageButton.addActionListener(e -> {
            try {
                FileInputStream fis = new FileInputStream(new File("./images/head/t0.jpg"));
                byte[] bytes = new byte[fis.available()];
                fis.read(bytes);
                ImageIcon icon = new ImageIcon(bytes);
                textTemp.insertIcon(icon);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
