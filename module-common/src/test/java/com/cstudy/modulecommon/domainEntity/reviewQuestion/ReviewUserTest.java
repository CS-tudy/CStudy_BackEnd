package com.cstudy.moduleapi.domainEntity.reviewQuestion;

import com.cstudy.modulecommon.domain.reviewQuestion.ReviewNote;
import com.cstudy.modulecommon.domain.reviewQuestion.ReviewUser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReviewUserTest {
    @Test
    public void 특정사용자_생성_테스트() {
        List<String> successQuestions = new ArrayList<>();
        List<String> failQuestions = new LinkedList<>();
        List<ReviewNote> reviewNotes = new ArrayList<>();

        ReviewUser reviewUser = ReviewUser.builder()
                .userName("sampleUser")
                .successQuestion(successQuestions)
                .failQuestion(failQuestions)
                .reviewNotes(reviewNotes)
                .build();

        Assertions.assertEquals("sampleUser", reviewUser.getUserName());
        Assertions.assertEquals(successQuestions, reviewUser.getSuccessQuestion());
        Assertions.assertEquals(failQuestions, reviewUser.getFailQuestion());
        Assertions.assertEquals(reviewNotes, reviewUser.getReviewNotes());
    }

    @Test
    public void 널체크테스트() {
        ReviewUser reviewUser = new com.cstudy.modulecommon.domain.reviewQuestion.ReviewUser();

        reviewUser.nullCheck();

        Assertions.assertEquals(new LinkedList<>(), reviewUser.getSuccessQuestion());
        Assertions.assertEquals(new LinkedList<>(), reviewUser.getFailQuestion());
    }
}