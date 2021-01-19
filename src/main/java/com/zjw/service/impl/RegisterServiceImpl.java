package com.zjw.service.impl;

import com.zjw.domain.Register;
import com.zjw.mapper.RegisterMapper;
import com.zjw.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/5 18:13
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    RegisterMapper mapper;


    @Override
    public boolean checkRegisterId(String id, String address) {
        Register register = mapper.selectByPrimaryKey(id);
        return register != null && register.getRegisterAddress().equals(address);
    }
}
