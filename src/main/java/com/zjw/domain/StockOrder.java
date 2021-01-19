package com.zjw.domain;

import java.util.Date;
import java.util.List;

import com.zjw.domain.util.GoodsIdAndGoodsCntForOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockOrder {
    private String stockId;

    private String stockEmploy;

    private Double stockMoney;

    private Date stockTime;

    private List<GoodsIdAndGoodsCntForOrder> goodsIdMap;
}