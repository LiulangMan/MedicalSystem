package com.zjw.domain;

import java.util.Date;
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
}