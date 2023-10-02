package com.cstudy.moduleapi.dto.question;

import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionAndCategoryRequestDto {

    @Valid
    @NotNull(message = "문제 createQuestionRequestDto를 입력하세요")
    private CreateQuestionRequestDto createQuestionRequestDto;

    @Valid
    @NotNull(message = "categoryRequestDto를 입력하세요")
    private CategoryRequestDto categoryRequestDto;

    @NotNull(message = "createChoicesAboutQuestionDto를 4개를 입력하세요.")
    @Size(min = 4, max = 4, message = "선택지는 정확히 4개여야 합니다.")
    private List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto;
}
