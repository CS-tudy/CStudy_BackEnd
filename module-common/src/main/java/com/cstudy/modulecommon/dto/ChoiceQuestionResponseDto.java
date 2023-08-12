package com.cstudy.modulecommon.dto;

import com.cstudy.modulecommon.domain.choice.Choice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChoiceQuestionResponseDto {

    private int number;

    private String content;

    public ChoiceQuestionResponseDto(Choice choice) {
        this.number = choice.getNumber();
        this.content = choice.getContent();
    }
}
