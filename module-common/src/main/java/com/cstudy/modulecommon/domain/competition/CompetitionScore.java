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

    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competition_score_id")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    private Integer choiceNumber;
    private boolean success;

    /********************************* 연관관계 매핑 *********************************/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_competition_id")
    private MemberCompetition memberCompetition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;


    /********************************* 빌더 *********************************/
    @Builder
    public CompetitionScore(MemberCompetition memberCompetition, Question question, Integer choiceNumber){
        this.memberCompetition = memberCompetition;
        this.question = question;
        this.choiceNumber = choiceNumber;
        this.success = false;
    }

    /********************************* 비즈니스 로직 *********************************/
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
