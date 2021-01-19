package com.zjw.service.impl;

import com.zjw.domain.Goods;
import com.zjw.domain.Order;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import com.zjw.mapper.OrderMapper;
import com.zjw.service.GoodService;
import com.zjw.service.OrderService;
import com.zjw.config.StaticConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/8 19:52
 */
@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderMapper mapper;

    @Autowired
    private GoodService goodService;

    @Override
    public void insert(Order order) {
        //提交
        mapper.insert(order);
        List<GoodsIdAndGoodsCntForOrder> goodsIdMap = order.getGoodsIdMap();
        for (GoodsIdAndGoodsCntForOrder goods : goodsIdMap) {
            mapper.insertOrderAndGoods(order.getOrderId(), goods.getGoodsId(), goods.getGoodsCnt());

            //更新库存
            Goods remain = StaticConfiguration.getGoodsInCache(goods.getGoodsId());
            remain.setGoodStock(remain.getGoodStock() - goods.getGoodsCnt());
            goodService.updateByGoodsId(remain);
        }
    }

    @Override
    public List<Order> queryAll() {
        List<Order> orders = mapper.selectAllSimpleInformation();
        for (Order order : orders) {
            List<GoodsIdAndGoodsCntForOrder> map = mapper.selectAllForGoodsInformationByOrderId(order.getOrderId());
            order.setGoodsIdMap(map);
        }

        return orders;
    }

    @Override
    public List<Order> queryAllOnlyCustomerId(String customerId) {
        List<Order> orders = mapper.selectAllSimpleInformationOnlyCustomerId(customerId);
        for (Order order : orders) {
            List<GoodsIdAndGoodsCntForOrder> map = mapper.selectAllForGoodsInformationByOrderId(order.getOrderId());
            order.setGoodsIdMap(map);
        }
        return orders;
    }

}
