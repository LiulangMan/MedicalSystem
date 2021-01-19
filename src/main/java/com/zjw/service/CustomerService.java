package com.zjw.service;

import com.zjw.domain.Customer;

import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/4 21:59
 */
public interface CustomerService {
    void insert(Customer employ);

    void delete(Integer id);

    void update(Customer employ);

    Customer selectByIdForOne(Integer id);

    List<Customer> queryAll();

    Customer queryByLoginNameForOne(String username);

    void deleteByUserName(String userName);

    List<Customer> queryAllByName(String name);

    List<Customer> queryByAddress(String address);
}
