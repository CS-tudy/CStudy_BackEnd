package com.cstudy.modulecommon.domainEntity.workbook;

import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.workbook.Workbook;
import com.cstudy.modulecommon.domain.workbook.WorkbookQuestion;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WorkbookTest {
    public void 특정_문제집_생성_테스트() {
        Workbook workbook = Workbook.builder()
                .title("Sample Workbook")
                .description("Sample Description")
                .endTime(LocalDateTime.now())
                .build();

        Assertions.assertEquals("Sample Workbook", workbook.getTitle());
        Assertions.assertEquals("Sample Description", workbook.getDescription());
        Assertions.assertNotNull(workbook.getCreatedAt());
    }

    @Test
    public void 문제_추가_테스트() {
        Workbook workbook = new Workbook();

        WorkbookQuestion question = Mockito.mock(WorkbookQuestion.class);
        workbook.addQuestion(question);

        Assertions.assertEquals(1, workbook.getQuestions().size());
        Assertions.assertEquals(question, workbook.getQuestions().get(0));
    }

    @Test
    public void 경쟁_설정_테스트() {
        Workbook workbook = new Workbook();
        Competition competition = Mockito.mock(Competition.class);

        workbook.setCompetition(competition);

        Assertions.assertEquals(competition, workbook.getCompetition());
        Assertions.assertEquals(competition.getCompetitionEnd(), workbook.getCompetitionEndTime());
    }

    @Test
    public void 문제집_변경_테스트() {
        Workbook workbook = Workbook.builder()
                .title("Original Title")
                .description("Original Description")
                .endTime(LocalDateTime.now())
                .build();

        UpdateWorkbookRequestDto workbookDto = new UpdateWorkbookRequestDto();
        workbookDto.setTitle("Updated Title");
        workbookDto.setDescription("Updated Description");

        workbook.changeWorkbook(workbookDto);

        Assertions.assertEquals("Updated Title", workbook.getTitle());
        Assertions.assertEquals("Updated Description", workbook.getDescription());
    }

    @Test
    public void 문제_삭제_테스트() {
        Workbook workbook = new Workbook();
        WorkbookQuestion question = Mockito.mock(WorkbookQuestion.class);

        workbook.addQuestion(question);
        workbook.deleteQuestion(question);

        Assertions.assertEquals(0, workbook.getQuestions().size());
    }
}