package com.cstudy.modulecommon.repository.competition;

import com.cstudy.modulecommon.config.QueryDslConfig;
import com.cstudy.modulecommon.domain.competition.Competition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
class CompetitionRepositoryTest {
    @Autowired
    private CompetitionRepository competitionRepository;

    @Test
    public void 대회_마감시간_이전_조회() {
        Competition competition = new Competition();

        competitionRepository.save(competition);

        Page<Competition> competitions = competitionRepository.findByCompetitionEndBefore(LocalDateTime.now(), PageRequest.of(0, 10));

        assertTrue(competitions.isEmpty());
    }

    @Test
    public void 대회_마감시간_이후_조회() {
        Competition competition = new Competition();

        competitionRepository.save(competition);

        Page<Competition> competitions = competitionRepository.findByCompetitionEndAfter(LocalDateTime.now(), PageRequest.of(0, 10));

        assertTrue(competitions.isEmpty());
    }

    @Test
    public void 낙관적_잠금을_위한_ID로_조회() {
        Competition competition = new Competition();

        competition = competitionRepository.save(competition);

        Optional<Competition> foundCompetition = competitionRepository.findByIdForUpdateOptimistic(competition.getId());

        assertTrue(foundCompetition.isPresent());
        assertEquals(competition.getId(), foundCompetition.get().getId());
    }
}