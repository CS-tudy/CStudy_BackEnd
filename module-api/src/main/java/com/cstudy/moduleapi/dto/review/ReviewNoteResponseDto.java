package com.cstudy.moduleapi.dto.review;

import com.cstudy.modulecommon.domain.reviewQuestion.ReviewNote;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewNoteResponseDto {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    private Long questionId;
    private int successChoiceNumber;
    private int failChoiceNumber;
    private boolean isAnswer;

    public ReviewNoteResponseDto(ReviewNote reviewNote) {
        this.id = reviewNote.getId();
        this.createdDate = reviewNote.getCreatedDate();
        this.questionId = reviewNote.getQuestionId();
        this.successChoiceNumber = reviewNote.getSuccessChoiceNumber();
        this.failChoiceNumber = reviewNote.getFailChoiceNumber();
        this.isAnswer = reviewNote.isAnswer();
    }
}
