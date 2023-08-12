package com.cstudy.modulecommon.repository.competition;

import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.competition.MemberCompetition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface MemberCompetitionRepository extends JpaRepository<MemberCompetition, Long> {

    @Query("SELECT MC FROM MemberCompetition MC " +
            "JOIN FETCH MC.member M " +
            "JOIN FETCH MC.competition C " +
            "WHERE C.id = :competitionId")
    List<MemberCompetition>findAllWithMemberAndCompetition(@Param("competitionId") Long competitionId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT CASE WHEN COUNT(mc) > 0 THEN true ELSE false END " +
            "FROM MemberCompetition mc " +
            "WHERE mc.member.id = :memberId AND mc.competition.id = :competitionId")
    boolean existsByMemberIdAndCompetitionId(@Param("memberId") Long memberId, @Param("competitionId") Long competitionId);

    @Query("SELECT MC FROM MemberCompetition MC " +
            "LEFT JOIN Competition C ON C = MC.competition " +
            "LEFT JOIN Member M ON M = MC.member " +
            "WHERE M.id = :memberId " +
            "AND C.id = :competitionId"
    )
    Optional<MemberCompetition> findByMemberIdAndCompetitionId(@Param("memberId") Long memberId,
                                                    @Param("competitionId") Long competitionId);
    @EntityGraph(attributePaths = {"member"})
    Page<MemberCompetition> findByCompetition(Competition competition, Pageable pageable);

    @Query("SELECT M.id FROM MemberCompetition MC " +
            "LEFT JOIN Member M ON M = MC.member " +
            "LEFT JOIN Competition C ON C = MC.competition " +
            "WHERE MC.score != NULL " +
            "AND C.id = :competitionId " +
            "ORDER BY MC.score DESC, MC.endTime ASC"
    )
    List<Long> findFinishMember(@Param("competitionId") Long competitionId);
}
