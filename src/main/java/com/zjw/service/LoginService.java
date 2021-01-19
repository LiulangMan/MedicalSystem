package com.zjw.service;

import com.zjw.domain.InfoLogin;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface LoginService {

    boolean recordLogin(InfoLogin infoLogin);

    List<InfoLogin> queryAll();

    List<InfoLogin> queryAllByUsername(String username);

    List<InfoLogin> queryAllByType(Integer type);

    List<InfoLogin> queryAllByUsernameAndType(String username,Integer type);

    List<InfoLogin> queryAllByTime(Date from,Date to);

    void logout(String loginName, Integer type);

    List<InfoLogin> queryAllOnline();
}
