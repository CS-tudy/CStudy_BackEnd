package com.cstudy.modulecommon.repository.question;

import com.cstudy.modulecommon.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {

    @Query("SELECT DISTINCT Q FROM Question Q" +
            " LEFT JOIN FETCH Q.choices C " +
            "JOIN FETCH Q.category CA " +
            "WHERE Q.id = :id")
    Optional<Question> findQuestionWithChoicesAndCategoryById(@Param("id") Long id);

    @Query("select q from Question q" +
            " join fetch q.category" +
            " where q.id = :questionId")
    Optional<Question> findByIdFetchJoinCategory(@Param("questionId") Long questionId);

}
