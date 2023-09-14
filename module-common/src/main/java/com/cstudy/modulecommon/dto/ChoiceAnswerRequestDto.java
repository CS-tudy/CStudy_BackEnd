package com.cstudy.modulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChoiceAnswerRequestDto {
    @NotEmpty(message = "선택 번호는 필수입니다.")
    private int choiceNumber;

    private Long time;
}
