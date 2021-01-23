package com.zjw.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Option {
    private Integer id;

    private String optionName;

    private String optionDescription;

    private Date optionTime;
}