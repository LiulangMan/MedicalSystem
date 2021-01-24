package com.zjw.service.impl;

import com.zjw.domain.Customer;
import com.zjw.mapper.CustomerMapper;
import com.zjw.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/4 22:01
 * @Text: 先直接查询数据库，后面改用socket编程方式与服务器连接
 */

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerMapper mapper;

    @Override
    public void insert(Customer customer) {
        mapper.insert(customer);
    }

    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Customer customer) {
        mapper.updateByPrimaryKey(customer);
    }

    @Override
    public Customer selectByIdForOne(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Customer> queryAll() {
        return mapper.selectAll();
    }

    @Override
    public Customer queryByLoginNameForOne(String username) {
        return mapper.selectByLoginUsername(username);
    }

    @Override
    public void deleteByUserName(String userName) {
        mapper.deleteByUserName(userName);
    }

    @Override
    public List<Customer> queryAllByName(String name) {
        return mapper.selectAllByName("%" + name + "%");
    }

    @Override
    public List<Customer> queryByAddress(String address) {
        return mapper.selectAllByAddress("%" + address + "%");
    }

}
