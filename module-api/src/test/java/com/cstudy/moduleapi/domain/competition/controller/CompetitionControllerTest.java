//package com.cstudy.moduleapi.domain.competition.controller;
//
//
//import com.cstudy.moduleapi.config.ControllerTestBase;
//import com.cstudy.moduleapi.dto.competition.*;
//import com.cstudy.moduleapi.dto.workbook.QuestionIdRequestDto;
//import com.cstudy.moduleapi.enums.MemberTestEnum;
//import com.cstudy.modulecommon.domain.role.RoleEnum;
//import com.cstudy.modulecommon.dto.ChoiceQuestionResponseDto;
//import com.cstudy.modulecommon.error.competition.NotFoundCompetitionId;
//import com.cstudy.modulecommon.util.LoginUserDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//class CompetitionControllerTest extends ControllerTestBase {
//
//    private static String VALID_TOKEN;
//    private static String INVALID_TOKEN;
//
//
//    @BeforeEach
//    void setUp() {
//        VALID_TOKEN = jwtTokenizer.createAccessToken(1L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.ADMIN.getRoleName()));
//        INVALID_TOKEN = jwtTokenizer.createAccessToken(2L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.CUSTOM.getRoleName()));
//    }
//
//    @Test
//    @DisplayName("대회 문제 조회하기")
//    void getCompetitionQuestionWithValid() throws Exception {
//        //given
//        Long competitionId = 1L;
//        //  VALID_TOKEN = jwtTokenizer.createAccessToken(1L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.ADMIN.getRoleName()));
//        LoginUserDto loginUserDto = LoginUserDto.builder()
//                .memberId(1L)
//                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
//                .build();
//        List<com.cstudy.modulecommon.dto.CompetitionQuestionDto> result = new ArrayList<>();
//
//
//        com.cstudy.modulecommon.dto.CompetitionQuestionDto build =
//                com.cstudy.modulecommon.dto.CompetitionQuestionDto.builder()
//                        .questionId(1L)
//                        .description("설명")
//                        .choices(List.of(ChoiceQuestionResponseDto.builder()
//                                .number(1)
//                                .content("정답")
//                                .build(), ChoiceQuestionResponseDto.builder()
//                                .number(2)
//                                .content("오답")
//                                .build(), ChoiceQuestionResponseDto.builder()
//                                .number(3)
//                                .content("오답")
//                                .build(), ChoiceQuestionResponseDto.builder()
//                                .number(4)
//                                .content("오답")
//                                .build()))
//                        .build();
//
//        result.add(build);
//
//        result.add(com.cstudy.modulecommon.dto.CompetitionQuestionDto.builder()
//                .questionId(2L)
//                .description("설명2")
//                .choices(List.of(ChoiceQuestionResponseDto.builder()
//                        .number(1)
//                        .content("정답")
//                        .build(), ChoiceQuestionResponseDto.builder()
//                        .number(2)
//                        .content("오답")
//                        .build(), ChoiceQuestionResponseDto.builder()
//                        .number(3)
//                        .content("오답")
//                        .build(), ChoiceQuestionResponseDto.builder()
//                        .number(4)
//                        .content("오답")
//                        .build()))
//                .build());
//        result.add(com.cstudy.modulecommon.dto.CompetitionQuestionDto.builder()
//                .questionId(3L)
//                .description("설명2")
//                .choices(List.of(ChoiceQuestionResponseDto.builder()
//                        .number(1)
//                        .content("정답")
//                        .build(), ChoiceQuestionResponseDto.builder()
//                        .number(2)
//                        .content("오답")
//                        .build(), ChoiceQuestionResponseDto.builder()
//                        .number(3)
//                        .content("오답")
//                        .build(), ChoiceQuestionResponseDto.builder()
//                        .number(4)
//                        .content("오답")
//                        .build()))
//                .build());
//
//        given(competitionService.getCompetitionQuestion(anyLong(), any(LoginUserDto.class))).willReturn(result);
//
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/api/competition/question/{competitionId}", competitionId)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content("")
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.length()").value(3))
//                .andExpect(jsonPath("$[0].questionId").value(1))
//                .andExpect(jsonPath("$[0].description").value("설명"))
//                .andExpect(jsonPath("$[0].choices").isArray())
//                .andExpect(jsonPath("$[0].choices.length()").value(4))
//                .andExpect(jsonPath("$[0].choices[0].number").value(1))
//                .andExpect(jsonPath("$[0].choices[0].content").value("정답"))
//                .andExpect(jsonPath("$[0].choices[1].number").value(2))
//                .andExpect(jsonPath("$[0].choices[1].content").value("오답"))
//                .andExpect(jsonPath("$[0].choices[2].number").value(3))
//                .andExpect(jsonPath("$[0].choices[2].content").value("오답"))
//                .andExpect(jsonPath("$[0].choices[3].number").value(4))
//                .andExpect(jsonPath("$[0].choices[3].content").value("오답"))
//                .andExpect(jsonPath("$[1].questionId").value(2))
//                .andExpect(jsonPath("$[1].description").value("설명2"))
//                .andExpect(jsonPath("$[1].choices").isArray())
//                .andExpect(jsonPath("$[1].choices.length()").value(4))
//                .andExpect(jsonPath("$[1].choices[0].number").value(1))
//                .andExpect(jsonPath("$[1].choices[0].content").value("정답"))
//                .andExpect(jsonPath("$[1].choices[1].number").value(2))
//                .andExpect(jsonPath("$[1].choices[1].content").value("오답"))
//                .andExpect(jsonPath("$[1].choices[2].number").value(3))
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        verify(competitionService).getCompetitionQuestion(anyLong(), any(LoginUserDto.class));
//    }
//
//    @Test
//    @DisplayName("대회 문제 조회하기 Invalid")
//    void getCompetitionQuestion() throws Exception {
//        //given
//        Long competitionId = 1L;
//        LoginUserDto loginUserDto = LoginUserDto.builder()
//                .memberId(1L)
//                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
//                .build();
//
//        given(competitionService.getCompetitionQuestion(eq(1L), any(LoginUserDto.class))).willThrow(NotFoundCompetitionId.class);
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/api/competition/question/{competitionId}", competitionId)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content("")
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("5001"))
//                .andDo(MockMvcResultHandlers.print());
//        //then
//    }
//
//    @Test
//    @DisplayName("참여 가능 대회 리스트")
//    void getAvailableCompetitionWithValid() throws Exception {
//        //given
//        List<CompetitionListResponseDto> list = new ArrayList<>();
//        CompetitionListResponseDto competitionListResponseDto = CompetitionListResponseDto.builder()
//                .id(1L)
//                .title("제목")
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .participants(100)
//                .build();
//        list.add(competitionListResponseDto);
//        Pageable pageable = PageRequest.of(0, 10);
//        LocalDateTime now = LocalDateTime.now();
//        Page<CompetitionListResponseDto> mockPageResult = new PageImpl<>(list, pageable, list.size());
//        given(competitionService.getCompetitionList(false, pageable, now)).willReturn(mockPageResult);
//
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/api/competition/list")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content("")
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        verify(competitionService).getCompetitionList(anyBoolean(), any(Pageable.class), any(LocalDateTime.class));
//    }
//
//    @Test
//    @DisplayName("종료된 대회 리스트")
//    void getFinishCompetitionWithValid() throws Exception {
//        //given
//        List<CompetitionListResponseDto> list = new ArrayList<>();
//        CompetitionListResponseDto competitionListResponseDto = CompetitionListResponseDto.builder()
//                .id(1L)
//                .title("제목")
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .participants(100)
//                .build();
//        list.add(competitionListResponseDto);
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<CompetitionListResponseDto> mockPageResult = new PageImpl<>(list, pageable, list.size());
//        LocalDateTime now = LocalDateTime.now();
//        given(competitionService.getCompetitionList(false, pageable, now)).willReturn(mockPageResult);
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/api/competition/list/finish")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content("")
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        verify(competitionService).getCompetitionList(anyBoolean(), any(Pageable.class), any(LocalDateTime.class));
//    }
//
//
//    @Test
//    @DisplayName("대회 문제 추가")
//    void addQuestionWithValid() throws Exception {
//        //given
//
//        CompetitionQuestionRequestDto competitionQuestionRequestDto = CompetitionQuestionRequestDto.builder()
//                .competitionId(1L)
//                .questionIds(List.of(QuestionIdRequestDto.builder()
//                        .id(1L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(2L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(3L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(4L)
//                        .build()))
//                .build();
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/competition/question/add")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content(objectMapper.writeValueAsBytes(competitionQuestionRequestDto))
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        //verify()
//    }
//
//    @Test
//    @DisplayName("대회 문제 추가 실패 Valid")
//    void addQuestionWithInvalidCauseValid() throws Exception {
//        //given
//        CompetitionQuestionRequestDto competitionQuestionRequestDto = CompetitionQuestionRequestDto.builder()
//                .questionIds(List.of(QuestionIdRequestDto.builder()
//                        .id(1L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(2L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(3L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(4L)
//                        .build()))
//                .build();
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/competition/question/add")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content(objectMapper.writeValueAsBytes(competitionQuestionRequestDto))
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.competitionId").value("문제접 번호를 입력하세요."))
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        //verify()
//    }
//
//    @Test
//    @DisplayName("대회 문제 추가 실패 Invalid")
//    void addQuestionWithInvalidCauseValidQuestionId() throws Exception {
//        //given
//        CompetitionQuestionRequestDto competitionQuestionRequestDto = CompetitionQuestionRequestDto.builder()
//                .competitionId(1L)
//                .build();
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/competition/question/add")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content(objectMapper.writeValueAsBytes(competitionQuestionRequestDto))
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.questionIds").value("문제 아이디 리스트를 입력하세요."))
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        //verify()
//    }
//
//    @Test
//    @DisplayName("대회 문제 삭제")
//    void deleteQuestionWithValid() throws Exception {
//        //given
//        CompetitionQuestionRequestDto competitionQuestionRequestDto = CompetitionQuestionRequestDto.builder()
//                .competitionId(1L)
//                .questionIds(List.of(QuestionIdRequestDto.builder()
//                        .id(1L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(2L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(3L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(4L)
//                        .build()))
//                .build();
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/competition/question/delete")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content(objectMapper.writeValueAsBytes(competitionQuestionRequestDto))
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        //verify()
//    }
//
//    @Test
//    @DisplayName("대회 문제 삭제 Dto Invalid")
//    void deleteCompetitionWithInvalidDto() throws Exception {
//        //given
//        CompetitionQuestionRequestDto competitionQuestionRequestDto = CompetitionQuestionRequestDto.builder()
//                .questionIds(List.of(QuestionIdRequestDto.builder()
//                        .id(1L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(2L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(3L)
//                        .build(), QuestionIdRequestDto.builder()
//                        .id(4L)
//                        .build()))
//                .build();
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/competition/question/delete")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content(objectMapper.writeValueAsBytes(competitionQuestionRequestDto))
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated())
////                .andExpect(MockMvcResultMatchers.status().isBadRequest())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.competitionId").value("문제접 번호를 입력하세요."))
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        //verify()
//    }
//
//    @Test
//    @DisplayName("대회 내 랭킹")
//    void getMyRanking() throws Exception {
//        //given
//        Long id = 1L;
//        MyCompetitionRankingDto myCompetitionRankingDto = MyCompetitionRankingDto.builder()
//                .ranking(1)
//                .build();
//
//        LoginUserDto loginUserDto = LoginUserDto.builder()
//                .memberId(1L)
//                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
//                .build();
//
//        given(memberCompetitionService.myRanking(loginUserDto.getMemberId(), id)).willReturn(myCompetitionRankingDto);
//
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/api/competition/myranking/{id}", id)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content("")
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        verify(memberCompetitionService).myRanking(anyLong(), anyLong());
//    }
//
//    @Test
//    @DisplayName("대회 생성하기")
//    void createCompetitionWithValidParameter() throws Exception {
//        //given
//        LocalDateTime now = LocalDateTime.now();
//
//        CreateCompetitionRequestDto createCompetitionRequestDto = CreateCompetitionRequestDto.builder()
//                .competitionStart(now)
//                .competitionEnd(now.plusHours(1))
//                .competitionTitle("CS 경기")
//                .participants(100)
//                .build();
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/competition")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content(objectMapper.writeValueAsBytes(createCompetitionRequestDto))
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        //verify()
//    }
//
//    @Test
//    @DisplayName("대회 생성하기 Token 오류")
//    void createCompetitionWithInvalidToken() throws Exception {
//        //given
//        LocalDateTime now = LocalDateTime.now();
//
//        CreateCompetitionRequestDto createCompetitionRequestDto = CreateCompetitionRequestDto.builder()
//                .competitionStart(now)
//                .competitionEnd(now.plusHours(1))
//                .competitionTitle("CS 경기")
//                .participants(100)
//                .build();
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/competition")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + INVALID_TOKEN)
//                                .content(objectMapper.writeValueAsBytes(createCompetitionRequestDto))
//                )
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        //verify()
//    }
//
//    @Test
//    @DisplayName("특정 대회 참가하기")
//    void joinCompetitionByIdWithValid() throws Exception {
//        //given
//        Long competitionId = 1L;
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/competition/join/{competitionId}", competitionId)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content("")
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        //verify()
//    }
//
//    @Test
//    @DisplayName("대회 정보 조회")
//    void getCompetitionWithValidCompetitionId() throws Exception {
//        //given
//        Long competitionId = 1L;
//
//        given(competitionService.getCompetition(anyLong())).willReturn(CompetitionResponseDto.builder()
//                .title("cs 대회")
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .MaxParticipants(100)
//                .participants(5)
//                .build());
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/api/competition/{competitionId}", competitionId)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content("")
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//        verify(competitionService).getCompetition(anyLong());
//    }
//
//    @Test
//    @DisplayName("특정 대회 정보 조회 실패 NotFoundCompetitionId")
//    void getCompetitionWithInvalid() throws Exception {
//        //given
//        Long competitionId = 1L;
//
//        given(competitionService.getCompetition(anyLong())).willThrow(NotFoundCompetitionId.class);
//        // when
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/api/competition/{competitionId}", competitionId)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .header("Authorization", "Bearer " + VALID_TOKEN)
//                                .content("")
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andDo(MockMvcResultHandlers.print());
//        //then
//    }
//}