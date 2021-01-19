package com.zjw.service.impl;

import com.zjw.domain.Employ;
import com.zjw.mapper.EmployMapper;
import com.zjw.service.EmployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/4 20:48
 */

@Service
public class EmployServiceImpl implements EmployService {

    @Autowired
    EmployMapper mapper;

    @Override
    public void insert(Employ employ) {
        mapper.insert(employ);
    }

    @Override
    public void deleteById(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Employ employ) {
        mapper.updateByPrimaryKey(employ);
    }

    @Override
    public Employ selectByIdForOne(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Employ> queryAll() {
        return mapper.selectAll();
    }

    @Override
    public Employ selectByLoginNameForOne(String username) {
        return mapper.selectByUserNameForOne(username);
    }

    @Override
    public void deleteByUserName(String userName) {
        mapper.deleteByUserName(userName);
    }
}
