package com.cstudy.modulecommon.repository.competition;

import com.cstudy.modulecommon.domain.competition.CompetitionScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompetitionScoreRepository extends JpaRepository<CompetitionScore, Long> {

    @Query(
            "SELECT CS FROM CompetitionScore CS " +
                    "LEFT JOIN CS.memberCompetition MC " +
                    "WHERE MC.member.id = :memberId AND" +
                    " MC.competition.id = :competitionId"
    )
    List<CompetitionScore> findByCompetitionIdAndMemberId(@Param("memberId") Long memberId, @Param("competitionId") Long competitionId);
}
