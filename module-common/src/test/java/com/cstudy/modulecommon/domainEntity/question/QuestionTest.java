package com.cstudy.modulecommon.domainEntity.question;

import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.question.Category;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.question.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {
    private Category category;
    private List<Choice> choices;
    private Set<MemberQuestion> questions;

    @BeforeEach
    public void setUp() {
        category = new Category();
        choices = List.of(new Choice(), new Choice());
        questions = new HashSet<>();
    }

    @Test
    public void createQuestion() {
        Question question = Question.builder()
                .title("제목")
                .description("설명")
                .explain("설명")
                .category(category)
                .choices(choices)
                .questions(questions)
                .build();

        Assertions.assertEquals("제목", question.getTitle());
        Assertions.assertEquals("설명", question.getDescription());
        Assertions.assertEquals("설명", question.getExplain());
        Assertions.assertEquals(category, question.getCategory());
        Assertions.assertEquals(choices, question.getChoices());
        Assertions.assertEquals(questions, question.getQuestions());
    }

    @Test
    public void setChoices() {
        Question question = new Question();

        question.setChoices(choices);

        Assertions.assertEquals(choices, question.getChoices());
    }
}