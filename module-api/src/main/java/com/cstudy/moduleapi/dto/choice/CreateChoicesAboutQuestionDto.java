package com.cstudy.moduleapi.dto.choice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChoicesAboutQuestionDto {

    @NotBlank(message = "번호를 입력하세요")
    private int number;
    @NotNull(message = "내용을 입력하세요")
    private String content;
    @Nullable
    private String answer;
}
