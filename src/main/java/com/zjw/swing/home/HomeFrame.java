package com.zjw.swing.home;

import com.zjw.config.StaticConfiguration;
import com.zjw.constant.IndexConstant;
import com.zjw.domain.Announcement;
import com.zjw.service.AnnouncementService;
import com.zjw.swing.message.MessageShowByTable;
import com.zjw.swing.message.MessageShowByText;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.swing.utils.FontJLabel;
import com.zjw.swing.utils.ImageJPanel;
import com.zjw.utils.DataUtils;
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
    private EditFrame editFrame;

    //弹出菜单
    private JPopupMenu popupMenu;

    private JPanel announcementPanel;

    private Font font = new Font("宋体", Font.BOLD | Font.ITALIC, 20);

    public HomeFrame() {
        super(null, "/images/login/login2.jpg");
    }

    public void init() {
        this.setSize(IndexConstant.CARD_WIDTH, IndexConstant.CARD_HIGH);

        //公告按钮
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

        JMenuItem editMenu = new JMenuItem("编辑");
        editMenu.setFont(new Font(null, Font.PLAIN, 20));

        JMenuItem deleteMenu = new JMenuItem("删除");
        deleteMenu.setFont(new Font(null, Font.PLAIN, 20));

        JMenuItem addMenu = new JMenuItem("添加");
        addMenu.setFont(new Font(null, Font.PLAIN, 20));


        popupMenu.add(refreshMenu);
        if (StaticConfiguration.getLoginType() == IndexConstant.LOGIN_TYPE_ADMIN) {
            popupMenu.add(editMenu);
            popupMenu.add(deleteMenu);
            popupMenu.add(addMenu);
        }

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
        editMenu.addActionListener(e -> {
            Point point = announcementPanel.getMousePosition();
            java.awt.Component component = announcementPanel.getComponentAt(point.x, point.y - 30);
            if (component instanceof FontJLabel) {
                FontJLabel jLabel = (FontJLabel) component;
                StaticConfiguration.addThreadPoolTask(new Runnable() {
                    @Override
                    public void run() {
                        editFrame.run(jLabel.getAnnouncement());
                    }
                });
            }
        });

        deleteMenu.addActionListener(e -> {
            boolean b = MessageShows.ShowMessageAboutDeleteAnnouncement(this);
            if (!b) return;
            Point point = announcementPanel.getMousePosition();
            java.awt.Component component = announcementPanel.getComponentAt(point.x, point.y - 60);
            if (component instanceof FontJLabel) {
                FontJLabel jLabel = (FontJLabel) component;
                announcementService.deleteAnnouncement(jLabel.getAnnouncement());
                refreshAnnouncementPanel();
            }
        });

        addMenu.addActionListener(e -> {
            StaticConfiguration.addThreadPoolTask(() -> {
                editFrame.run(null);
            });

        });

        listButton.addActionListener(e -> {
            Object[] colName = new Object[]{"题目", "时间"};
            List<Announcement> announcementList = announcementService.queryAll();
            //如果考虑并发安全，可以使用Time+Title作为Key
            Map<String, Announcement> announcementHashMap = new HashMap<>();
            for (Announcement announcement : announcementList) {
                announcementHashMap.put(DataUtils.defaultDataFormat.format(announcement.getWriteTime()), announcement);
            }
            DefaultJTable show = MessageShowByTable.show(colName, DataUtils.AnnouncementToArray(announcementList));
            show.addMouseListener(new DefaultMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = show.getSelectedRow();
                        String var1 = (String) show.getValueAt(row, 1);
                        Announcement announcement = announcementHashMap.get(var1);
                        MessageShowByText.show(
                                announcement.getTitle(),
                                announcement.getText(),
                                font
                        );
                    }
                }
            });
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
