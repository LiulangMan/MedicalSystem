package com.zjw.domain.util;

import com.zjw.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/16 19:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsAndSuppliedId {
    private Integer SupplierId;
    private Goods goods;
}
