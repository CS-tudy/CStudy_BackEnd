package com.cstudy.moduleapi.application.competition.application.impl;

import com.cstudy.moduleapi.config.ServiceTestBase;
import com.cstudy.moduleapi.dto.competition.CreateCompetitionRequestDto;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimisticFacadeTest extends ServiceTestBase {


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
        memberService.signUpForTest(memberSignupRequest);

        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(1L)
                .memberEmail(MemberTestEnum.ADMIN_EMAIL.getMessage())
                .roles(List.of(RoleEnum.ADMIN.getRoleName()))
                .build();

        CreateCompetitionRequestDto requestDto = CreateCompetitionRequestDto.builder()
                .competitionTitle("CS 대회")
                .participants(100)
                .competitionEnd(LocalDateTime.now().plusHours(1))
                .build();

        competitionService.createCompetition(requestDto);



        CreateCompetitionRequestDto requestDto2 = CreateCompetitionRequestDto.builder()
                .competitionTitle("CS 대회")
                .participants(100)
                .competitionEnd(LocalDateTime.now().plusHours(1))
                .build();

        competitionService.createCompetition(requestDto2);
    }

    @Test
    @DisplayName("싱글 스레드 환경 테스트")
    public void CreateJoinMemberWithSingleThread() throws Exception {
        //given
        LoginUserDto userDto = LoginUserDto.builder()
                .memberId(1L)
                .build();
        //when
        facade.joinCompetition(userDto, 2L);

        //Then
        Competition competition = competitionRepository.findById(2L)
                .orElseThrow(RuntimeException::new);

        Assertions.assertThat(competition.getParticipants()).isEqualTo(99);
    }

    @Test
    @DisplayName("멀티쓰레드 환경에서 테스트")
    public void CreateJoinMemberWithMultiThread() throws Exception {
        LoginUserDto userDto = LoginUserDto.builder()
                .memberId(1L)
                .build();

        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    facade.joinCompetition(userDto, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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