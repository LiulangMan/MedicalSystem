package com.zjw.swing.home;

import com.zjw.config.FontConfiguration;
import com.zjw.config.StaticConfiguration;
import com.zjw.constant.IndexConstant;
import com.zjw.domain.Announcement;
import com.zjw.service.AnnouncementService;
import com.zjw.swing.message.MessageShowByTable;
import com.zjw.swing.message.MessageShowByText;
import com.zjw.swing.message.MessageShows;
import com.zjw.swing.utils.DefaultJTable;
import com.zjw.utils.DataUtils;
import com.zjw.utils.interfaceImpl.DefaultMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/22 13:06
 */
@Component
public class HomeListFrame {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private HomeEditFrame homeEditFrame;

    @Autowired
    private HomeFrame homeFrame;

    private Map<String, Announcement> announcementHashMap = new HashMap<>();

    private DefaultJTable show;


    public void run() {
        {
            //弹出菜单
            JPopupMenu jPopupMenu = new JPopupMenu();
            JMenuItem refreshListButton = new JMenuItem("刷新");
            JMenuItem editListButton = new JMenuItem("编辑");
            JMenuItem deleteButton = new JMenuItem("删除");
            JMenuItem addListButton = new JMenuItem("添加");
            jPopupMenu.add(refreshListButton);

            //超级管理员权限
            if (StaticConfiguration.getLoginType() == IndexConstant.LOGIN_TYPE_ADMIN) {
                jPopupMenu.add(editListButton);
                jPopupMenu.add(deleteButton);
                jPopupMenu.add(addListButton);
            }

            //表格
            show = MessageShowByTable.show(new Object[]{"题目", "时间"}, null);

            //监听
            show.addMouseListener(new DefaultMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = show.getSelectedRow();
                        String var1 = (String) show.getValueAt(row, 1);
                        String var2 = (String) show.getValueAt(row, 0);
                        Announcement announcement = announcementHashMap.get(var1 + var2);
                        MessageShowByText.show(
                                announcement.getTitle(),
                                announcement.getText(),
                                FontConfiguration.getFont("宋体", 20)
                        );
                    } else if (e.isMetaDown()) {
                        jPopupMenu.show(show, e.getX(), e.getY());
                    }
                }
            });

            refreshListButton.addActionListener(e -> {
                this.refreshData();
            });

            addListButton.addActionListener(e -> {
                StaticConfiguration.addThreadPoolTask(() -> {
                    homeEditFrame.run(null);
                });
            });

            deleteButton.addActionListener(e -> {
                int row = show.getSelectedRow();
                boolean b = MessageShows.ShowMessageAboutMakeSure(show, "确认删除此公告？");
                if (!b) return;

                String var0 = (String) show.getValueAt(row, 0);
                String var1 = (String) show.getValueAt(row, 1);
                Announcement announcement = announcementHashMap.get(var1 + var0);
                announcementService.deleteAnnouncement(announcement);

                this.refreshData();
                homeFrame.refreshAnnouncementPanel();
            });

            editListButton.addActionListener(e -> {
                int row = show.getSelectedRow();
                String var0 = (String) show.getValueAt(row, 0);
                String var1 = (String) show.getValueAt(row, 1);
                Announcement announcement = announcementHashMap.get(var1 + var0);
                StaticConfiguration.addThreadPoolTask(() -> {
                    homeEditFrame.run(announcement);
                });
            });


            this.refreshData();
        }
    }

    public void refreshData() {
        //查询表格
        List<Announcement> announcementList = announcementService.queryAll();
        //如果考虑并发安全，可以使用Time+Title作为Key
        announcementHashMap.clear();
        for (Announcement announcement : announcementList) {
            announcementHashMap.put(DataUtils.defaultDataFormat.format(announcement.getWriteTime()) + announcement.getTitle(), announcement);
        }

        show.refreshData(DataUtils.AnnouncementToArray(announcementList));
    }
}
