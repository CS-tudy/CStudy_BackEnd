package com.cstudy.modulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChoiceAnswerRequestDto {
    @NotNull(message = "번호를 입력하세요.")
    @Positive(message = "양수를 입력하세요.")
    private int choiceNumber;

    @NotNull(message = "시간을 입력하세요.")
    @Positive(message = "양수를 입력하세요.")
    private Long time;
}
