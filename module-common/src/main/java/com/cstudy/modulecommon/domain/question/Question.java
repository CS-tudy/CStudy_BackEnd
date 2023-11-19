package com.cstudy.modulecommon.domain.question;

import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.workbook.WorkbookQuestion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "question", indexes = {
        @Index(name = "idx_question_title", columnList = "question_title")
//}, uniqueConstraints = {
//        @UniqueConstraint(name = "description", columnNames = {"question_description"}),
//        @UniqueConstraint(name = "title", columnNames = {"question_title"}),
})
public class Question {
    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    @Column(nullable = false, name = "question_title")
    private String title;

    @Column(nullable = false, name = "question_description")
    private String description;

    @Column(nullable = false, name = "question_explain")
    private String explain;

    /********************************* 연관관계 매핑 *********************************/
    @JsonBackReference
    @OneToMany(
            mappedBy = "question",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    Set<MemberQuestion> questions = new HashSet<>();

    @OneToMany(
            mappedBy = "question",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    List<Choice> choices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(
            mappedBy = "question",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<WorkbookQuestion> workbookQuestions;

    /********************************* 빌더 *********************************/

    @Builder
    public Question(
            Long id,
            String title,
            String description,
            String explain,
            Set<MemberQuestion> questions,
            List<Choice> choices,
            Category category
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.explain = explain;
        this.questions = questions;
        this.choices = choices;
        this.category = category;
    }

    /********************************* 비즈니스 로직 *********************************/

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

}
