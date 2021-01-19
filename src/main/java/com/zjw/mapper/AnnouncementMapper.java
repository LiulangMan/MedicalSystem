package com.zjw.mapper;

import com.zjw.domain.Announcement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnnouncementMapper {

    int insert(Announcement record);

    int insertSelective(Announcement record);

    List<Announcement> selectTopNAnnouncement(Integer n);

    List<Announcement> selectAllByTitle(@Param("title") String title);

    void updateAnnouncement(Announcement announcement);

    void deleteAnnouncement(Announcement announcement);

    List<Announcement> selectAll();
}