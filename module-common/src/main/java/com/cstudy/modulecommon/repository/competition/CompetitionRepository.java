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
            "    q.question_id as question_id, " +
            "    q.question_title as question_title, " +
            "    q.question_description as question_description, " +
            "    CONCAT('[', GROUP_CONCAT(c.choice_number ORDER BY c.choice_number SEPARATOR ','), ']') as choice_numbers, " +
            "    JSON_ARRAYAGG(c.content)  as choice_contents " +
            "FROM " +
            "    question q " +
            "INNER JOIN " +
            "    workbook_question wq " +
            "        ON q.question_id = wq.question_id " +
            "INNER JOIN " +
            "    workbook w " +
            "        ON wq.workbook_id = w.workbook_id " +
            "INNER JOIN " +
            "    competition comp " +
            "        ON w.workbook_id = comp.workbook_id " +
            "INNER JOIN " +
            "    choice c " +
            "        ON q.question_id = c.question_id " +
            "WHERE " +
            "    comp.competition_id = :competitionId " +
            "GROUP BY " +
            "    q.question_id", nativeQuery = true)
    List<Object[]> findQuestionsWithChoices(@Param("competitionId") Long competitionId);



}
