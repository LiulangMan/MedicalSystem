package com.zjw.mapper;

import com.zjw.domain.StockOrder;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StockOrderMapper {
    int deleteByPrimaryKey(String stockId);

    int insert(StockOrder record);

    int insertSelective(StockOrder record);

    StockOrder selectByPrimaryKey(String stockId);

    int updateByPrimaryKeySelective(StockOrder record);

    int updateByPrimaryKey(StockOrder record);

    int insertGoodsAndCnt(@Param("orderId") String orderId,@Param("goodsId") Integer goodsId,@Param("suppliedId") Integer supplierId,@Param("cnt") Integer cnt);


    List<StockOrder> selectAll();

    List<GoodsIdAndGoodsCntForOrder> selectGoodsAndCntForOrderId(@Param("orderId") String orderId);
}