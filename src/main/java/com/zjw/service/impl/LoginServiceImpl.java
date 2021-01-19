package com.zjw.service.impl;

import com.zjw.domain.InfoLogin;
import com.zjw.mapper.InfoLoginMapper;
import com.zjw.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/13 19:15
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private InfoLoginMapper mapper;

    @Override
    public boolean recordLogin(InfoLogin infoLogin) {
        int cnt = mapper.checkUserOnline(infoLogin.getUsername(), infoLogin.getLoginType());
        if (cnt != 0) {
            //已经登陆了
            return false;
        }

        //记录登陆
        mapper.insertRecord(infoLogin);
        //记录在线
        mapper.insertToOnline(infoLogin.getUsername(), infoLogin.getLoginType());
        return true;
    }

    @Override
    public List<InfoLogin> queryAll() {
        return mapper.selectAll();
    }

    @Override
    public List<InfoLogin> queryAllByUsername(String username) {
        return mapper.selectAllByUsername(username);
    }

    @Override
    public List<InfoLogin> queryAllByType(Integer type) {
        return mapper.selectAllByType(type);
    }

    @Override
    public List<InfoLogin> queryAllByUsernameAndType(String username, Integer type) {
        return mapper.selectAllByUsernameAndType(username,type);
    }

    @Override
    public List<InfoLogin> queryAllByTime(Date from, Date to) {
        return mapper.selectAllByTime(from, to);
    }

    @Override
    public void logout(String loginName, Integer type) {
        mapper.deleteUserOnline(loginName, type);
    }

    @Override
    public List<InfoLogin> queryAllOnline() {
        return mapper.selectAllOnline();
    }
}
