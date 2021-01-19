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
}