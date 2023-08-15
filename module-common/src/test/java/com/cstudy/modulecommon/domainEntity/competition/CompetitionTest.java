package com.cstudy.modulecommon.domainEntity.competition;

import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.workbook.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CompetitionTest {
    private Workbook workbook;

    @BeforeEach
    public void setUp() {
        workbook = new Workbook();
    }

    @Test
    public void createCompetition() {
        LocalDateTime start = LocalDateTime.of(2023, 8, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 16, 18, 0);

        Competition competition = Competition.builder()
                .competitionTitle("Sample Competition")
                .participants(50)
                .competitionStart(start)
                .competitionEnd(end)
                .workbook(workbook)
                .build();

        Assertions.assertEquals("Sample Competition", competition.getCompetitionTitle());
        Assertions.assertEquals(50, competition.getParticipants());
        Assertions.assertEquals(start, competition.getCompetitionStart());
        Assertions.assertEquals(end, competition.getCompetitionEnd());
        Assertions.assertEquals(workbook, competition.getWorkbook());
    }

    @Test
    public void decreaseParticipantsCount() {
        Competition competition = Competition.builder()
                .competitionTitle("Sample Competition")
                .participants(50)
                .competitionStart(LocalDateTime.now())
                .competitionEnd(LocalDateTime.now().plusHours(2))
                .workbook(workbook)
                .build();

        int initialParticipants = competition.getParticipants();
        competition.decreaseParticipantsCount();
        int updatedParticipants = competition.getParticipants();

        Assertions.assertEquals(initialParticipants - 1, updatedParticipants);
    }
}