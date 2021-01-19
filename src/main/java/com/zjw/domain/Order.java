package com.zjw.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String orderId;

    private String customerId;

    private String customerName;

    private String customerPhone;

    private String saleEmployName;

    private Double orderMoney;

    private Date orderTime;

    //存储药品ID和数量的Map
    private List<GoodsIdAndGoodsCntForOrder> goodsIdMap;

}