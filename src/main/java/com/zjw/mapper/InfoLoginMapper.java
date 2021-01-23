package com.zjw.mapper;

import com.zjw.domain.InfoLogin;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface InfoLoginMapper {

    int insertRecord(InfoLogin record);

    int insertSelective(InfoLogin record);

    List<InfoLogin> selectAll();

    List<InfoLogin> selectAllByUsername(String username);

    List<InfoLogin> selectAllByType(Integer type);

    List<InfoLogin> selectAllByTime(@Param("from") Date from, @Param("to") Date to);

    /*和登陆状态相关*/
    int insertToOnline(@Param("username") String name, @Param("type") Integer type);

    int checkUserOnline(@Param("username") String name, @Param("type") Integer type);

    int deleteUserOnline(@Param("username") String name, @Param("type") Integer type);

    List<InfoLogin> selectAllByUsernameAndType(@Param("username") String username,@Param("type") Integer type);

    List<InfoLogin> selectAllOnline();

    int deleteAllBefore(Date time);
}