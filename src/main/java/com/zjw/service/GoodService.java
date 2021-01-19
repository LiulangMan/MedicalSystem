package com.zjw.service;

import com.zjw.domain.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodService {
    List<Goods> queryAll();

    void insert(Goods test);

    /*模糊查询药名*/
    List<Goods> queryAllForName(String name);

    List<Goods> queryAllForDescription(String description);

    Goods queryById(Integer id);

    List<Goods> queryByLeftPrice(Double price);

    void updateByGoodsId(Goods goods);

    /*采购列表*/
    void insertStockList(Goods goods);

    List<Goods> queryAllStockGoods();

    int queryMaxStockGoodsId();

    boolean checkIdOrNameInDataBase(int id, String name);

    void deleteByGoodsId(Integer goodId);

    void deleteByStockGoodsId(Integer id);

    int updateStockGoodsById(Goods goods);

    Goods queryStockGoodsById(Integer id);
}
