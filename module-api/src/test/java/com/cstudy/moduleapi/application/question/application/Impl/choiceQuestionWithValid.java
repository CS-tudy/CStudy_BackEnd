package com.cstudy.moduleapi.application.question.application.Impl;

import com.cstudy.moduleapi.application.member.impl.MemberCacheRepository;
import com.cstudy.moduleapi.config.ServiceTestBase;
import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.dto.question.CategoryRequestDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionAndCategoryRequestDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionRequestDto;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class choiceQuestionWithValid extends ServiceTestBase {

    @Autowired
    private MemberCacheRepository memberCacheRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("문제 선택 - 성공")
    public void 단일_문제_선택하기_성공_200_반환() throws Exception {
        //given
        questionService.createQuestionChoice(CreateQuestionAndCategoryRequestDto.builder()
                .createQuestionRequestDto(CreateQuestionRequestDto.builder()
                        .questionTitle("문제 제목")
                        .questionDesc("문제에 대한 설명")
                        .questionExplain("문제에 대한 해답")
                        .build())
                .categoryRequestDto(CategoryRequestDto.builder()
                        .category("네트워크")
                        .build())
                .createChoicesAboutQuestionDto(List.of(
                        CreateChoicesAboutQuestionDto.builder().number(1).content("선택 1").build(),
                        CreateChoicesAboutQuestionDto.builder().number(2).content("선택 2").build(),
                        CreateChoicesAboutQuestionDto.builder().number(3).content("선택 3").answer("정답").build(),
                        CreateChoicesAboutQuestionDto.builder().number(4).content("선택 4").build()
                ))
                .build());

        final String email = MemberTestEnum.CUSTOM_EMAIL.getMessage();
        final String name = "이름";

        memberService.signUp(MemberSignupRequest.builder()
                .email(email)
                .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                .name(name)
                .build());

        memberCacheRepository.setMember(memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberEmail(email)));

        ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
                .choiceNumber(3)
                .time(30L)
                .build();
        //when
        questionService.choiceQuestion(LoginUserDto.builder()
                .memberId(memberRepository.findByEmail(email)
                        .orElseThrow(() -> new NotFoundMemberEmail(email)).getId())
                .build(), 1L, choiceAnswerRequestDto);

        //Then
        assertThat(memberQuestionRepository.count()).isNotNull();
    }
}
