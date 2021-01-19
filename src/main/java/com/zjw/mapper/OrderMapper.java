package com.zjw.mapper;

import com.zjw.domain.Order;
import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(Order record);

    int insertOrderAndGoods(String orderId, Integer goodId, Integer goodCnt);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String orderId);

    List<Order> selectAllSimpleInformation();

    List<Order> selectAllSimpleInformationOnlyCustomerId(@Param("customerId") String customerId);

    List<GoodsIdAndGoodsCntForOrder> selectAllForGoodsInformationByOrderId(@Param("orderId") String orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}