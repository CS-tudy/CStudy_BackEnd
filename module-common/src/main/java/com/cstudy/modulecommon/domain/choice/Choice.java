package com.cstudy.modulecommon.domain.choice;

import com.cstudy.modulecommon.domain.question.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Choice {
    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    @Column(name = "choice_number")
    private int number;

    private String content;

    private boolean answer = false;

    /********************************* 연관관계 매핑 *********************************/

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Question question;

    @Builder
    public Choice(Long id, int number, String content, boolean answer, Question question) {
        this.id = id;
        this.number = number;
        this.content = content;
        this.answer = answer;
        this.question = question;
    }

    public static Choice of(int number, String content, boolean isAnswer) {
        return Choice.builder()
                .number(number)
                .content(content)
                .answer(isAnswer)
                .build();
    }


}