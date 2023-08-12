package com.cstudy.moduleapi.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequestDto {
    private String questionTitle;
    private String questionDesc;
    private String questionExplain;
}
