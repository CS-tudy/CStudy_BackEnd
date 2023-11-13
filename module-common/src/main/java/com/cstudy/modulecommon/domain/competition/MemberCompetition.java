package com.cstudy.modulecommon.domain.competition;

import com.cstudy.modulecommon.domain.member.Member;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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

    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_competition_id")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/
    private Integer score;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /********************************* 동시성 버전 *********************************/

    @Version
    private Long version;

    /********************************* 연관관계 매핑 *********************************/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(
        mappedBy = "memberCompetition",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    private List<CompetitionScore> competitionScore = new ArrayList<>();


    /********************************* 빌더 *********************************/

    @Builder
    public MemberCompetition(Long id, Competition competition, Member member) {
        this.id = id;
        this.competition = competition;
        this.member = member;
    }

    /********************************* 비니지스 로직 *********************************/

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
