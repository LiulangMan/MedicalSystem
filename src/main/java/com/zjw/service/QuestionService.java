package com.zjw.service;

import com.zjw.domain.Question;

import java.util.List;

public interface QuestionService {

    List<Question> queryAll();

    List<Question> queryAllByQuestion(String question);

    int insertQuestion(Question question);

    int deleteQuestionById(Integer id);

    int updateQuestionById(Question question);

    Question queryOneById(Integer id);

    List<Question> queryAllByQuestionUserName(String loginName);
}
