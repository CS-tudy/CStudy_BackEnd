package com.cstudy.moduleapi.domainEntity.competition;

import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.competition.CompetitionScore;
import com.cstudy.modulecommon.domain.competition.MemberCompetition;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MemberCompetitionTest {

    private Competition competition;
    private Member member;

    @BeforeEach
    public void setUp() {
        competition = new Competition();
        member = new Member();
    }

    @Test
    public void createMemberCompetition() {
        MemberCompetition memberCompetition = MemberCompetition.builder()
                .competition(competition)
                .member(member)
                .build();

        Assertions.assertEquals(competition, memberCompetition.getCompetition());
        Assertions.assertEquals(member, memberCompetition.getMember());
    }

    @Test
    public void setScore() {
        MemberCompetition memberCompetition = MemberCompetition.builder()
                .competition(competition)
                .member(member)
                .build();

        memberCompetition.setScore(100);
        Assertions.assertEquals(100, memberCompetition.getScore());
    }

    @Test
    public void setEndTime() {
        LocalDateTime endTime = LocalDateTime.now();

        MemberCompetition memberCompetition = MemberCompetition.builder()
                .competition(competition)
                .member(member)
                .build();

        memberCompetition.setEndTime(endTime);
        Assertions.assertEquals(endTime, memberCompetition.getEndTime());
    }

    @Test
    public void addCompetitionScore() {
        MemberCompetition memberCompetition = MemberCompetition.builder()
                .competition(competition)
                .member(member)
                .build();

        CompetitionScore score = CompetitionScore.builder()
                .memberCompetition(memberCompetition)
                .question(new Question()) // Set appropriate Question instance here
                .choiceNumber(2)
                .build();

        memberCompetition.addCompetitionScore(score);

        Assertions.assertEquals(1, memberCompetition.getCompetitionScore().size());
        Assertions.assertEquals(score, memberCompetition.getCompetitionScore().get(0));
    }


}