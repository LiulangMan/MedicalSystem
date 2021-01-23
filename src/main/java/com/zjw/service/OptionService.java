package com.zjw.service;

import com.zjw.domain.Option;

import java.util.Date;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/23 15:04
 */
public interface OptionService {

    List<Option> queryAll();

    int insertOption(Option option);

    int deleteById(Integer id);

    int updateById(Option option);

    int deleteAllBefore(Date date);

    List<Option> queryAllByName(String name);

    List<Option> queryAllByDescription(String description);

    List<Option> queryAllByTime(Date from, Date to);

    Option queryOneById(Integer id);
}
