//package com.cstudy.moduleapi.application.competition.application.impl;
//
//import com.cstudy.moduleapi.config.ServiceTestBase;
//import com.cstudy.moduleapi.dto.competition.*;
//import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
//import com.cstudy.moduleapi.dto.workbook.QuestionIdRequestDto;
//import com.cstudy.moduleapi.dto.workbook.WorkbookQuestionRequestDto;
//import com.cstudy.modulecommon.domain.choice.Choice;
//import com.cstudy.modulecommon.domain.member.Member;
//import com.cstudy.modulecommon.domain.question.Question;
//import com.cstudy.modulecommon.domain.workbook.Workbook;
//import com.cstudy.modulecommon.error.competition.DuplicateMemberWithCompetition;
//import com.cstudy.modulecommon.error.competition.NotFoundMemberCompetition;
//import com.cstudy.modulecommon.error.competition.ParticipantsWereInvitedParticipateException;
//import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
//import com.cstudy.modulecommon.util.LoginUserDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Transactional
//class CompetitionServiceImplTest extends ServiceTestBase {
//
//
//    private List<Long> memberIds = new ArrayList<>();
//    private Long competitionId1;
//    private Long competitionId2;
//
//    @BeforeEach
//    public void setUp() {
//
//        for (int i = 0; i < 10; i++) {
//            MemberSignupRequest memberSignupRequest1 = MemberSignupRequest.builder()
//                    .email("test" + i + "@test.com")
//                    .name("테스트 유저" + i)
//                    .password("test1234!").build();
//            memberService.signUp(memberSignupRequest1);
//            Member member = memberRepository.findByEmail("test" + i + "@test.com")
//                    .orElseThrow(() -> new NotFoundMemberEmail("test@test.com"));
//            memberIds.add(member.getId());
//
//        }
//        CreateCompetitionRequestDto competitionDto = CreateCompetitionRequestDto.builder()
//                .competitionTitle("대회 이름1")
//                .participants(5)
//                .competitionStart(LocalDateTime.of(2024, 5, 19, 20, 0))
//                .competitionEnd(LocalDateTime.of(2024, 6, 19, 20, 0)).build();
//        competitionId1 = competitionService.createCompetition(competitionDto, loginUserDto);
//
//        CreateCompetitionRequestDto competitionDto1 = CreateCompetitionRequestDto.builder()
//                .competitionTitle("대회 이름2")
//                .participants(5)
//                .competitionStart(LocalDateTime.of(2022, 5, 19, 20, 0))
//                .competitionEnd(LocalDateTime.of(2022, 6, 19, 20, 0)).build();
//        competitionId2 = competitionService.createCompetition(competitionDto1, loginUserDto);
//    }
//
//    @Test
//    @DisplayName("대회 생성 및 조회")
//    public void create() {
//        CompetitionResponseDto competition = competitionService.getCompetition(competitionId1);
//
//        assertEquals(competition.getTitle(), "대회 이름1");
//        assertEquals(competition.getEndTime(), LocalDateTime.of(2024, 6, 19, 20, 0));
//    }
//
//    @Test
//    @DisplayName("끝난 대회 조회")
//    public void finish() {
//        Pageable pageable = PageRequest.of(0, 5);
//        LocalDateTime now = LocalDateTime.now();
//        Page<CompetitionListResponseDto> competitionList =
//                competitionService.getCompetitionList(true, pageable, now);
//        assertEquals(competitionList.getTotalElements(), 1);
//        assertEquals(competitionList.getContent().get(0).getTitle(), "대회 이름2");
//    }
//
//    @Test
//    @DisplayName("시작 전 대회 조회")
//    public void notFinish() {
//        Pageable pageable = PageRequest.of(0, 1);
//        LocalDateTime now = LocalDateTime.now();
//        Page<CompetitionListResponseDto> competitionList =
//                competitionService.getCompetitionList(false, pageable, now);
//
//        assertEquals(competitionList.getTotalElements(), 1);
//        assertEquals(competitionList.getContent().get(0).getTitle(), "대회 이름1");
//    }
//
//    @Nested
//    @DisplayName("대회 참여")
//    public class join {
//
//        @Test
//        @DisplayName("대회 참여 성공")
//        public void joinSuccess() {
//            Long memberId = memberIds.get(0);
//            LoginUserDto loginUserDto = LoginUserDto.builder()
//                    .memberId(memberId)
//                    .build();
//            memberCompetitionService.joinCompetition(loginUserDto, competitionId1);
//            CompetitionResponseDto competition = competitionService.getCompetition(competitionId1);
//
//            assertEquals(memberCompetitionService.getJoinMemberCount(competitionId1), 1);
//            assertEquals(competition.getMaxParticipants(), 5);
//            assertEquals(competition.getParticipants(), 1);
//
//        }
//
//        @Test
//        @DisplayName("대회 중복 참여")
//        public void joinDuplicate() {
//            Long memberId = memberIds.get(0);
//            LoginUserDto loginUserDto = LoginUserDto.builder()
//                    .memberId(memberId)
//                    .build();
//            memberCompetitionService.joinCompetition(loginUserDto, competitionId1);
//
//            Exception exception = assertThrows(DuplicateMemberWithCompetition.class, () -> {
//                memberCompetitionService.joinCompetition(loginUserDto, competitionId1);
//            });
//            assertTrue(exception.getMessage().contains("Duplicate request sent for the same competition."));
//            assertEquals(memberCompetitionService.getJoinMemberCount(competitionId1), 1);
//
//        }
//
//        @Test
//        @DisplayName("대회 참여 실패 - 인원 초과")
//        public void joinFail() {
//            for (int i = 0; i < 5; i++) {
//                Long memberId = memberIds.get(i);
//                LoginUserDto loginUserDto = LoginUserDto.builder()
//                        .memberId(memberId)
//                        .build();
//                memberCompetitionService.joinCompetition(loginUserDto, competitionId1);
//            }
//
//            assertEquals(memberCompetitionService.getJoinMemberCount(competitionId1), 5);
//
//            LoginUserDto loginUserDto = LoginUserDto.builder()
//                    .memberId(memberIds.get(5))
//                    .build();
//
//            Exception exception = assertThrows(ParticipantsWereInvitedParticipateException.class, () -> {
//                memberCompetitionService.joinCompetition(loginUserDto, competitionId1);
//            });
//            assertTrue(exception.getMessage().contains("참가 가능한 인원이 초과"));
//
//            assertEquals(memberCompetitionService.getJoinMemberCount(competitionId1), 5);
//        }
//    }
//
//    //todo : 점수 테스트 코드 수정하기
//    @Nested
//    @DisplayName("점수")
//    public class score {
//        private List<Long> questionIds = new ArrayList<>();
//
//        @BeforeEach
//        public void setUp() {
//            //대회에 문제 추가
//            List<QuestionIdRequestDto> requestDtos = new ArrayList<>();
//            for (int i = 0; i < 10; i++) {
//                Question question = Question.builder()
//                        .title("문제 제목" + i)
//                        .description("문제 내용" + i)
//                        .explain("정답 설명"+i)
//                        .build();
//                questionRepository.save(question);
//                for (int j = 1; j <= 4; j++) {
//                    Choice choice = Choice.builder()
//                            .number(j)
//                            .content("보기" + j)
//                            .answer(i % 4 == j - 1)
//                            .question(question)
//                            .build();
//                    choiceRepository.save(choice);
//                }
//
//                requestDtos.add(QuestionIdRequestDto.builder()
//                        .id(question.getId())
//                        .build()
//                );
//                questionIds.add(question.getId());
//            }
//            Workbook workbook = workbookRepository.findAll().get(0);
//
//            WorkbookQuestionRequestDto requestDto = WorkbookQuestionRequestDto.builder()
//                    .questionIds(requestDtos)
//                    .workbookId(workbook.getId()).build();
//
//            workbookService.addQuestion(requestDto);
//        }
//
//        @Test
//        @DisplayName("대회 점수 채점 및 점수 조회")
//        public void score() {
//            Long memberId = memberIds.get(0);
//            LoginUserDto loginUserDto = LoginUserDto.builder()
//                    .memberId(memberId)
//                    .build();
//            memberCompetitionService.joinCompetition(loginUserDto, competitionId1);
//            for (int i = 1; i <= 4; i++) {
//                scoring(memberId, i);
//                //1,2번 3개, 3,4번은 2개.
//                if (i <= 2) {
//                    assertEquals(competitionScoreService.getScore(memberId, competitionId1), 3);
//                } else {
//                    assertEquals(competitionScoreService.getScore(memberId, competitionId1), 2);
//                }
//            }
//            ;
//            Exception exception = assertThrows(NotFoundMemberCompetition.class, () -> {
//                competitionScoreService.getAnswer(memberIds.get(1), competitionId1);
//            });
//            assertTrue(exception.getMessage().contains("Not Found MemberCompetition"));
//        }
//
//        @Test
//        @DisplayName("대회 랭킹 조회")
//        public void ranking() {
//            for (int i = 1; i <= 4; i++) {
//                LoginUserDto loginUserDto = LoginUserDto.builder()
//                        .memberId(memberIds.get(i))
//                        .build();
//                memberCompetitionService.joinCompetition(loginUserDto, competitionId1);
//                scoring(memberIds.get(i), i);
//            }
//
//            Pageable pageable = PageRequest.of(0, 5);
//            Page<CompetitionRankingResponseDto> competitionRanking =
//                    competitionService.getCompetitionRanking(competitionId1, pageable);
//
//            assertEquals(competitionRanking.getTotalElements(), 4);
//        }
//
//
//        public void scoring(Long memberId, int choice) {
//            List<CompetitionScoreRequestDto.CompetitionAnswerRequestDto> questionDto = new ArrayList<>();
//            for (int i = 0; i < 10; i++) {
//                questionDto.add(CompetitionScoreRequestDto.CompetitionAnswerRequestDto.builder()
//                        .questionId(questionIds.get(i))
//                        .choiceNumber(choice)
//                        .build()
//                );
//            }
//            CompetitionScoreRequestDto scoreRequestDto = CompetitionScoreRequestDto.builder()
//                    .competitionId(competitionId1)
//                    .endTime(LocalDateTime.of(2023, 7, 20, 0, 0))
//                    .questions(questionDto)
//                    .build();
//            LoginUserDto loginUserDto = LoginUserDto.builder()
//                    .memberId(memberId)
//                    .build();
//            competitionScoreService.scoring(scoreRequestDto, loginUserDto);
//        }
//    }
//
//}