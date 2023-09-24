package com.cstudy.modulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChoiceAnswerRequestDto {
    @Positive(message = "번호를 입력하세요.")
    private int choiceNumber;

    private Long time;
}
