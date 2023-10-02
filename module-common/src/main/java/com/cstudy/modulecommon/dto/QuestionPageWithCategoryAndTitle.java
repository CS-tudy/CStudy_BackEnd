package com.cstudy.modulecommon.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class QuestionPageWithCategoryAndTitle {
    private Long questionId;
    private String questionTitle;
    private String categoryTitle;
    private int status;

    @QueryProjection
    public QuestionPageWithCategoryAndTitle(Long questionId, String questionTitle, String categoryTitle, int status) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.categoryTitle = categoryTitle;
        this.status = status;
    }


    public static QuestionPageWithCategoryAndTitle createQuestionPageWithCategoryAndTitle(Long questionId, String questionTitle, String categoryTitle, int status) {
        return new QuestionPageWithCategoryAndTitle(questionId, questionTitle, categoryTitle, status);
    }
}
