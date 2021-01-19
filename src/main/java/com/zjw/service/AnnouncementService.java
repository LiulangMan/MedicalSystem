package com.zjw.service;

import com.zjw.domain.Announcement;

import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/7 21:55
 */
public interface AnnouncementService {
    List<Announcement> getAnnouncementTop10();

    List<Announcement> getAnnouncementByTitle(String title);

    void updateAnnouncement(Announcement announcement);

    void insert(Announcement newAnnouncement);

    void deleteAnnouncement(Announcement announcement);

    List<Announcement> queryAll();
}
