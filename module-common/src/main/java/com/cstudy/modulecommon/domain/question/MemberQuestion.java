package com.cstudy.modulecommon.domain.question;

import com.cstudy.modulecommon.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class MemberQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_success")
    private int success;

    @Column(name = "question_fail")
    private int fail;

    private Long solveTime;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "question_id")
    private Question question;

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
