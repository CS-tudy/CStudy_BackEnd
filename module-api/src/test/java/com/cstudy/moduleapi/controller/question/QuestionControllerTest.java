package com.cstudy.moduleapi.controller.question;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import com.cstudy.moduleapi.dto.question.*;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.dto.ChoiceQuestionResponseDto;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import com.cstudy.modulecommon.dto.QuestionSearchCondition;
import com.cstudy.moduleapi.dto.question.QuestionResponseDto;
import com.cstudy.modulecommon.error.question.NotFoundQuestionWithChoicesAndCategoryById;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle.createQuestionPageWithCategoryAndTitle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class QuestionControllerTest extends ControllerTest {



    @DisplayName("POST /api/questions 문제 생성하기")
    @Nested
    class 문제_생성하기 {

        @Test
        public void 문제_생성하기_성공_200() throws Exception {
            //given
            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionDesc("스프링의 특징에 대해 자세하게 학습할 수 있습니다.")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();
            //when
            ApiResponse<String> response = questionMockApiCaller.createQuestionChoice(requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(201);
        }

        @Test
        public void 일반_유저가_문제_생성을_항_403() throws Exception {

            String url = "/api/questions";

            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionDesc("스프링의 특징에 대해 자세하게 학습할 수 있습니다.")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_USER_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(403),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("403"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("Access Denied: Access is denied")
            );
        }


        @Test
        public void 문제_생성하기_토큰_없음_401() throws Exception {

            String url = "/api/questions";

            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionDesc("스프링의 특징에 대해 자세하게 학습할 수 있습니다.")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithNoAuthorization_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(401),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("401"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("Headers에 토큰 형식의 값 찾을 수 없음")
            );
        }

        @DisplayName("createQuestionRequestDto 없음 -> 유효성 검사 실패")
        @Test
        public void 문제_생성하기_유효성_검사() throws Exception {

            String url = "/api/questions";

            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("createQuestionRequestDto")).isEqualTo("문제 createQuestionRequestDto를 입력하세요")
            );
        }


        @DisplayName("categoryRequestDto 없음 -> 유효성 검사 실패")
        @Test
        public void 문제_생성하기_유효성_검사_실패_categoryRequestDto_400() throws Exception {

            String url = "/api/questions";

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionDesc("스프링의 특징에 대해 자세하게 학습할 수 있습니다.")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("categoryRequestDto")).isEqualTo("categoryRequestDto를 입력하세요")
            );
        }


        @DisplayName("createChoicesAboutQuestionDto 없음 -> 유효성 검사 실패")
        @Test
        public void 문제_생성하기_유효성_검사_실패_categoryRequesㄴtDto_400() throws Exception {

            String url = "/api/questions";

            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionDesc("스프링의 특징에 대해 자세하게 학습할 수 있습니다.")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("createChoicesAboutQuestionDto")).isEqualTo("createChoicesAboutQuestionDto를 4개를 입력하세요.")
            );
        }


        @Test
        public void 문제_생성하기_유효성_검사_실패_카테고리DTO_유효성실패_400() throws Exception {
            //given
            String url = "/api/questions";
            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionDesc("스프링의 특징에 대해 자세하게 학습할 수 있습니다.")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("categoryRequestDto.category")).isEqualTo("카테고리를 선택을 하세요.")
            );
        }

        @Test
        public void 문제_생성하기_유효성_검사_실패_createQuestionRequestDto_유효성실패_400() throws Exception {
            //given
            String url = "/api/questions";
            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionDesc("스프링의 특징에 대해 자세하게 학습할 수 있습니다.")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("createQuestionRequestDto.questionTitle")).isEqualTo("제목을 입력하세요")
            );
        }

        @Test
        public void 문제_생성하기_유효성_검사_실패_questionDesc_유효성실패_400() throws Exception {
            //given
            String url = "/api/questions";
            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("createQuestionRequestDto.questionDesc")).isEqualTo("문제에 대한 설명을 입력하세요")
            );
        }


        @Test
        public void 문제_생성하기_유효성_검사_실패_questionExplain_유효성실패_400() throws Exception {
            //given
            String url = "/api/questions";
            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionDesc("스프링 특징에 대해서 학습하기")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("createQuestionRequestDto.questionExplain")).isEqualTo("정답에 대해 설명의 필드를 추가하세요")
            );
        }

        @Test
        @DisplayName("문제 선택은 4지선다로 고정")
        public void 문제_생성하기_유효성_검사_실패_createChoicesAboutQuestionDto_4개_유효성실패_400() throws Exception {
            //given
            String url = "/api/questions";
            //given
            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionDesc("스프링의 특징에 대해 자세하게 학습할 수 있습니다.")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();


            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice2, choice3, choice4));


            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();

            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_ParseErrorResponse(url, requestDto);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),

                    () -> assertThat(response.getBody().getCode()).isEqualTo("400"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다."),
                    () -> assertThat(response.getBody().getValidation().get("createChoicesAboutQuestionDto")).isEqualTo("선택지는 정확히 4개여야 합니다.")
            );
        }
    }

    @DisplayName("POST /api/questions/bulk")
    @Nested
    class 문제_대량으로_생성하기 {
        @BeforeEach
        void setUp() {

        }

        @Test
        public void 대량_문제_생성하기_성공_200() throws Exception {
            //given
            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("Spring의 특징이 아닌 것을 선택을 하시오")
                    .questionDesc("스프링의 특징에 대해 자세하게 학습할 수 있습니다.")
                    .questionExplain("스프링은 ioc, di, aop가 있습니다.")
                    .build();

            CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                    .number(4)
                    .content("Reflection")
                    .answer("정답")
                    .build();

            ArrayList<CreateChoicesAboutQuestionDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));

            CreateQuestionAndCategoryRequestDto requestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();

            CreateQuestionAndCategoryRequestDto requestDto2 = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();

            CreateQuestionAndCategoryRequestDto requestDto3 = CreateQuestionAndCategoryRequestDto.builder()
                    .categoryRequestDto(categoryRequestDto)
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .createChoicesAboutQuestionDto(choicesAboutQuestionDtoArrayList)
                    .build();

            List<CreateQuestionAndCategoryRequestDto> list = new ArrayList<>(List.of(requestDto, requestDto2, requestDto3));

            //when
            ApiResponse<String> response = questionMockApiCaller.bulkCreateQuestionChoice(list);

            //Then
            assertThat(response.getStatus()).isEqualTo(201);
        }
    }

    @DisplayName("/api/question/{questionId}")
    @Nested
    class 단일_문제_탐색하기 {
        @BeforeEach
        void setUp() {
            ChoiceQuestionResponseDto choice1 = ChoiceQuestionResponseDto.builder()
                    .number(1)
                    .content("IOC")
                    .build();

            ChoiceQuestionResponseDto choice2 = ChoiceQuestionResponseDto.builder()
                    .number(2)
                    .content("DI")
                    .build();

            ChoiceQuestionResponseDto choice3 = ChoiceQuestionResponseDto.builder()
                    .number(3)
                    .content("AOP")
                    .build();

            ChoiceQuestionResponseDto choice4 = ChoiceQuestionResponseDto.builder()
                    .number(4)
                    .content("Reflection")
                    .build();

            ArrayList<ChoiceQuestionResponseDto> choicesAboutQuestionDtoArrayList = new ArrayList<>(List.of(choice1, choice2, choice3, choice4));


            QuestionResponseDto responseDto = QuestionResponseDto.builder()
                    .title("제목")
                    .categoryTitle("네트워크")
                    .description("예시 설명")
                    .explain("정답 설명")
                    .choices(choicesAboutQuestionDtoArrayList)
                    .build();


            given(questionService.findQuestionWithChoiceAndCategory(eq(1L)))
                    .willReturn(responseDto);

            given(questionService.findQuestionWithChoiceAndCategory(eq(2L)))
                    .willThrow(NotFoundQuestionWithChoicesAndCategoryById.class);
        }

        @Test
        public void 단일_문제_탐색하기_성공_200() throws Exception {
            //given
            Long questionId = 1L;
            //when
            ApiResponse<QuestionResponseDto> response = questionMockApiCaller.findQuestionPathId(questionId);
            //Then
            //assertThat().isEqualTo();
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getTitle()).isEqualTo("제목"),
                    () -> assertThat(response.getBody().getCategoryTitle()).isEqualTo("네트워크"),
                    () -> assertThat(response.getBody().getDescription()).isEqualTo("예시 설명"),
                    () -> assertThat(response.getBody().getExplain()).isEqualTo("정답 설명"),

                    () -> assertThat(response.getBody().getChoices().get(0).getNumber()).isEqualTo(1),
                    () -> assertThat(response.getBody().getChoices().get(0).getContent()).isEqualTo("IOC"),

                    () -> assertThat(response.getBody().getChoices().get(1).getNumber()).isEqualTo(2),
                    () -> assertThat(response.getBody().getChoices().get(1).getContent()).isEqualTo("DI"),

                    () -> assertThat(response.getBody().getChoices().get(2).getNumber()).isEqualTo(3),
                    () -> assertThat(response.getBody().getChoices().get(2).getContent()).isEqualTo("AOP"),

                    () -> assertThat(response.getBody().getChoices().get(3).getNumber()).isEqualTo(4),
                    () -> assertThat(response.getBody().getChoices().get(3).getContent()).isEqualTo("Reflection")
            );

        }

        @Test
        public void 단일_문제_탐색하기_path_없음_400() throws Exception {
            //given
            String url = "/api/questions/2";
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendGetRequest_ExpectErrorResponse(url, null);

            //Then

            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(400),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("3002")
            );
        }

        @Test
        public void 단일_문제_탐색_음수_400() throws Exception {
            //given
            String url = "/api/questions/-1";
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendGetRequest_ExpectErrorResponse(url, null);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);

            assertAll(
                    () -> assertThat(response.getBody().getCode()).isEqualTo("8500"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("pathvariable은 양수로 처리를 해야됩니다.-1")
            );

        }
    }

    @DisplayName("/api/question/{questionId}/answer")
    @Nested
    class 단일_문제에_대한_정답_선택하기 {
        @BeforeEach
        void setUp() {
            ChoiceAnswerRequestDto requestDto = ChoiceAnswerRequestDto.builder()
                    .choiceNumber(1)
                    .time(30L)
                    .build();
            given(memberQuestionService.isCorrectAnswer(anyLong(), eq(1L), eq(requestDto)))
                    .willReturn(QuestionAnswerDto.builder().answer(true).build());
        }

        @Test
        public void 단일_문제에_대한_정답_선택하기_성공_200() throws Exception {
            //given

            Long questionId = 1L;
            ChoiceAnswerRequestDto requestDto = ChoiceAnswerRequestDto.builder()
                    .choiceNumber(1)
                    .time(30L)
                    .build();

            //when
            ApiResponse<QuestionAnswerDto> response = questionMockApiCaller.choiceQuestion(questionId, requestDto);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(201),
                    () -> assertThat(response.getBody().isAnswer()).isEqualTo(true)
            );
        }

        @Test
        public void 단일_문제에_대한_실패_Token_없음_401() throws Exception {
            //given
            String url = "/api/questions/1/answer";
            ChoiceAnswerRequestDto requestDto = ChoiceAnswerRequestDto.builder()
                    .choiceNumber(1)
                    .time(30L)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithNoAuthorization_ParseErrorResponse(url, requestDto);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(401),
                    () -> assertThat(response.getBody().getCode()).isEqualTo("401"),
                    () -> assertThat(response.getBody().getMessage()).isEqualTo("Headers에 토큰 형식의 값 찾을 수 없음")
            );
        }

        @Test
        public void 단일_문제에_대한_실패_RequestBody_유효성_400() throws Exception {
            //given
            String url = "/api/questions/1/answer";
            ChoiceAnswerRequestDto requestDto = ChoiceAnswerRequestDto.builder()
                    .time(30L)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_USER_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
        }

        @Test
        public void 단일_문제에_대한_실패_number_음수_400() throws Exception {
            //given
            String url = "/api/questions/1/answer";
            ChoiceAnswerRequestDto requestDto = ChoiceAnswerRequestDto.builder()
                    .choiceNumber(-2)
                    .time(30L)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_USER_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
        }

        @Test
        public void 단일_문제에_대한_실패_시간_유효성_400() throws Exception {
            //given
            String url = "/api/questions/1/answer";
            ChoiceAnswerRequestDto requestDto = ChoiceAnswerRequestDto.builder()
                    .choiceNumber(1)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_USER_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
        }

        @Test
        public void 단일_문제에_대한_실패_시간_양수_유효성_400() throws Exception {
            //given
            String url = "/api/questions/1/answer";
            ChoiceAnswerRequestDto requestDto = ChoiceAnswerRequestDto.builder()
                    .choiceNumber(1)
                    .time(-30L)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_USER_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("400");
            assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청입니다.");
        }

        @Test
        public void 단일_문제에_대한_실패_Path_음수_유효성_400() throws Exception {
            //given
            String url = "/api/questions/-1/answer";
            ChoiceAnswerRequestDto requestDto = ChoiceAnswerRequestDto.builder()
                    .choiceNumber(1)
                    .time(30L)
                    .build();
            //when
            ApiResponse<ErrorResponse> response = questionMockApiCaller.sendPostRequest_WithAuthorization_USER_ParseErrorResponse(url, requestDto);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("8500");
            assertThat(response.getBody().getMessage()).isEqualTo("pathvariable은 양수로 처리를 해야됩니다.-1");
        }
    }

    @DisplayName("/api/questions")
    @Nested
    class 전체_문제를_페이징 {

        @BeforeEach
        void setUp() {
            QuestionPageWithCategoryAndTitle paging1 = createQuestionPageWithCategoryAndTitle(1L, "자바 문제", "자바", 1);
            QuestionPageWithCategoryAndTitle paging4 = createQuestionPageWithCategoryAndTitle(4L, "데이터베이스 문제", "데이터베이스", 1);
            QuestionPageWithCategoryAndTitle paging6 = createQuestionPageWithCategoryAndTitle(6L, "접근제어자 문제", "자바", 1);

            QuestionPageWithCategoryAndTitle paging2 = createQuestionPageWithCategoryAndTitle(2L, "네트워크 문제", "네트워크", 0);
            QuestionPageWithCategoryAndTitle paging5 = createQuestionPageWithCategoryAndTitle(5L, "TCP 문제", "네트워크", 0);

            QuestionPageWithCategoryAndTitle paging3 = createQuestionPageWithCategoryAndTitle(3L, "운영체제 문제", "운영체제", 2);
            QuestionPageWithCategoryAndTitle paging7 = createQuestionPageWithCategoryAndTitle(7L, "인덱스 문제", "데이터베이스", 2);

            List<QuestionPageWithCategoryAndTitle> list = new ArrayList<>(Arrays.asList(paging1, paging2, paging3, paging4));
            List<QuestionPageWithCategoryAndTitle> list2 = new ArrayList<>(List.of(paging1));

            //STATUS
            List<QuestionPageWithCategoryAndTitle> list4 = new ArrayList<>(Arrays.asList(paging2, paging5)); //0
            List<QuestionPageWithCategoryAndTitle> list3 = new ArrayList<>(Arrays.asList(paging1, paging4, paging6)); // 1
            List<QuestionPageWithCategoryAndTitle> list5 = new ArrayList<>(Arrays.asList(paging3, paging7)); //2

            Page<QuestionPageWithCategoryAndTitle> pagedResponse = new PageImpl<>(list);
            Page<QuestionPageWithCategoryAndTitle> pagedResponse2 = new PageImpl<>(list2);

            //STATUS
            Page<QuestionPageWithCategoryAndTitle> pagedResponse3 = new PageImpl<>(list3);
            Page<QuestionPageWithCategoryAndTitle> pagedResponse4 = new PageImpl<>(list4);
            Page<QuestionPageWithCategoryAndTitle> pagedResponse5 = new PageImpl<>(list5);


            QuestionSearchCondition searchCondition = QuestionSearchCondition.builder().build();
            QuestionSearchCondition searchCondition2 = QuestionSearchCondition.builder().categoryTitle("자바").build();
            QuestionSearchCondition searchCondition3 = QuestionSearchCondition.builder().questionTitle("자바 문제").build();
            QuestionSearchCondition searchCondition4 = QuestionSearchCondition.builder().status(0).build();
            QuestionSearchCondition searchCondition5 = QuestionSearchCondition.builder().status(1).build();
            QuestionSearchCondition searchCondition6 = QuestionSearchCondition.builder().status(2).build();

            given(questionService.questionPageWithCategory(eq(searchCondition), anyInt(), anyInt(), any(LoginUserDto.class))).willReturn(pagedResponse);
            given(questionService.questionPageWithCategory(eq(searchCondition2), anyInt(), anyInt(), any(LoginUserDto.class))).willReturn(pagedResponse2);
            given(questionService.questionPageWithCategory(eq(searchCondition3), anyInt(), anyInt(), any(LoginUserDto.class))).willReturn(pagedResponse2);

            given(questionService.questionPageWithCategory(eq(searchCondition4), anyInt(), anyInt(), any(LoginUserDto.class))).willReturn(pagedResponse4);
            given(questionService.questionPageWithCategory(eq(searchCondition5), anyInt(), anyInt(), any(LoginUserDto.class))).willReturn(pagedResponse3);
            given(questionService.questionPageWithCategory(eq(searchCondition6), anyInt(), anyInt(), any(LoginUserDto.class))).willReturn(pagedResponse5);
        }

        @Test
        public void 전체_문제_페이징_성공_200() throws Exception {
            //given
            String url = "/api/questions";
            QuestionSearchCondition searchCondition = QuestionSearchCondition.builder().build();
            //when
            ApiResponse<Page<QuestionPageWithCategoryAndTitle>> response = questionMockApiCaller.questionPageWithCategory(url, searchCondition);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(2),


                    () -> assertThat(response.getBody().getContent().get(0).getQuestionId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(0).getQuestionTitle()).isEqualTo("네트워크 문제"),
                    () -> assertThat(response.getBody().getContent().get(0).getCategoryTitle()).isEqualTo("네트워크"),
                    () -> assertThat(response.getBody().getContent().get(0).getStatus()).isEqualTo(0),

                    () -> assertThat(response.getBody().getContent().get(1).getQuestionId()).isEqualTo(5L),
                    () -> assertThat(response.getBody().getContent().get(1).getQuestionTitle()).isEqualTo("TCP 문제"),
                    () -> assertThat(response.getBody().getContent().get(1).getCategoryTitle()).isEqualTo("네트워크"),
                    () -> assertThat(response.getBody().getContent().get(1).getStatus()).isEqualTo(0)
            );
        }

        @Test
        public void 전체_문제_페이징_조건_카테고리_자바만_성공_200() throws Exception {
            //given
            String url = "/api/questions?categoryTitle=자바";
            QuestionSearchCondition searchCondition2 = QuestionSearchCondition.builder().categoryTitle("자바").build();
            //when
            ApiResponse<Page<QuestionPageWithCategoryAndTitle>> response = questionMockApiCaller.questionPageWithCategory(url, searchCondition2);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(1),


                    () -> assertThat(response.getBody().getContent().get(0).getQuestionId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getContent().get(0).getQuestionTitle()).isEqualTo("자바 문제"),
                    () -> assertThat(response.getBody().getContent().get(0).getCategoryTitle()).isEqualTo("자바"),
                    () -> assertThat(response.getBody().getContent().get(0).getStatus()).isEqualTo(1)
            );
        }

        @Test
        public void 전체_문제_페이징_조건_문제제목_성공_200() throws Exception {
            //given
            String url = "/api/questions?questionTitle=자바 문제";
            QuestionSearchCondition searchCondition2 = QuestionSearchCondition.builder().questionTitle("자바 문제").build();
            //when
            ApiResponse<Page<QuestionPageWithCategoryAndTitle>> response = questionMockApiCaller.questionPageWithCategory(url, searchCondition2);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(1),


                    () -> assertThat(response.getBody().getContent().get(0).getQuestionId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getContent().get(0).getQuestionTitle()).isEqualTo("자바 문제"),
                    () -> assertThat(response.getBody().getContent().get(0).getCategoryTitle()).isEqualTo("자바"),
                    () -> assertThat(response.getBody().getContent().get(0).getStatus()).isEqualTo(1)
            );
        }

        @Test
        public void 전체_문제_페이징_조건_해결한_문제만_보기_성공_200() throws Exception {
            //given
            String url = "/api/questions?status=1";
            QuestionSearchCondition searchCondition2 = QuestionSearchCondition.builder().status(1).build();
            //when
            ApiResponse<Page<QuestionPageWithCategoryAndTitle>> response = questionMockApiCaller.questionPageWithCategory(url, searchCondition2);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(3),


                    () -> assertThat(response.getBody().getContent().get(0).getQuestionId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getContent().get(0).getQuestionTitle()).isEqualTo("자바 문제"),
                    () -> assertThat(response.getBody().getContent().get(0).getCategoryTitle()).isEqualTo("자바"),
                    () -> assertThat(response.getBody().getContent().get(0).getStatus()).isEqualTo(1),

                    () -> assertThat(response.getBody().getContent().get(1).getQuestionId()).isEqualTo(4L),
                    () -> assertThat(response.getBody().getContent().get(1).getQuestionTitle()).isEqualTo("데이터베이스 문제"),
                    () -> assertThat(response.getBody().getContent().get(1).getCategoryTitle()).isEqualTo("데이터베이스"),
                    () -> assertThat(response.getBody().getContent().get(1).getStatus()).isEqualTo(1),

                    () -> assertThat(response.getBody().getContent().get(2).getQuestionId()).isEqualTo(6L),
                    () -> assertThat(response.getBody().getContent().get(2).getQuestionTitle()).isEqualTo("접근제어자 문제"),
                    () -> assertThat(response.getBody().getContent().get(2).getCategoryTitle()).isEqualTo("자바"),
                    () -> assertThat(response.getBody().getContent().get(2).getStatus()).isEqualTo(1)
            );
        }


        @Test
        public void 전체_문제_페이징_조건_아직_해결못한_문제_보기_성공_200() throws Exception {
            //given
            String url = "/api/questions?status=0";
            QuestionSearchCondition searchCondition2 = QuestionSearchCondition.builder().status(0).build();
            //when
            ApiResponse<Page<QuestionPageWithCategoryAndTitle>> response = questionMockApiCaller.questionPageWithCategory(url, searchCondition2);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),


                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(2),


                    () -> assertThat(response.getBody().getContent().get(0).getQuestionId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(0).getQuestionTitle()).isEqualTo("네트워크 문제"),
                    () -> assertThat(response.getBody().getContent().get(0).getCategoryTitle()).isEqualTo("네트워크"),
                    () -> assertThat(response.getBody().getContent().get(0).getStatus()).isEqualTo(0),

                    () -> assertThat(response.getBody().getContent().get(1).getQuestionId()).isEqualTo(5L),
                    () -> assertThat(response.getBody().getContent().get(1).getQuestionTitle()).isEqualTo("TCP 문제"),
                    () -> assertThat(response.getBody().getContent().get(1).getCategoryTitle()).isEqualTo("네트워크"),
                    () -> assertThat(response.getBody().getContent().get(1).getStatus()).isEqualTo(0)
            );
        }

        @Test
        public void 전체_문제_페이징_조건_실패한_문제_보기_성공_200() throws Exception {
            //given
            String url = "/api/questions?status=2";
            QuestionSearchCondition searchCondition2 = QuestionSearchCondition.builder().status(2).build();
            //when
            ApiResponse<Page<QuestionPageWithCategoryAndTitle>> response = questionMockApiCaller.questionPageWithCategory(url, searchCondition2);

            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(2),


                    () -> assertThat(response.getBody().getContent().get(0).getQuestionId()).isEqualTo(3L),
                    () -> assertThat(response.getBody().getContent().get(0).getQuestionTitle()).isEqualTo("운영체제 문제"),
                    () -> assertThat(response.getBody().getContent().get(0).getCategoryTitle()).isEqualTo("운영체제"),
                    () -> assertThat(response.getBody().getContent().get(0).getStatus()).isEqualTo(2),

                    () -> assertThat(response.getBody().getContent().get(1).getQuestionId()).isEqualTo(7L),
                    () -> assertThat(response.getBody().getContent().get(1).getQuestionTitle()).isEqualTo("인덱스 문제"),
                    () -> assertThat(response.getBody().getContent().get(1).getCategoryTitle()).isEqualTo("데이터베이스"),
                    () -> assertThat(response.getBody().getContent().get(1).getStatus()).isEqualTo(2)
            );
        }
    }

    @DisplayName("/api/questions/my-questions 단일 회원의 문제 찾기")
    @Nested
    class 단일_회원의_문제_찾기 {

        @BeforeEach
        void setUp() {
            QuestionPageWithCategoryAndTitle paging1 = createQuestionPageWithCategoryAndTitle(1L, "자바 문제", "자바", 0);
            QuestionPageWithCategoryAndTitle paging2 = createQuestionPageWithCategoryAndTitle(2L, "데이터베이스 문제", "데이터베이스", 1);
            QuestionPageWithCategoryAndTitle paging3 = createQuestionPageWithCategoryAndTitle(3L, "접근제어자 문제", "자바", 2);

            List<QuestionPageWithCategoryAndTitle> list = new ArrayList<>(Arrays.asList(paging1, paging2, paging3));

            Page<QuestionPageWithCategoryAndTitle> pagedResponse = new PageImpl<>(list);

            QuestionSearchCondition searchCondition = QuestionSearchCondition.builder()
                    .memberId(1L)
                    .build();

            given(questionService.questionPageWithCategory(eq(searchCondition), anyInt(), anyInt(), any(LoginUserDto.class))).willReturn(pagedResponse);
        }

        @Test
        public void 회원의_문제_찾기_성공_200() throws Exception {
            //given
            String url = "/api/questions/my-questions";

            QuestionSearchCondition searchCondition = QuestionSearchCondition.builder()
                    .memberId(1L)
                    .build();
            //when
            ApiResponse<Page<QuestionPageWithCategoryAndTitle>> response = questionMockApiCaller.findMyQuestion(url, searchCondition);
            //Then
            assertAll(
                    () -> assertThat(response.getStatus()).isEqualTo(200),

                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getNumber()).isEqualTo(0),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(3),


                    () -> assertThat(response.getBody().getContent().get(0).getQuestionId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getContent().get(0).getQuestionTitle()).isEqualTo("자바 문제"),
                    () -> assertThat(response.getBody().getContent().get(0).getCategoryTitle()).isEqualTo("자바"),
                    () -> assertThat(response.getBody().getContent().get(0).getStatus()).isEqualTo(0),

                    () -> assertThat(response.getBody().getContent().get(1).getQuestionId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(1).getQuestionTitle()).isEqualTo("데이터베이스 문제"),
                    () -> assertThat(response.getBody().getContent().get(1).getCategoryTitle()).isEqualTo("데이터베이스"),
                    () -> assertThat(response.getBody().getContent().get(1).getStatus()).isEqualTo(1),

                    () -> assertThat(response.getBody().getContent().get(2).getQuestionId()).isEqualTo(3L),
                    () -> assertThat(response.getBody().getContent().get(2).getQuestionTitle()).isEqualTo("접근제어자 문제"),
                    () -> assertThat(response.getBody().getContent().get(2).getCategoryTitle()).isEqualTo("자바"),
                    () -> assertThat(response.getBody().getContent().get(2).getStatus()).isEqualTo(2)
            );

        }
    }

}