package com.zjw.domain.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/9 11:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsIdAndGoodsCntForOrder {
    private Integer goodsId;

    private Integer goodsCnt;
}
