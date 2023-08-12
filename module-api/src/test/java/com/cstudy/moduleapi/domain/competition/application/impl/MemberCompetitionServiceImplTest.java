package com.cstudy.moduleapi.domain.competition.application.impl;

import com.cstudy.moduleapi.application.competition.CompetitionService;
import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.dto.competition.CreateCompetitionRequestDto;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.error.competition.NotFoundCompetitionId;
import com.cstudy.modulecommon.repository.competition.CompetitionRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MemberCompetitionServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CompetitionService competitionService;

    @Autowired
    private MemberCompetitionService memberCompetitionService;

    @Autowired
    private CompetitionRepository competitionRepository;

    private static final String EMAIL = "test1213@email.com";
    private static final String PASSWORD = "test1234!";
    private static final String NAME = "김무건";

    @BeforeEach
    void setUp() {

        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .build();
        memberService.signUp(memberSignupRequest);

        MemberSignupRequest memberSignupRequest2 = MemberSignupRequest.builder()
                .email("EMAIL")
                .password("PASSWORD")
                .name("NAME")
                .build();

        memberService.signUp(memberSignupRequest2);


        CreateCompetitionRequestDto requestDto = CreateCompetitionRequestDto.builder()
                .competitionTitle("CS 대회")
                .participants(5)
                .competitionEnd(LocalDateTime.now().plusHours(1))
                .build();

        competitionService.createCompetition(requestDto);
    }

    @Test
    @DisplayName("싱글 스레드 환경에서 대회 참가 조회")
    public void findCompetitionAboutJoinCompetitionWithSingleThread() throws Exception {
        //given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(2L)
                .build();
        //when
        memberCompetitionService.joinCompetition(loginUserDto, 1L);

        //Then
        Competition competition = competitionRepository.findById(1L)
                .orElseThrow(() -> new NotFoundCompetitionId(1L));

        assertThat(competition.getParticipants()).isEqualTo(4);
    }

    @Test
    @DisplayName("싱글 스레드 환경에서 대회 실패 참가 조회")
    public void findCompetitionAboutInvalidJoinCompetitionWithSingleThread() throws Exception {
        //given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(2L)
                .build();
        //when
        memberCompetitionService.joinCompetition(loginUserDto, 1L);

        //Then
        Competition competition = competitionRepository.findById(1L)
                .orElseThrow(() -> new NotFoundCompetitionId(1L));

        assertThat(competition.getParticipants()).isNotEqualTo(3);
    }

    @Test
    @DisplayName("멀티쓰레드 환경에서 테스트")
    public void studyMemberCreateJoinMemberWithMultiThread() throws Exception {

        LoginUserDto userDto = LoginUserDto.builder()
                .memberId(1L)
                .build();

        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    memberCompetitionService.joinCompetition(userDto, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Competition competition = competitionRepository.findById(1L)
                .orElseThrow();

        assertThat(competition.getParticipants()).isEqualTo(0);
    }
}