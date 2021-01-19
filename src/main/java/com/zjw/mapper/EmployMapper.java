package com.zjw.mapper;

import com.zjw.domain.Employ;

import java.util.List;

public interface EmployMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employ record);

    int insertSelective(Employ record);

    Employ selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employ record);

    int updateByPrimaryKey(Employ record);

    int updateByUsername(Employ record);

    List<Employ> selectAll();

    Employ selectByUserNameForOne(String username);

    void deleteByUserName(String userName);

    List<Employ> selectAllByName(String name);

    List<Employ> selectAllByAddress(String address);
}