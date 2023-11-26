package com.cstudy.modulecommon.repository.workbook;


import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.domain.workbook.Workbook;
import com.cstudy.modulecommon.domain.workbook.WorkbookQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkbookQuestionRepository extends JpaRepository<WorkbookQuestion, Long> {
    Optional<WorkbookQuestion> findByWorkbookAndQuestion(Workbook workbook, Question question);
    boolean existsByWorkbookAndQuestion(Workbook workbook, Question question);
}
