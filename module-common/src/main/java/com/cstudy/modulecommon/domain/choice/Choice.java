package com.cstudy.modulecommon.domain.choice;

import com.cstudy.modulecommon.domain.question.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "choice_number")
    private int number;

    private String content;

    private boolean answer = false;

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


}