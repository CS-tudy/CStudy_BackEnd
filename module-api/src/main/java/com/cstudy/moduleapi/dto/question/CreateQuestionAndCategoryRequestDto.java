package com.cstudy.moduleapi.dto.question;

import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionAndCategoryRequestDto {

//    @NotEmpty(message = "createQuestionRequestDto 필드는 필수입니다.")
    private CreateQuestionRequestDto createQuestionRequestDto;
//    @NotEmpty(message = "categoryRequestDto 필드는 필수입니다.")
    private CategoryRequestDto categoryRequestDto;
    private List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto;
}
