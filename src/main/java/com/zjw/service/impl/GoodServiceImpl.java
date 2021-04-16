package com.zjw.service.impl;

import com.zjw.config.StaticConfiguration;
import com.zjw.domain.Goods;
import com.zjw.domain.Option;
import com.zjw.mapper.GoodsMapper;
import com.zjw.mapper.SupplierMapper;
import com.zjw.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/7 12:47
 */
@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<Goods> queryAll() {
        return goodsMapper.selectAll();
    }

    @Override
    public void insert(Goods goods) {
        goodsMapper.insert(goods);
    }

    @Override
    public List<Goods> queryAllForName(String name) {
        return goodsMapper.selectAllForName("%" + name + "%");
    }

    @Override
    public List<Goods> queryAllForDescription(String description) {
        return goodsMapper.selectAllForDescription("%" + description + "%");
    }

    @Override
    public Goods queryById(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Goods> queryByLeftPrice(Double price) {
        return goodsMapper.selectByLeftPrice(price);
    }

    @Override
    public void updateByGoodsId(Goods goods) {
        goodsMapper.updateByPrimaryKey(goods);
    }


    @Override
    public void insertStockList(Goods goods) {
        goodsMapper.insertToStockList(goods);
    }

    @Override
    public List<Goods> queryAllStockGoods() {
        return goodsMapper.selectAllStockGoods();
    }

    @Override
    public int queryMaxStockGoodsId() {
        return goodsMapper.selectMaxStockGoodsId();
    }

    @Override
    public boolean checkIdOrNameInDataBase(int id, String name) {
        return (goodsMapper.selectOneFromStockGoodsById(id) != null) || (goodsMapper.selectOneFromStockGoodsByName(name) != null);
    }

    @Override
    public void deleteByGoodsId(Integer goodId) {
        goodsMapper.deleteByPrimaryKey(goodId);
    }

    @Override
    public void deleteByStockGoodsId(Integer id) {
        goodsMapper.deleteStockGoodsById(id);
    }

    @Override
    public int updateStockGoodsById(Goods goods) {
        return goodsMapper.updateStockGoodsById(goods);
    }

    @Override
    public Goods queryStockGoodsById(Integer id) {
        return goodsMapper.selectStockOneById(id);
    }

    @Override
    public int offShelfGoods(Goods goods) {
        this.deleteByGoodsId(goods.getGoodId());

        //更新库存
        Goods stockGoods = StaticConfiguration.getStockGoodsInCache(goods.getGoodId());
        stockGoods.setGoodStock(stockGoods.getGoodStock() + goods.getGoodStock());
        this.updateStockGoodsById(stockGoods);
        return 0;
    }

    @Override
    public int putShelfGoods(Goods stock) {
        Goods temp = new Goods(stock);
        temp.setGoodStock(0);
        this.insert(temp);
        return 0;
    }
}
