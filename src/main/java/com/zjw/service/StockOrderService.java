package com.zjw.service;

import com.zjw.domain.StockOrder;

import java.util.List;

public interface StockOrderService {
    void insert(StockOrder order);

    List<StockOrder> queryAll();
}
