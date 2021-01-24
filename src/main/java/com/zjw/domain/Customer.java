package com.zjw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private Integer id;

    private String customerId;

    private String loginName;

    private String loginPassword;

    private String name;

    private Integer sex;

    private String address;

    private String phone;

    private String imagesPath;

    public Customer(Integer id, String customerId, String loginName, String loginPassword, String name, Integer sex, String address, String phone) {
        this.id = id;
        this.customerId = customerId;
        this.loginName = loginName;
        this.loginPassword = loginPassword;
        this.name = name;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
    }
}