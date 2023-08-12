package com.cstudy.moduleapi.dto.question;

import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionAndCategoryRequestDto {
    private CreateQuestionRequestDto createQuestionRequestDto;
    private CategoryRequestDto categoryRequestDto;
    private List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto;
}
