package com.cstudy.modulecommon.domainEntity.member;

import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.role.Role;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    private Member member;

    @BeforeEach
    public void setUp() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_USER"));
        member = Member.builder()
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .roles(roles)
                .build();
    }

    @Test
    @DisplayName("랭킹 점수 더하기")
    public void addRankingPoint() {
        ChoiceAnswerRequestDto requestDto = new ChoiceAnswerRequestDto();
        requestDto.setTime(500L);

        double initialRankingPoint = member.getRankingPoint();
        member.addRankingPoint(requestDto);
        double expectedRankingPoint = initialRankingPoint + 3.0 + (1 - (requestDto.getTime() / 1000.0));

        Assertions.assertEquals(expectedRankingPoint, member.getRankingPoint(), 0.001);
    }

    @Test
    @DisplayName("랭킹 점수 마이너스")
    public void testMinusRankingPoint() {
        double initialRankingPoint = member.getRankingPoint();
        member.minusRankingPoint(2.0);
        double expectedRankingPoint = initialRankingPoint - 2.0;

        Assertions.assertEquals(expectedRankingPoint, member.getRankingPoint(), 0.001);
    }

}