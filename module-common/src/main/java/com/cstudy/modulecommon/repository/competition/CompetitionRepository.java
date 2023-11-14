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
            "    question.question_id as question_id, " +
            "    question.question_title as question_title, " +
            "    question.question_description as question_description, " +
            "    JSON_ARRAYAGG(choices.choice_number) as choice_numbers, " +
            "    JSON_ARRAYAGG(choices.content) as choice_contents " +
            "FROM " +
            "    question " +
            "LEFT OUTER JOIN " +
            "    workbook_question workbook_question_alias " +
            "        ON question.question_id=workbook_question_alias.question_id " +
            "LEFT OUTER JOIN " +
            "    workbook workbook_alias " +
            "        ON workbook_question_alias.workbook_id=workbook_alias.workbook_id " +
            "LEFT OUTER JOIN " +
            "    competition competition_alias " +
            "        ON workbook_alias.workbook_id=competition_alias.workbook_id " +
            "LEFT OUTER JOIN " +
            "    choice choices " +
            "        ON question.question_id=choices.question_id " +
            "WHERE " +
            "    competition_alias.competition_id=:competitionId " +
            "GROUP BY " +
            "    question.question_id", nativeQuery = true)
    List<Object[]> findQuestionsWithChoices(@Param("competitionId") Long competitionId);


}
