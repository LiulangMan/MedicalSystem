package com.zjw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    private Integer supplierId;

    private String name;

    private String phone;

    private String address;

}