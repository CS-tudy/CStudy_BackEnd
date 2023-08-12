package com.cstudy.modulecommon.domain.competition;

import com.cstudy.modulecommon.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class MemberCompetition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_competition_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;


    @Version
    private Long version;

    private Integer score;

    private LocalDateTime endTime;

    @OneToMany(
        mappedBy = "memberCompetition",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    private List<CompetitionScore> competitionScore = new ArrayList<>();


    @Builder
    public MemberCompetition(Long id, Competition competition, Member member) {
        this.id = id;
        this.competition = competition;
        this.member = member;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    public void addCompetitionScore(CompetitionScore competitionScore) {
        this.competitionScore.add(competitionScore);
    }
}
