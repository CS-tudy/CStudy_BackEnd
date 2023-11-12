package com.cstudy.modulecommon.repository.competition;

import com.cstudy.modulecommon.domain.competition.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    Page<Competition> findByCompetitionEndBefore(LocalDateTime time, Pageable pageable);

    Page<Competition> findByCompetitionEndAfter(LocalDateTime time, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select m from Competition m where m.id = :id")
    Optional<Competition> findByIdForUpdateOptimistic(@Param("id") Long id);


    @Query(value = "SELECT " +
            "    question0_.question_id as question_id, " +
            "    question0_.question_description as question_description, " +
            "    GROUP_CONCAT(choices4_.choice_number) as choice_numbers, " +
            "    GROUP_CONCAT(choices4_.content) as choice_contents " +
            "FROM " +
            "    question question0_ " +
            "LEFT OUTER JOIN " +
            "    workbook_question workbookqu1_ " +
            "        ON question0_.question_id=workbookqu1_.question_id " +
            "LEFT OUTER JOIN " +
            "    workbook workbook2_ " +
            "        ON workbookqu1_.workbook_id=workbook2_.workbook_id " +
            "LEFT OUTER JOIN " +
            "    competition competitio3_ " +
            "        ON workbook2_.workbook_id=competitio3_.workbook_id " +
            "LEFT OUTER JOIN " +
            "    choice choices4_ " +
            "        ON question0_.question_id=choices4_.question_id " +
            "WHERE " +
            "    competitio3_.competition_id=:competitionId " +
            "GROUP BY " +
            "    question0_.question_id", nativeQuery = true)
    List<Object[]> findQuestionsWithChoices(@Param("competitionId") Long competitionId);
}
