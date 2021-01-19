package com.zjw.mapper;

import com.zjw.domain.Customer;

import java.util.List;

public interface CustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    int updateByUsername(Customer record);

    List<Customer> selectAll();

    Customer selectByLoginUsername(String username);

    void deleteByUserName(String userName);

    List<Customer> selectAllByName(String name);

    List<Customer> selectAllByAddress(String address);
}