package com.cstudy.moduleapi.domainEntity.question;

import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.question.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberQuestionTest {
    private Member member;
    private Question question;

    @BeforeEach
    public void setUp() {
        member = new Member();
        question = new Question();
    }

    @Test
    @DisplayName("회원-문제 생성")
    public void createMemberQuestion() throws Exception{
        MemberQuestion memberQuestion = MemberQuestion.builder()
                .success(3)
                .fail(1)
                .solveTime(500L)
                .member(member)
                .question(question)
                .build();

        Assertions.assertEquals(3, memberQuestion.getSuccess());
        Assertions.assertEquals(1, memberQuestion.getFail());
        Assertions.assertEquals(500L, memberQuestion.getSolveTime());
        Assertions.assertEquals(member, memberQuestion.getMember());
        Assertions.assertEquals(question, memberQuestion.getQuestion());
    }
}