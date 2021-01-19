package com.zjw.service.impl;

import com.zjw.domain.Goods;
import com.zjw.domain.StockOrder;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import com.zjw.mapper.StockOrderMapper;
import com.zjw.service.GoodService;
import com.zjw.service.StockOrderService;
import com.zjw.config.StaticConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/11 12:55
 */
@Service
public class StockOrderServiceImpl implements StockOrderService {


    @Autowired
    private StockOrderMapper mapper;

    @Autowired
    private GoodService goodService;

    @Override
    public void insert(StockOrder order) {

        //插入采购订单
        mapper.insert(order);
        List<GoodsIdAndGoodsCntForOrder> goodsIdMap = order.getGoodsIdMap();
        for (GoodsIdAndGoodsCntForOrder e : goodsIdMap) {
            mapper.insertGoodsAndCnt(order.getStockId(), e.getGoodsId(), null, e.getGoodsCnt());

        }

        //更新库存列表
        Collection<Goods> stockOrderGoods = StaticConfiguration.getStockOrderGoods().values();
        for (Goods goods : stockOrderGoods) {
            //tmp必定存在
            Goods temp = goodService.queryStockGoodsById(goods.getGoodId());
            goods.setGoodStock(goods.getGoodStock() + temp.getGoodStock());
            goodService.updateStockGoodsById(goods);
        }
    }

    @Override
    public List<StockOrder> queryAll() {
        List<StockOrder> stockOrders = mapper.selectAll();

        for (StockOrder stockOrder : stockOrders) {
            String stockId = stockOrder.getStockId();
            List<GoodsIdAndGoodsCntForOrder> e = mapper.selectGoodsAndCntForOrderId(stockId);
            stockOrder.setGoodsIdMap(e);
        }

        return stockOrders;
    }
}
