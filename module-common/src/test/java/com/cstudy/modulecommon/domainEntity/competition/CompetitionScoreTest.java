package com.cstudy.modulecommon.domainEntity.competition;

import com.cstudy.modulecommon.domain.competition.CompetitionScore;
import com.cstudy.modulecommon.domain.question.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompetitionScoreTest {
    private Question question;

    @BeforeEach
    public void setUp() {
        question = new Question();
    }

    @Test
    public void testCreateCompetitionScore() {
        CompetitionScore score = CompetitionScore.builder()
                .memberCompetition(null)
                .question(question)
                .choiceNumber(2)
                .build();

        Assertions.assertEquals(question, score.getQuestion());
        Assertions.assertEquals(2, score.getChoiceNumber());
        Assertions.assertFalse(score.isSuccess());
    }

    @Test
    public void testSetSuccess() {
        CompetitionScore score = CompetitionScore.builder()
                .memberCompetition(null)
                .question(question)
                .choiceNumber(1)
                .build();

        Assertions.assertFalse(score.isSuccess());
        score.setSuccess(true);
        Assertions.assertTrue(score.isSuccess());
    }
}