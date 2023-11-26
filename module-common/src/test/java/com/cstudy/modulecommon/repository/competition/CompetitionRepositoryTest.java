package com.cstudy.modulecommon.repository.competition;

import com.cstudy.modulecommon.config.QueryDslConfig;
import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.repository.choice.ChoiceRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
class CompetitionRepositoryTest {


    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChoiceRepository choiceRepository;


    @Test
    public void 대회_마감시간_이전_조회() {
        LocalDateTime now = LocalDateTime.now();
        Competition competition = Competition.builder()
                .competitionTitle("제목")
                .competitionStart(now)
                .competitionEnd(now.plusHours(1))
                .participants(5)
                .build();

        competitionRepository.save(competition);

        Page<Competition> competitions = competitionRepository.findByCompetitionEndBefore(LocalDateTime.now(), PageRequest.of(0, 10));

        assertTrue(competitions.isEmpty());
    }

    @Test
    public void 대회_마감시간_이후_조회() {
        LocalDateTime now = LocalDateTime.now();
        Competition competition = Competition.builder()
                .competitionTitle("제목")
                .competitionStart(now)
                .competitionEnd(now.plusHours(1))
                .participants(5)
                .build();

        competitionRepository.save(competition);

        Page<Competition> competitions = competitionRepository.findByCompetitionEndAfter(LocalDateTime.now(), PageRequest.of(0, 10));

        assertFalse(false);
    }

    @Test
    public void 낙관적_잠금을_위한_ID로_조회() {
        LocalDateTime now = LocalDateTime.now();
        Competition competition = Competition.builder()
                .competitionTitle("제목")
                .competitionStart(now)
                .competitionEnd(now.plusHours(1))
                .participants(5)
                .build();

        competition = competitionRepository.save(competition);

        Optional<Competition> foundCompetition = competitionRepository.findByIdForUpdateOptimistic(competition.getId());

        assertTrue(foundCompetition.isPresent());
        assertEquals(competition.getId(), foundCompetition.get().getId());
    }


    @Test
    public void 대회_문제_보기() throws Exception {
        //given
        final String content = "보기";
        final String title = "문제_제목";
        final String desc = "문제_설명";
        final String explain = "문제_정답";

        Choice choice = Choice.of(1, content + "1", false);
        Choice choice1 = Choice.of(2, content + "2", false);
        Choice choice2 = Choice.of(3, content + "3", false);
        Choice choice3 = Choice.of(4, content + "4", true);

        choiceRepository.saveAll(List.of(choice, choice1, choice2, choice3));

        Question question = Question.of(title, desc,explain,List.of(choice, choice1, choice2, choice3));
        questionRepository.save(question);
        //when
        List<Object[]> questionsWithChoices = competitionRepository.findQuestionsWithChoices(1L);

        //Then
        //assertThat().isEqualTo();
    }
}