package com.zjw.mapper;

import com.zjw.domain.Option;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OptionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Option record);

    int insertSelective(Option record);

    Option selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Option record);

    int updateByPrimaryKey(Option record);

    List<Option> selectAll();

    List<Option> selectAllByName(String name);

    List<Option> selectAllByTime(@Param("from") Date from,@Param("to") Date to);

    List<Option> selectAllByDescription(String description);

    int deleteAllBefore(Date date);
}