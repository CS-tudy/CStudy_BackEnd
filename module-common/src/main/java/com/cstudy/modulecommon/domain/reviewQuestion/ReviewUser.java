package com.cstudy.modulecommon.domain.reviewQuestion;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@Getter
@Document(collection = "reviewUser")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewUser {
    @Id
    private String userName;

    private List<String> successQuestion;

    private List<String> failQuestion;

    @DBRef(lazy = true)
    private List<ReviewNote> reviewNotes = new ArrayList<>();

    @Builder
    public ReviewUser(
            String userName,
            List<String> successQuestion,
            List<String> failQuestion,
            List<ReviewNote> reviewNotes
    ) {
        this.userName = userName;
        this.successQuestion = successQuestion;
        this.failQuestion = failQuestion;
        this.reviewNotes = reviewNotes;
    }

}
