package com.cstudy.modulecommon.domain.reviewQuestion;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Component
@Getter
@Document(collection = "reviewNote")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewNote {
    @Id
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    private Long questionId;

    private int successChoiceNumber;
    private int failChoiceNumber;

    private boolean isAnswer;


//    @Builder
//    public ReviewNote(String id, LocalDateTime createdDate, Long questionId, int successChoiceNumber, boolean isAnswer) {
//        this.id = id;
//        this.createdDate = createdDate;
//        this.questionId = questionId;
//        this.successChoiceNumber = successChoiceNumber;
//        this.isAnswer = isAnswer;
//    }
//
//    @Builder
//    public ReviewNote(String id, LocalDateTime createdDate, Long questionId, int successChoiceNumber, int failChoiceNumber, boolean isAnswer) {
//        this.id = id;
//        this.createdDate = createdDate;
//        this.questionId = questionId;
//        this.successChoiceNumber = successChoiceNumber;
//        this.failChoiceNumber = failChoiceNumber;
//        this.isAnswer = isAnswer;
//    }

    public ReviewNote(LocalDateTime createdDate, Long questionId, int failChoiceNumber, boolean isAnswer) {
        this.createdDate = createdDate;
        this.questionId = questionId;
        this.failChoiceNumber = failChoiceNumber;
        this.isAnswer = isAnswer;
    }

    @Builder
    public ReviewNote(String id, LocalDateTime createdDate, Long questionId, int successChoiceNumber, int failChoiceNumber, boolean isAnswer) {
        this.id = id;
        this.createdDate = createdDate;
        this.questionId = questionId;
        this.successChoiceNumber = successChoiceNumber;
        this.failChoiceNumber = failChoiceNumber;
        this.isAnswer = isAnswer;
    }

    //    public static ReviewNote createFailNote(LocalDateTime createdDate, Long questionId, boolean isAnswer, int failChoiceNumber) {
//        return new ReviewNote(createdDate, questionId,failChoiceNumber,isAnswer);
//    }

}
