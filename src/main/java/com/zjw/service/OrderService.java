package com.zjw.service;

import com.zjw.domain.Order;

import java.util.List;

public interface OrderService {

    void insert(Order order);

    List<Order> queryAll();

    List<Order> queryAllOnlyCustomerId(String customerId);



}
