package com.cstudy.modulecommon.domain.question;

import com.cstudy.modulecommon.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class MemberQuestion {
    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    @Column(name = "question_success")
    private int success;

    @Column(name = "question_fail")
    private int fail;

    private Long solveTime;

    /********************************* 연관관계 매핑 *********************************/
    @JsonBackReference
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonManagedReference
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "question_id")
    private Question question;

    /********************************* 빌더 *********************************/
    @Builder
    public MemberQuestion(
            int success,
            int fail,
            Long solveTime,
            Member member,
            Question question
    ) {
        this.success = success;
        this.fail = fail;
        this.solveTime = solveTime;
        this.member = member;
        this.question = question;
    }
}
