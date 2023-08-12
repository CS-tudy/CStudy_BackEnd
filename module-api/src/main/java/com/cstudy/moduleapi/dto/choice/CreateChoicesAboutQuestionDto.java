package com.cstudy.moduleapi.dto.choice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChoicesAboutQuestionDto {
    private int number;
    private String content;
    @Nullable
    private String answer;
}
