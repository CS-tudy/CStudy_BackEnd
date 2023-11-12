package com.cstudy.modulecommon.dto;

import com.cstudy.modulecommon.domain.question.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionQuestionDto {
    private Long questionId;
    private String questionTitle;
    private String description;
    private List<ChoiceQuestionResponseDto> choices =new ArrayList<>();

    public static CompetitionQuestionDto of(Question question){
        return CompetitionQuestionDto.builder()
                .questionId(question.getId())
                .description(question.getDescription())
                .choices(question.getChoices().stream()
                        .map(ChoiceQuestionResponseDto::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
