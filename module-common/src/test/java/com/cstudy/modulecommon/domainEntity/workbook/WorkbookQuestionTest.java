package com.cstudy.moduleapi.domainEntity.workbook;

import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.domain.workbook.Workbook;
import com.cstudy.modulecommon.domain.workbook.WorkbookQuestion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WorkbookQuestionTest {

    @Test
    public void 문제집_문제_연결_테스트() {
        Workbook workbook = Mockito.mock(Workbook.class);
        Question question = Mockito.mock(Question.class);

        WorkbookQuestion workbookQuestion = WorkbookQuestion.builder()
                .workbook(workbook)
                .question(question)
                .build();

        Assertions.assertEquals(workbook, workbookQuestion.getWorkbook());
        Assertions.assertEquals(question, workbookQuestion.getQuestion());
    }

}