package com.cstudy.moduleapi.domain.question.application.Impl;

import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.question.QuestionService;
import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import com.cstudy.moduleapi.dto.member.MemberLoginRequest;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.dto.question.CategoryRequestDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionAndCategoryRequestDto;
import com.cstudy.moduleapi.dto.question.CreateQuestionRequestDto;
import com.cstudy.moduleapi.dto.question.QuestionResponseDto;
import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import com.cstudy.modulecommon.dto.QuestionSearchCondition;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.question.MemberQuestionRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class QuestionServiceImplTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberQuestionRepository memberQuestionRepository;

    @Test
    @DisplayName("문제에 대한 제목, 설명, 정답 설명")
    public void createQuestionWithChoice_Valid() throws Exception {
        //given
        new findQuestionWithChoiceAndCategory().beforeCreateSet();

        Question question = questionRepository.findById(1L)
                .orElseThrow();
        //Then
        assertThat(questionRepository.count()).isNotNull();
        assertThat(question.getDescription()).isEqualTo("문제에 대한 설명");
        assertThat(question.getExplain()).isEqualTo("문제에 대한 해답");
        assertThat(question.getTitle()).isEqualTo("문제 제목");
    }

    @Test
    @DisplayName("recursiveCreateQuestionChoice 테스트")
    public void recursiveCreateQuestionChoice_Valid() {
        // Given
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

        List<CreateQuestionAndCategoryRequestDto> requestDtos = new ArrayList<>();
        requestDtos.add(createQuestionAndCategoryRequestDto);

        // When
        questionService.recursiveCreateQuestionChoice(requestDtos);

        // Then
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(1);

        Question question = questions.get(0);
        assertThat(question.getTitle()).isEqualTo("문제 제목");
        assertThat(question.getDescription()).isEqualTo("문제에 대한 설명");
        assertThat(question.getExplain()).isEqualTo("문제에 대한 해답");

        List<Choice> choices = question.getChoices();
        assertThat(choices.get(0).getContent()).isEqualTo("선택 1");
        assertThat(choices.get(1).getContent()).isEqualTo("선택 2");
        assertThat(choices.get(2).getContent()).isEqualTo("선택 3");
        assertThat(choices.get(3).getContent()).isEqualTo("선택 4");
        assertThat(choices.get(2).isAnswer()).isTrue();
    }

    @DisplayName("문제 find & category")
    @Nested
    class findQuestionWithChoiceAndCategory {

        void beforeCreateSet() {
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

        @BeforeEach
        void setUp() {
            beforeCreateSet();
        }

//        @Test
//        @DisplayName("생성된 문제에 대한 문제 찾기 및 카테고리")
//        public void findQuestionValidWithChoiceAndCategory() throws Exception {
//            //given
//            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
//                    .category("네트워크")
//                    .build();
//
//            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
//                    .questionTitle("문제 제목")
//                    .questionDesc("문제에 대한 설명")
//                    .questionExplain("문제에 대한 해답")
//                    .build();
//
//            CreateChoicesAboutQuestionDto request1 = CreateChoicesAboutQuestionDto.builder()
//                    .number(1)
//                    .content("선택 1")
//                    .build();
//
//            CreateChoicesAboutQuestionDto request2 = CreateChoicesAboutQuestionDto.builder()
//                    .number(2)
//                    .content("선택 2")
//                    .build();
//            CreateChoicesAboutQuestionDto request3 = CreateChoicesAboutQuestionDto.builder()
//                    .number(3)
//                    .content("선택 3")
//                    .answer("정답")
//                    .build();
//            CreateChoicesAboutQuestionDto request4 = CreateChoicesAboutQuestionDto.builder()
//                    .number(4)
//                    .content("선택 4")
//                    .build();
//
//
//            List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto = new ArrayList<>();
//            createChoicesAboutQuestionDto.add(request1);
//            createChoicesAboutQuestionDto.add(request2);
//            createChoicesAboutQuestionDto.add(request3);
//            createChoicesAboutQuestionDto.add(request4);
//
//            CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
//                    .createQuestionRequestDto(createQuestionRequestDto)
//                    .categoryRequestDto(categoryRequestDto)
//                    .createChoicesAboutQuestionDto(createChoicesAboutQuestionDto)
//                    .build();
//
//            questionService.createQuestionChoice(createQuestionAndCategoryRequestDto);
//            //when
//            QuestionResponseDto result = questionService.findQuestionWithChoiceAndCategory(1L);
//            //Then
//            assertAll(
//                    () -> assertThat(result.getTitle()).isEqualTo("문제 제목"),
//                    () -> assertThat(result.getDescription()).isEqualTo("문제에 대한 설명"),
//                    () -> assertThat(result.getExplain()).isEqualTo("문제에 대한 해답")
//            );
//
//            assertAll(
//                    () -> assertThat(result.getChoices().get(0).getContent()).isEqualTo("선택 1"),
//                    () -> assertThat(result.getChoices().get(1).getContent()).isEqualTo("선택 2"),
//                    () -> assertThat(result.getChoices().get(2).getContent()).isEqualTo("선택 3"),
//                    () -> assertThat(result.getChoices().get(3).getContent()).isEqualTo("선택 4")
//
//            );
//
//            assertAll(
//                    () -> assertThat(result.getChoices().get(0).getNumber()).isEqualTo(1),
//                    () -> assertThat(result.getChoices().get(1).getNumber()).isEqualTo(2),
//                    () -> assertThat(result.getChoices().get(2).getNumber()).isEqualTo(3),
//                    () -> assertThat(result.getChoices().get(3).getNumber()).isEqualTo(4)
//            );
//
//            assertThat(result.getCategoryTitle()).isEqualTo("네트워크");
//        }
//    }

        @Nested
        class test {

            @BeforeEach
            void setUp() {
                new findQuestionWithChoiceAndCategory().beforeCreateSet();

                MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                        .email("test1234@gmail.com")
                        .password("1234")
                        .name("김무건")
                        .build();
                memberService.signUp(memberSignupRequest);
            }

//        @Test
//        @DisplayName("문제 선택 - 실패")
//        public void choiceQuestionWithInValid() throws Exception {
//            //given
//            MemberLoginRequest request = MemberLoginRequest.builder()
//                    .email("test1234@gmail.com")
//                    .password("1234")
//                    .build();
//
//            MemberLoginResponse login = memberService.login(request);
//
//            Member member = memberRepository.findByEmail(login.getEmail())
//                    .orElseThrow(() -> new NotFoundMemberEmail(login.getEmail()));
//
//            LoginUserDto loginUserDto = LoginUserDto.builder()
//                    .memberId(member.getId())
//                    .build();
//
//            ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
//                    .choiceNumber(1)
//                    .build();
//            //when
//            questionService.choiceQuestion(loginUserDto, 1L, choiceAnswerRequestDto);
//
//            MemberQuestion memberQuestion = memberQuestionRepository.findById(1L)
//                    .orElseThrow(RuntimeException::new);
//            //Then
//            assertThat(memberQuestion.getFail()).isEqualTo(1);
//        }

//        @Test
//        @DisplayName("문제 선택 - 성공")
//        public void choiceQuestionWithValid() throws Exception {
//            //given
//            MemberLoginRequest request = MemberLoginRequest.builder()
//                    .email("test1234@gmail.com")
//                    .password("1234")
//                    .build();
//
//            MemberLoginResponse login = memberService.login(request);
//
//            Member member = memberRepository.findByEmail(login.getEmail())
//                    .orElseThrow(() -> new NotFoundMemberEmail(login.getEmail()));
//
//            LoginUserDto loginUserDto = LoginUserDto.builder()
//                    .memberId(member.getId())
//                    .build();
//
//            ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
//                    .choiceNumber(3)
//                    .build();
//            //when
//            questionService.choiceQuestion(loginUserDto, 1L, choiceAnswerRequestDto);
//
//            MemberQuestion memberQuestion = memberQuestionRepository.findById(1L)
//                    .orElseThrow(RuntimeException::new);
//            //Then
//            assertThat(memberQuestion.getSuccess()).isEqualTo(3);
//        }

            @Test
            @DisplayName("페이징 문제 및 카테고리")
            public void findPagingQuestionAndCategoryWithValid() throws Exception {
                //given
                LoginUserDto loginUserDto = LoginUserDto.builder()
                        .memberId(1L)
                        .build();
                //when
                QuestionSearchCondition questionSearchCondition = QuestionSearchCondition.builder()
                        .build();
                //Then
                Page<QuestionPageWithCategoryAndTitle> questionPageWithCategoryAndTitles = questionService.questionPageWithCategory(
                        questionSearchCondition, 0, 10,
                        loginUserDto);

                System.out.println("questionPageWithCategoryAndTitles = " + questionPageWithCategoryAndTitles);

                assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getQuestionTitle)
                        .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("문제 제목");

                assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getCategoryTitle)
                        .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("네트워크");
            }

            @Test
            @DisplayName("페이징 문제 및 카테고리 - 문제 제목")
            public void findPagingQuestionAndCategoryWithValidCondition() throws Exception {
                //given
                LoginUserDto loginUserDto = LoginUserDto.builder()
                        .memberId(1L)
                        .build();
                QuestionSearchCondition questionSearchCondition = QuestionSearchCondition.builder()
                        .questionTitle("문제 제목")
                        .build();
                //when
                Page<QuestionPageWithCategoryAndTitle> questionPageWithCategoryAndTitles = questionService.questionPageWithCategory(
                        questionSearchCondition, 0, 10,
                        loginUserDto);

                //Then

                System.out.println("questionPageWithCategoryAndTitles = " + questionPageWithCategoryAndTitles);

                assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getQuestionTitle)
                        .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("문제 제목");

                assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getCategoryTitle)
                        .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("네트워크");
            }
        }
    }
}