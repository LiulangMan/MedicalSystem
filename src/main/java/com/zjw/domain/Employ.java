package com.zjw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employ {

    private Integer id;

    private String employId;

    private String loginName;

    private String loginPassword;

    private String name;

    private Integer sex;

    private String address;

    private String phone;

    private Integer type;

    private String imagesPath;

    public Employ(Integer id, String employId, String loginName, String loginPassword, String name, Integer sex, String address, String phone, Integer type) {
        this.id = id;
        this.employId = employId;
        this.loginName = loginName;
        this.loginPassword = loginPassword;
        this.name = name;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.type = type;
    }
}