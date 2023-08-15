package com.cstudy.moduleapi.domain.question.controller;

import com.cstudy.moduleapi.application.question.MemberQuestionService;
import com.cstudy.moduleapi.application.question.QuestionService;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.moduleapi.dto.choice.CreateChoicesAboutQuestionDto;
import com.cstudy.moduleapi.dto.question.*;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.dto.ChoiceQuestionResponseDto;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import com.cstudy.modulecommon.dto.QuestionSearchCondition;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @MockBean
    private MemberQuestionService memberQuestionService;

    @MockBean
    private QuestionService questionService;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtTokenizer.createAccessToken(1L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.ADMIN.getRoleName()));
    }

    @Test
    @DisplayName("문제 생성하기")
    void createQuestionWithValid() throws Exception {
        //given
        /**
         *     private CreateQuestionRequestDto createQuestionRequestDto;
         *     private CategoryRequestDto categoryRequestDto;
         *     private List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto;
         */
        CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                .questionTitle("문제 제목")
                .questionExplain("문제 설명")
                .questionDesc("This is a sample question")
                .build();

        CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                .category("네트워크")
                .build();

        CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                .number(1)
                .content("선택1")
                .answer("정답")
                .build();
        CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                .number(2)
                .content("선택2")
                .build();
        CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                .number(3)
                .content("선택3")
                .build();
        CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                .number(4)
                .content("선택4")
                .build();

        List<CreateChoicesAboutQuestionDto> list = new ArrayList<>();

        list.add(choice1);
        list.add(choice2);
        list.add(choice3);
        list.add(choice4);

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
                .categoryRequestDto(categoryRequestDto)
                .createQuestionRequestDto(createQuestionRequestDto)
                .createChoicesAboutQuestionDto(list)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/question")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(createQuestionAndCategoryRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("문제 생성하기 - 토큰 없음")
    void createQuestionWithInValidToken() throws Exception {
        //given
        /**
         *     private CreateQuestionRequestDto createQuestionRequestDto;
         *     private CategoryRequestDto categoryRequestDto;
         *     private List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto;
         */
        CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                .questionTitle("문제 제목")
                .questionExplain("문제 설명")
                .questionDesc("This is a sample question")
                .build();

        CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                .category("네트워크")
                .build();

        CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                .number(1)
                .content("선택1")
                .answer("정답")
                .build();
        CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                .number(2)
                .content("선택2")
                .build();
        CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                .number(3)
                .content("선택3")
                .build();
        CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                .number(4)
                .content("선택4")
                .build();

        List<CreateChoicesAboutQuestionDto> list = new ArrayList<>();

        list.add(choice1);
        list.add(choice2);
        list.add(choice3);
        list.add(choice4);

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
                .categoryRequestDto(categoryRequestDto)
                .createQuestionRequestDto(createQuestionRequestDto)
                .createChoicesAboutQuestionDto(list)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/question")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(createQuestionAndCategoryRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("대량 문제 생성하기")
    void createBulkQuestionWithCategoryValid() throws Exception {
        CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                .questionTitle("문제 제목")
                .questionExplain("문제 설명")
                .questionDesc("This is a sample question")
                .build();

        CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                .category("네트워크")
                .build();

        CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                .number(1)
                .content("선택1")
                .answer("정답")
                .build();
        CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                .number(2)
                .content("선택2")
                .build();
        CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                .number(3)
                .content("선택3")
                .build();
        CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                .number(4)
                .content("선택4")
                .build();

        List<CreateChoicesAboutQuestionDto> list = new ArrayList<>();

        list.add(choice1);
        list.add(choice2);
        list.add(choice3);
        list.add(choice4);

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto1 = CreateQuestionAndCategoryRequestDto.builder()
                .categoryRequestDto(categoryRequestDto)
                .createQuestionRequestDto(createQuestionRequestDto)
                .createChoicesAboutQuestionDto(list)
                .build();

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto2 = CreateQuestionAndCategoryRequestDto.builder()
                .categoryRequestDto(categoryRequestDto)
                .createQuestionRequestDto(createQuestionRequestDto)
                .createChoicesAboutQuestionDto(list)
                .build();

        ArrayList<CreateQuestionAndCategoryRequestDto> bulkList = new ArrayList<>();
        bulkList.add(createQuestionAndCategoryRequestDto1);
        bulkList.add(createQuestionAndCategoryRequestDto2);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/questions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(bulkList))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("대량 문제 생성하기 - empty token")
    void createBulkQuestionWithCategoryInValid() throws Exception {
        CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                .questionTitle("문제 제목")
                .questionExplain("문제 설명")
                .questionDesc("This is a sample question")
                .build();

        CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                .category("네트워크")
                .build();

        CreateChoicesAboutQuestionDto choice1 = CreateChoicesAboutQuestionDto.builder()
                .number(1)
                .content("선택1")
                .answer("정답")
                .build();
        CreateChoicesAboutQuestionDto choice2 = CreateChoicesAboutQuestionDto.builder()
                .number(2)
                .content("선택2")
                .build();
        CreateChoicesAboutQuestionDto choice3 = CreateChoicesAboutQuestionDto.builder()
                .number(3)
                .content("선택3")
                .build();
        CreateChoicesAboutQuestionDto choice4 = CreateChoicesAboutQuestionDto.builder()
                .number(4)
                .content("선택4")
                .build();

        List<CreateChoicesAboutQuestionDto> list = new ArrayList<>();

        list.add(choice1);
        list.add(choice2);
        list.add(choice3);
        list.add(choice4);

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto1 = CreateQuestionAndCategoryRequestDto.builder()
                .categoryRequestDto(categoryRequestDto)
                .createQuestionRequestDto(createQuestionRequestDto)
                .createChoicesAboutQuestionDto(list)
                .build();

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto2 = CreateQuestionAndCategoryRequestDto.builder()
                .categoryRequestDto(categoryRequestDto)
                .createQuestionRequestDto(createQuestionRequestDto)
                .createChoicesAboutQuestionDto(list)
                .build();

        ArrayList<CreateQuestionAndCategoryRequestDto> bulkList = new ArrayList<>();
        bulkList.add(createQuestionAndCategoryRequestDto1);
        bulkList.add(createQuestionAndCategoryRequestDto2);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/questions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(bulkList))
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("단일 문제 찾기")
    void findFirstQuestionAndParameterValid() throws Exception {
        //given
        Long questionId = 1L;

        QuestionResponseDto questionResponseDto = QuestionResponseDto.builder()
                .title("제목")
                .categoryTitle("네트워크")
                .description("정답")
                .explain("설명")
                .choices(List.of(ChoiceQuestionResponseDto.builder()
                        .number(1)
                        .content("1")
                        .build(), ChoiceQuestionResponseDto.builder()
                        .number(2)
                        .content("2")
                        .build(), ChoiceQuestionResponseDto.builder()
                        .number(3)
                        .content("3")
                        .build(), ChoiceQuestionResponseDto.builder()
                        .number(4)
                        .content("4")
                        .build()))
                .build();

        given(questionService.findQuestionWithChoiceAndCategory(eq(questionId))).willReturn(questionResponseDto);
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/question/{questionId}", questionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.categoryTitle").value("네트워크"))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.description").value("정답"))
                .andExpect(jsonPath("$.explain").value("설명"))
                .andDo(print());
        //then
        verify(questionService).findQuestionWithChoiceAndCategory(any(Long.class));
    }

    @Test
    @DisplayName("단일 문제 찾기 - empty token")
    void findFirstQuestionAndParameterInValidCauseEmptyToken() throws Exception {
        //given
        Long questionId = 1L;

        QuestionResponseDto questionResponseDto = QuestionResponseDto.builder()
                .title("제목")
                .categoryTitle("네트워크")
                .description("정답")
                .explain("설명")
                .choices(List.of(ChoiceQuestionResponseDto.builder()
                        .number(1)
                        .content("1")
                        .build(), ChoiceQuestionResponseDto.builder()
                        .number(2)
                        .content("2")
                        .build(), ChoiceQuestionResponseDto.builder()
                        .number(3)
                        .content("3")
                        .build(), ChoiceQuestionResponseDto.builder()
                        .number(4)
                        .content("4")
                        .build()))
                .build();

        given(questionService.findQuestionWithChoiceAndCategory(eq(questionId))).willReturn(questionResponseDto);
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/question/{questionId}", questionId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("단일 문제 정답 선택하기")
    void choiceQuestionValidWithPathVariable() throws Exception {
        //given
        Long questionId = 1L;

        ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
                .choiceNumber(1)
                .time(100L)
                .build();

        QuestionAnswerDto questionAnswerDto = QuestionAnswerDto.builder()
                .answer(true)
                .build();

        given(memberQuestionService.isCorrectAnswer(1L, questionId, choiceAnswerRequestDto)).willReturn(questionAnswerDto);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/question/{questionId}", questionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(choiceAnswerRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
        //then
        verify(memberQuestionService).isCorrectAnswer(any(Long.class), any(Long.class), any(ChoiceAnswerRequestDto.class));
    }

//    @Test
//    @DisplayName("단일 문제 정답 선택하기 - TOKEN 없음")
//    void choiceQuestionValidWithPathVariableCauseEmptyToken() throws Exception {
//        //given
//        Long questionId = 1L;
//
//        ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
//                .choiceNumber(1)
//                .time(100L)
//                .build();
//
//        QuestionAnswerDto questionAnswerDto = QuestionAnswerDto.builder()
//                .answer(true)
//                .build();
//
//        given(memberQuestionService.isCorrectAnswer(1L, questionId, choiceAnswerRequestDto)).willReturn(questionAnswerDto);
//
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/question/{questionId}", questionId)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsBytes(choiceAnswerRequestDto))
//                )
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
//                .andDo(print());
//        //then
//    }

    @Test
    @DisplayName("전체 문제 페이징")
    void findQuestionPageWithCategoryAndTitleConditionalSearchValid() throws Exception {
        //given
        QuestionSearchCondition questionSearchCondition = QuestionSearchCondition.builder()
                .build();

        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(1L)
                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
                .build();


        List<QuestionPageWithCategoryAndTitle> mockQuestionList = new ArrayList<>();
        mockQuestionList.add(new QuestionPageWithCategoryAndTitle(1L, "질문1", "네트워크", 3));
        mockQuestionList.add(new QuestionPageWithCategoryAndTitle(1L, "질문2", "네트워크", 4));

        Pageable pageable = PageRequest.of(0, 10); // Page 0, with 10 elements per page

        Page<QuestionPageWithCategoryAndTitle> mockPageResult = new PageImpl<>(mockQuestionList, pageable, mockQuestionList.size());


        given(questionService.questionPageWithCategory(questionSearchCondition, 0, 10, loginUserDto))
                .willReturn(mockPageResult);


        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/questions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(questionSearchCondition))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("내가 푼 문제 조회")
    void findMyQuestionWithValid() throws Exception {
        //given
        QuestionSearchCondition questionSearchCondition = QuestionSearchCondition.builder().build();

        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(1L)
                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
                .build();


        List<QuestionPageWithCategoryAndTitle> mockQuestionList = new ArrayList<>();
        mockQuestionList.add(new QuestionPageWithCategoryAndTitle(1L, "질문1", "네트워크", 3));
        mockQuestionList.add(new QuestionPageWithCategoryAndTitle(1L, "질문2", "네트워크", 4));

        Pageable pageable = PageRequest.of(0, 10);

        Page<QuestionPageWithCategoryAndTitle> mockPageResult = new PageImpl<>(mockQuestionList, pageable, mockQuestionList.size());

        given(questionService.questionPageWithCategory(questionSearchCondition, 0, 10, loginUserDto)).willReturn(mockPageResult);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/questions/myquestion")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
        //then
        //verify()
    }

//    @Test
//    @DisplayName("게시글 삭제")
//    void deleteRequestId() throws Exception {
//        //given
//        Long id = 1L;
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.delete("/api/request/{id}", id)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + token)
//                                .content("")
//                )
//                .andExpect(MockMvcResultMatchers.status().isNoContent())
//                .andDo(print());
//        //then
//        //verify()
//    }
}