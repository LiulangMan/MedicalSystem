package com.zjw.mapper;

import com.zjw.domain.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {

    int deleteByPrimaryKey(Integer goodId);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer goodId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    List<Goods> selectAll();

    List<Goods> selectAllForName(String name);

    List<Goods> selectAllForDescription(String description);

    List<Goods> selectByLeftPrice(Double price);

    Goods selectOneByName(String name);

    /*采购相关*/

    int insertToStockList(Goods goods);

    List<Goods> selectAllStockGoods();

    int selectMaxStockGoodsId();

    int deleteStockGoodsById(Integer id);

    int updateStockGoodsById(Goods goods);

    Goods selectStockOneById(Integer id);
}