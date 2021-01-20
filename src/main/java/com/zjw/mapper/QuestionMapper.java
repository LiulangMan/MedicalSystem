package com.zjw.mapper;

import com.zjw.domain.Question;

import java.util.List;

public interface QuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKey(Question record);

    List<Question> selectAll();

    List<Question> selectAllByQuestion(String question);

    List<Question> selectAllByQuestionUserName(String loginName);
}