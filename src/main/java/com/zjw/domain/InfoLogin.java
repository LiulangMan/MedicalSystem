package com.zjw.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoLogin {
    private String username;

    private Date loginTime;

    private Integer loginType;
}