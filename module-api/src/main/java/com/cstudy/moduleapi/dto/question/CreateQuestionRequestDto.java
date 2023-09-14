package com.cstudy.moduleapi.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequestDto {
    @NotEmpty(message = "제목을 입력하세요")
    private String questionTitle;
    @NotEmpty(message = "문제에 대한 설명을 입력하세요")
    private String questionDesc;
    @NotEmpty(message = "정답에 대해 설명의 필드를 추가하세요")
    private String questionExplain;
}
