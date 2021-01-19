package com.zjw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    private Integer goodId;

    private String goodName;

    private Integer goodStock;

    private String goodText;

    private Double goodMoney;

    private Integer goodType;

    private Integer supplierId;

    public Goods(Integer goodId, String goodName, Integer goodStock, String goodText, Double goodMoney, Integer goodType) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.goodStock = goodStock;
        this.goodText = goodText;
        this.goodMoney = goodMoney;
        this.goodType = goodType;
    }

    public Goods(Goods goods) {
        this.goodId = goods.getGoodId();
        this.goodName = goods.getGoodName();
        this.goodStock = goods.getGoodStock();
        this.goodText = goods.getGoodText();
        this.goodMoney = goods.getGoodMoney();
        this.goodType = goods.getGoodType();
        this.supplierId = goods.getSupplierId();
    }
}