package com.zjw.service.impl;

import com.zjw.domain.Option;
import com.zjw.mapper.OptionMapper;
import com.zjw.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/23 15:08
 */

@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionMapper mapper;

    @Override
    public List<Option> queryAll() {
        return mapper.selectAll();
    }

    @Override
    public int insertOption(Option option) {
        return mapper.insert(option);
    }

    @Override
    public int deleteById(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(Option option) {
        return mapper.updateByPrimaryKey(option);
    }

    @Override
    public int deleteAllBefore(Date date) {
        return mapper.deleteAllBefore(date);
    }

    @Override
    public List<Option> queryAllByName(String name) {
        return mapper.selectAllByName("%" + name + "%");
    }

    @Override
    public List<Option> queryAllByDescription(String description) {
        return mapper.selectAllByDescription("%" + description + "%");
    }

    @Override
    public List<Option> queryAllByTime(Date from, Date to) {
        return mapper.selectAllByTime(from, to);
    }

    @Override
    public Option queryOneById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }
}
