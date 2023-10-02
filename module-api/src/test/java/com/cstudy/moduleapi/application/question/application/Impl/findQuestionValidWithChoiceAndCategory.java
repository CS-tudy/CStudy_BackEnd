package com.cstudy.moduleapi.application.question.application.Impl;

import com.cstudy.moduleapi.config.ServiceTestBase;
import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import com.cstudy.moduleapi.dto.question.CategoryRequestDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionAndCategoryRequestDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionRequestDto;
import com.cstudy.moduleapi.dto.question.QuestionResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class findQuestionValidWithChoiceAndCategory extends ServiceTestBase {


    @BeforeEach
    void setUp() {
        CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                .category("네트워크")
                .build();

        CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                .questionTitle("문제 제목")
                .questionDesc("문제에 대한 설명")
                .questionExplain("문제에 대한 해답")
                .build();

        CreateChoicesAboutQuestionDto request1 = CreateChoicesAboutQuestionDto.builder()
                .number(1)
                .content("선택 1")
                .build();

        CreateChoicesAboutQuestionDto request2 = CreateChoicesAboutQuestionDto.builder()
                .number(2)
                .content("선택 2")
                .build();
        CreateChoicesAboutQuestionDto request3 = CreateChoicesAboutQuestionDto.builder()
                .number(3)
                .content("선택 3")
                .answer("정답")
                .build();
        CreateChoicesAboutQuestionDto request4 = CreateChoicesAboutQuestionDto.builder()
                .number(4)
                .content("선택 4")
                .build();


        List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto = new ArrayList<>();
        createChoicesAboutQuestionDto.add(request1);
        createChoicesAboutQuestionDto.add(request2);
        createChoicesAboutQuestionDto.add(request3);
        createChoicesAboutQuestionDto.add(request4);

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
                .createQuestionRequestDto(createQuestionRequestDto)
                .categoryRequestDto(categoryRequestDto)
                .createChoicesAboutQuestionDto(createChoicesAboutQuestionDto)
                .build();

        questionService.createQuestionChoice(createQuestionAndCategoryRequestDto);
    }

    @Test
    @DisplayName("생성된 문제에 대한 문제 찾기 및 카테고리")
    public void findQuestionValidWithChoiceAndCategory() throws Exception {
        //given
        CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                .category("네트워크")
                .build();

        CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                .questionTitle("문제 제목")
                .questionDesc("문제에 대한 설명")
                .questionExplain("문제에 대한 해답")
                .build();

        CreateChoicesAboutQuestionDto request1 = CreateChoicesAboutQuestionDto.builder()
                .number(1)
                .content("선택 1")
                .build();

        CreateChoicesAboutQuestionDto request2 = CreateChoicesAboutQuestionDto.builder()
                .number(2)
                .content("선택 2")
                .build();
        CreateChoicesAboutQuestionDto request3 = CreateChoicesAboutQuestionDto.builder()
                .number(3)
                .content("선택 3")
                .answer("정답")
                .build();
        CreateChoicesAboutQuestionDto request4 = CreateChoicesAboutQuestionDto.builder()
                .number(4)
                .content("선택 4")
                .build();


        List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto = new ArrayList<>();
        createChoicesAboutQuestionDto.add(request1);
        createChoicesAboutQuestionDto.add(request2);
        createChoicesAboutQuestionDto.add(request3);
        createChoicesAboutQuestionDto.add(request4);

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
                .createQuestionRequestDto(createQuestionRequestDto)
                .categoryRequestDto(categoryRequestDto)
                .createChoicesAboutQuestionDto(createChoicesAboutQuestionDto)
                .build();

        questionService.createQuestionChoice(createQuestionAndCategoryRequestDto);
        //when
        QuestionResponseDto result = questionService.findQuestionWithChoiceAndCategory(1L);
        //Then
        assertAll(
                () -> assertThat(result.getTitle()).isEqualTo("문제 제목"),
                () -> assertThat(result.getDescription()).isEqualTo("문제에 대한 설명"),
                () -> assertThat(result.getExplain()).isEqualTo("문제에 대한 해답")
        );

        assertAll(
                () -> assertThat(result.getChoices().get(0).getContent()).isEqualTo("선택 1"),
                () -> assertThat(result.getChoices().get(1).getContent()).isEqualTo("선택 2"),
                () -> assertThat(result.getChoices().get(2).getContent()).isEqualTo("선택 3"),
                () -> assertThat(result.getChoices().get(3).getContent()).isEqualTo("선택 4")

        );

        assertAll(
                () -> assertThat(result.getChoices().get(0).getNumber()).isEqualTo(1),
                () -> assertThat(result.getChoices().get(1).getNumber()).isEqualTo(2),
                () -> assertThat(result.getChoices().get(2).getNumber()).isEqualTo(3),
                () -> assertThat(result.getChoices().get(3).getNumber()).isEqualTo(4)
        );

        assertThat(result.getCategoryTitle()).isEqualTo("네트워크");
    }
}
