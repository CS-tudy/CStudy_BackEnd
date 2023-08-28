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

    /********************************* PK 필드 *********************************/
    @Id
    private String userName;

    /********************************* PK가 아닌 필드 *********************************/
    private List<String> successQuestion;

    private List<String> failQuestion;


    /********************************* 연관관계 매핑 *********************************/
    @DBRef(lazy = true)
    private List<ReviewNote> reviewNotes = new ArrayList<>();


    /********************************* 빌더 *********************************/
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
