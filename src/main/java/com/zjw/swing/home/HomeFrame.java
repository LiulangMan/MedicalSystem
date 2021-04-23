package com.zjw.swing.home;

import com.zjw.config.FontConfiguration;
import com.zjw.config.StaticConfiguration;
import com.zjw.constant.IndexConstant;
import com.zjw.domain.Announcement;
import com.zjw.service.AnnouncementService;
import com.zjw.swing.message.MessageShowByText;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.FontJLabel;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 20:55
 */
@Component
public class HomeFrame extends ImageJPanel {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private HomeListFrame homeListFrame;

    //弹出菜单
    private JPopupMenu popupMenu;

    private JPanel announcementPanel;

    private Font font = FontConfiguration.getFont("宋体", 20);

    public HomeFrame() {
        super(null, "/images/login/login2.jpg");
    }

    public void init() {
        this.setSize(IndexConstant.CARD_WIDTH, IndexConstant.CARD_HIGH);

        //公告列表
        JButton listButton = new JButton("公告列表");
        listButton.setSize(100, 30);
        listButton.setLocation(1100, 770);
        this.add(listButton);

        //公告
        announcementPanel = new JPanel(new GridLayout(10, 1));
        announcementPanel.setSize(this.getWidth(), this.getHeight() - 50);
        announcementPanel.setLocation(0, 0);
        announcementPanel.setBackground(null);
        announcementPanel.setOpaque(false);
        this.add(announcementPanel);

        //弹出菜单
        popupMenu = new JPopupMenu();
        JMenuItem refreshMenu = new JMenuItem("刷新");
        refreshMenu.setFont(new Font(null, Font.PLAIN, 20));

        popupMenu.add(refreshMenu);


        /*监听*/
        announcementPanel.addMouseListener(new DefaultMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isMetaDown()) {
                    popupMenu.show(announcementPanel, e.getX(), e.getY());
                }
            }
        });

        refreshMenu.addActionListener(e -> {
            refreshAnnouncementPanel();
        });

        listButton.addActionListener(e -> {
            StaticConfiguration.addThreadPoolTask(() -> homeListFrame.run());
        });
    }

    public void refreshAnnouncementPanel() {
        //先隐藏
        announcementPanel.setVisible(false);

        //先移除之前的公告
        announcementPanel.removeAll();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd ");
        List<Announcement> announcements = announcementService.getAnnouncementTop10();
        List<JLabel> announcementList = new ArrayList<>(10);
        for (Announcement announcement : announcements) {
            announcementList.add(new FontJLabel(formatter.format(announcement.getWriteTime()) + " "
                    + announcement.getTitle(), font, announcement));
        }

        for (int i = 0; i < announcementList.size(); i++) {
            int finalI = i;

            JLabel label = announcementList.get(i);
            announcementPanel.add(label);
            label.addMouseListener(new DefaultMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        //左击
                        Announcement announcement = announcements.get(finalI);
                        MessageShowByText.show(
                                announcement.getTitle() + " " + formatter.format(announcement.getWriteTime()),
                                announcement.getText(),
                                new Font(null, Font.PLAIN, 15)
                        );
                    }
                    if (e.isMetaDown()) {
                        //右击
                        popupMenu.show(label, e.getX(), e.getY());
                    }
                }
            });
        }

        //再现身
        announcementPanel.setVisible(true);
    }

}
