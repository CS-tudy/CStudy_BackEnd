package com.cstudy.modulecommon.domainEntity.reviewQuestion;

import com.cstudy.modulecommon.domain.reviewQuestion.ReviewNote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ReviewNoteTest {
    @Test
    public void createReviewNote() {
        ReviewNote reviewNote = ReviewNote.builder()
                .createdDate(LocalDateTime.now())
                .questionId(1L)
                .successChoiceNumber(2)
                .isAnswer(true)
                .build();

        Assertions.assertEquals(1L, reviewNote.getQuestionId());
        Assertions.assertEquals(2, reviewNote.getSuccessChoiceNumber());
        Assertions.assertEquals(true, reviewNote.isAnswer());
    }

    @Test
    public void createFailNote() {
        ReviewNote reviewNote = ReviewNote.builder()
                .createdDate(LocalDateTime.now())
                .questionId(1L)
                .failChoiceNumber(3)
                .isAnswer(false)
                .build();

        Assertions.assertEquals(1L, reviewNote.getQuestionId());
        Assertions.assertEquals(3, reviewNote.getFailChoiceNumber());
        Assertions.assertEquals(false, reviewNote.isAnswer());
    }
}