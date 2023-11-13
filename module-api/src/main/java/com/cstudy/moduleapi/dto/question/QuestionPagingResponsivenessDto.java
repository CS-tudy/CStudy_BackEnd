package com.cstudy.moduleapi.dto.question;

import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.domain.request.Request;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPagingResponsivenessDto {
    private Long questionId;
    private String questionTitle;
    private String categoryTitle;

    public static QuestionPagingResponsivenessDto of(Question question) {
        return QuestionPagingResponsivenessDto.builder()
                .questionId(question.getId())
                .questionTitle(question.getTitle())
                .categoryTitle(question.getCategory().getCategoryTitle())
                .build();
    }
}
