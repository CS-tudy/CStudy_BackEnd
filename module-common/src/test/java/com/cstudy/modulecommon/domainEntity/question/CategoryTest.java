package com.cstudy.moduleapi.domainEntity.question;

import com.cstudy.modulecommon.domain.question.Category;
import com.cstudy.modulecommon.domain.question.Question;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    public void testCreateCategory() {
        Set<Question> questions = new HashSet<>();
        questions.add(new Question());

        Category category = Category.builder()
                .categoryTitle("네트워크")
                .questions(questions)
                .build();

        Assertions.assertEquals("네트워크", category.getCategoryTitle());
        Assertions.assertEquals(1, category.getQuestions().size());
    }

    @Test
    public void testCreateEmptyCategory() {
        Category category = new Category();

        Assertions.assertEquals(null, category.getCategoryTitle());
        Assertions.assertEquals(0, category.getQuestions().size());
    }


}