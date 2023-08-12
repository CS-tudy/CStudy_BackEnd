package com.cstudy.moduleapi.dto.question;

import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.dto.ChoiceQuestionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private String categoryTitle;
    private String title;
    private String description;
    private String explain;
    List<ChoiceQuestionResponseDto> choices = new ArrayList<>();

    public static QuestionResponseDto of(Question question) {
        return QuestionResponseDto.builder()
                .title(question.getTitle())
                .description(question.getDescription())
                .categoryTitle(question.getCategory().getCategoryTitle())
                .explain(question.getExplain())
                .choices(question.getChoices().stream()
                        .map(ChoiceQuestionResponseDto::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
