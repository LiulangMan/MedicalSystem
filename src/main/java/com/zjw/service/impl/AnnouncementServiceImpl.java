package com.zjw.service.impl;

import com.zjw.domain.Announcement;
import com.zjw.mapper.AnnouncementMapper;
import com.zjw.service.AnnouncementService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/7 21:55
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    AnnouncementMapper mapper;

    @Override
    public List<Announcement> getAnnouncementTop10() {
        return mapper.selectTopNAnnouncement(10);
    }

    @Override
    public List<Announcement> getAnnouncementByTitle(String title) {
        return mapper.selectAllByTitle(title);
    }

    @Override
    public void updateAnnouncement(Announcement announcement) {
        mapper.updateAnnouncement(announcement);
    }

    @Override
    public void insert(Announcement newAnnouncement) {
        mapper.insert(newAnnouncement);
    }

    @Override
    public void deleteAnnouncement(Announcement announcement) {
        mapper.deleteAnnouncement(announcement);
    }

    @Override
    public List<Announcement> queryAll() {
        return mapper.selectAll();
    }

    @Override
    public Announcement queryOneByTitleAndTime(String title, Date data) {
        return mapper.selectOneByTitleAndTime(title, data);
    }
}
