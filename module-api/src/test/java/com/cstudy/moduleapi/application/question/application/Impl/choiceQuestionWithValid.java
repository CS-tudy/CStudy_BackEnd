package com.cstudy.moduleapi.application.question.application.Impl;

import com.cstudy.moduleapi.config.ServiceTestBase;
import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.dto.question.CategoryRequestDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionAndCategoryRequestDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionRequestDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class choiceQuestionWithValid extends ServiceTestBase {


    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("문제 선택 - 성공")
    public void choiceQuestionWithValid() throws Exception {
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

        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .email("mugeon1234@gmail.com")
                .password("1234")
                .name("김무건")
                .build();
        memberService.signUpForTest(memberSignupRequest);

        String email = "mugeon1234@gmail.com";
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberEmail(email));

        ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
                .choiceNumber(3)
                .time(30L)
                .build();
        //when
        questionService.choiceQuestion(LoginUserDto.builder()
                .memberId(member.getId())
                .build(), 1L, choiceAnswerRequestDto);

        //Then
        assertThat(memberQuestionRepository.count()).isNotNull();
    }
}
