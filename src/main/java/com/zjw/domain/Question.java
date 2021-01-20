package com.zjw.domain;

import java.util.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private Integer id;

    private String question;

    private String answer;

    private Date questionTime;

    private Date answerTime;

    /**
    * 0未解决 1解决
    */
    private Integer status;

    /**
    * 提问者 username
    */
    private String questionUsername;

    /**
    * 回答者 username
    */
    private String responseUsername;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question1 = (Question) o;
        return Objects.equals(getId(), question1.getId()) &&
                Objects.equals(getQuestion(), question1.getQuestion()) &&
                Objects.equals(getAnswer(), question1.getAnswer()) &&
                Objects.equals(getQuestionTime(), question1.getQuestionTime()) &&
                Objects.equals(getAnswerTime(), question1.getAnswerTime()) &&
                Objects.equals(getStatus(), question1.getStatus()) &&
                Objects.equals(getQuestionUsername(), question1.getQuestionUsername()) &&
                Objects.equals(getResponseUsername(), question1.getResponseUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQuestion(), getAnswer(), getQuestionTime(), getAnswerTime(), getStatus(), getQuestionUsername(), getResponseUsername());
    }
}