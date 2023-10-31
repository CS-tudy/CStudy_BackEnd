package com.cstudy.modulecommon.repository.choice;

import com.cstudy.modulecommon.config.QueryDslConfig;
import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.question.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
public class ChoiceRepositoryTest {

    @Autowired
    ChoiceRepository choiceRepository;

    @Test
    public void testFindByQuestionAndNumber() {
        Question question = Question.builder()
                .title("문제1")
                .description("설명")
                .explain("설명")
                .build();

        Choice choice = Choice.builder()
                .question(question)
                .number(1)
                .build();

        choiceRepository.save(choice);

        Optional<Choice> foundChoice = choiceRepository.findByQuestionAndNumber(question, 1);

        assertTrue(foundChoice.isPresent());
        assertEquals(choice.getId(), foundChoice.get().getId());
        assertEquals(choice.getNumber(), foundChoice.get().getNumber());
    }
}