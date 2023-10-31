package com.cstudy.moduleapi.application.question.application.Impl;

import com.cstudy.moduleapi.config.ServiceTestBase;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import com.cstudy.modulecommon.dto.QuestionSearchCondition;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class QuestionServiceChoiceTest extends ServiceTestBase {




//
//    @Test
//    @DisplayName("문제 선택 - 실패")
//    public void choiceQuestionWithInValid() throws Exception {
//        //given
//        CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
//                .category("네트워크")
//                .build();
//
//        CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
//                .questionTitle("문제 제목")
//                .questionDesc("문제에 대한 설명")
//                .questionExplain("문제에 대한 해답")
//                .build();
//
//        CreateChoicesAboutQuestionDto request1 = CreateChoicesAboutQuestionDto.builder()
//                .number(1)
//                .content("선택 1")
//                .build();
//
//        CreateChoicesAboutQuestionDto request2 = CreateChoicesAboutQuestionDto.builder()
//                .number(2)
//                .content("선택 2")
//                .build();
//        CreateChoicesAboutQuestionDto request3 = CreateChoicesAboutQuestionDto.builder()
//                .number(3)
//                .content("선택 3")
//                .answer("정답")
//                .build();
//        CreateChoicesAboutQuestionDto request4 = CreateChoicesAboutQuestionDto.builder()
//                .number(4)
//                .content("선택 4")
//                .build();
//
//
//        List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto = new ArrayList<>();
//        createChoicesAboutQuestionDto.add(request1);
//        createChoicesAboutQuestionDto.add(request2);
//        createChoicesAboutQuestionDto.add(request3);
//        createChoicesAboutQuestionDto.add(request4);
//
//        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
//                .createQuestionRequestDto(createQuestionRequestDto)
//                .categoryRequestDto(categoryRequestDto)
//                .createChoicesAboutQuestionDto(createChoicesAboutQuestionDto)
//                .build();
//
//        questionService.createQuestionChoice(createQuestionAndCategoryRequestDto);
//
//        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
//                .email("mugeon1234@gmail.com")
//                .password("1234")
//                .name("김무건")
//                .build();
//        memberService.signUpForTest(memberSignupRequest);
//
//        MemberLoginRequest request = MemberLoginRequest.builder()
//                .email("mugeon1234@gmail.com")
//                .password("1234")
//                .build();
//
//        String email = "mugeon1234@gmail.com";
//
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new NotFoundMemberEmail(email));
//
//        LoginUserDto loginUserDto = LoginUserDto.builder()
//                .memberId(member.getId())
//                .build();
//
//        ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
//                .choiceNumber(1)
//                .time(30L)
//                .build();
//        //when
//        questionService.choiceQuestion(loginUserDto, 1L, choiceAnswerRequestDto);
//
//        MemberQuestion memberQuestion = memberQuestionRepository.findById(1L)
//                .orElseThrow(RuntimeException::new);
//        //Then
//        assertThat(memberQuestion.getFail()).isEqualTo(1);
//    }

//    @Test
//    @DisplayName("문제 선택 - 성공")
//    public void choiceQuestionWithValid() throws Exception {
//        //given
//        CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
//                .category("네트워크")
//                .build();
//
//        CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
//                .questionTitle("문제 제목")
//                .questionDesc("문제에 대한 설명")
//                .questionExplain("문제에 대한 해답")
//                .build();
//
//        CreateChoicesAboutQuestionDto request1 = CreateChoicesAboutQuestionDto.builder()
//                .number(1)
//                .content("선택 1")
//                .build();
//
//        CreateChoicesAboutQuestionDto request2 = CreateChoicesAboutQuestionDto.builder()
//                .number(2)
//                .content("선택 2")
//                .build();
//        CreateChoicesAboutQuestionDto request3 = CreateChoicesAboutQuestionDto.builder()
//                .number(3)
//                .content("선택 3")
//                .answer("정답")
//                .build();
//        CreateChoicesAboutQuestionDto request4 = CreateChoicesAboutQuestionDto.builder()
//                .number(4)
//                .content("선택 4")
//                .build();
//
//
//        List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto = new ArrayList<>();
//        createChoicesAboutQuestionDto.add(request1);
//        createChoicesAboutQuestionDto.add(request2);
//        createChoicesAboutQuestionDto.add(request3);
//        createChoicesAboutQuestionDto.add(request4);
//
//        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
//                .createQuestionRequestDto(createQuestionRequestDto)
//                .categoryRequestDto(categoryRequestDto)
//                .createChoicesAboutQuestionDto(createChoicesAboutQuestionDto)
//                .build();
//
//        questionService.createQuestionChoice(createQuestionAndCategoryRequestDto);
//
//        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
//                .email("mugeon1234@gmail.com")
//                .password("1234")
//                .name("김무건")
//                .build();
//        memberService.signUpForTest(memberSignupRequest);
//
//        String email = "mugeon1234@gmail.com";
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new NotFoundMemberEmail(email));
//
//        ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
//                .choiceNumber(3)
//                .time(30L)
//                .build();
//        //when
//        questionService.choiceQuestion(LoginUserDto.builder()
//                .memberId(member.getId())
//                .build(), 1L, choiceAnswerRequestDto);
//
//        MemberQuestion memberQuestion = memberQuestionRepository.findById(1L)
//                .orElseThrow(RuntimeException::new);
//        //Then
//        assertThat(memberQuestion.getSuccess()).isEqualTo(3);
//    }
//
//    @Test
//    @DisplayName("페이징 문제 및 카테고리")
//    public void findPagingQuestionAndCategoryWithValid() throws Exception {
//        //given
//        LoginUserDto loginUserDto = LoginUserDto.builder()
//                .memberId(1L)
//                .build();
//        //when
//        QuestionSearchCondition questionSearchCondition = QuestionSearchCondition.builder()
//                .build();
//        //Then
//        Page<QuestionPageWithCategoryAndTitle> questionPageWithCategoryAndTitles = questionService.questionPageWithCategory(
//                questionSearchCondition, 0, 10,
//                loginUserDto);
//
//        System.out.println("questionPageWithCategoryAndTitles = " + questionPageWithCategoryAndTitles);
//
//        assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getQuestionTitle)
//                .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("문제 제목");
//
//        assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getCategoryTitle)
//                .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("네트워크");
//    }
//
//    @Test
//    @DisplayName("페이징 문제 및 카테고리 - 문제 제목")
//    public void findPagingQuestionAndCategoryWithValidCondition() throws Exception {
//        //given
//        LoginUserDto loginUserDto = LoginUserDto.builder()
//                .memberId(1L)
//                .build();
//        QuestionSearchCondition questionSearchCondition = QuestionSearchCondition.builder()
//                .questionTitle("문제 제목")
//                .build();
//        //when
//        Page<QuestionPageWithCategoryAndTitle> questionPageWithCategoryAndTitles = questionService.questionPageWithCategory(
//                questionSearchCondition, 0, 10,
//                loginUserDto);
//
//        //Then
//
//        System.out.println("questionPageWithCategoryAndTitles = " + questionPageWithCategoryAndTitles);
//
//        assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getQuestionTitle)
//                .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("문제 제목");
//
//        assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getCategoryTitle)
//                .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("네트워크");
//    }
}
