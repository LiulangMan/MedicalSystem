package com.zjw.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.zjw.domain.Question;
import com.zjw.mapper.QuestionMapper;
import com.zjw.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/20 12:59
 */
@Service
public class QuestionServiceImpl implements QuestionService {


    @Autowired
    private QuestionMapper mapper;

    @Override
    public List<Question> queryAll() {
        return mapper.selectAll();
    }

    @Override
    public List<Question> queryAllByQuestion(String question) {
        //使用jie-ba库进行分词搜索
        //使用set去重
        Set<Question> set = new HashSet<>();

        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> process = segmenter.process(question, JiebaSegmenter.SegMode.SEARCH);
        for (SegToken segToken : process) {
            List<Question> questionList = mapper.selectAllByQuestion("%" + segToken.word + "%");
            set.addAll(questionList);
        }
        List<Question> questions = new ArrayList<>(set);
        return questions;
    }

    @Override
    public int insertQuestion(Question question) {
        return mapper.insert(question);
    }

    @Override
    public int deleteQuestionById(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateQuestionById(Question question) {
        return mapper.updateByPrimaryKey(question);
    }

    @Override
    public Question queryOneById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Question> queryAllByQuestionUserName(String loginName) {
        return mapper.selectAllByQuestionUserName(loginName);
    }
}
