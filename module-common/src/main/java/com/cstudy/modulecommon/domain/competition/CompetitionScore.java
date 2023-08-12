package com.cstudy.modulecommon.domain.competition;

import com.cstudy.modulecommon.domain.question.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CompetitionScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competition_score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_competition_id")
    private MemberCompetition memberCompetition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private Integer choiceNumber;
    private boolean success;

    @Builder
    public CompetitionScore(MemberCompetition memberCompetition, Question question, Integer choiceNumber){
        this.memberCompetition = memberCompetition;
        this.question = question;
        this.choiceNumber = choiceNumber;
        this.success = false;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
