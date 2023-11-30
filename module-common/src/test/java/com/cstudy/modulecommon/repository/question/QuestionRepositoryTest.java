package com.cstudy.modulecommon.repository.question;

import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.question.Category;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.error.question.NotFoundQuestionId;
import com.cstudy.modulecommon.util.DataJpaCustomUtil;
import com.cstudy.modulecommon.util.TestEnum;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuestionRepositoryTest extends DataJpaCustomUtil {

    @Test
    public void 문제_카테고리_보기를_한방에_조회하기() throws Exception{
        //given
        List<Choice> choiceList = new ArrayList<>();
        Choice choice = Choice.of(1, "보기1", false);
        Choice choice1 = Choice.of(2, "보기2", true);
        Choice choice2 = Choice.of(3, "보기3", false);
        Choice choice3 = Choice.of(4, "보기4", false);
        choiceList.addAll(List.of(choice,choice1,choice2,choice));
        choiceRepository.saveAll(choiceList);

        Question question = createQuestion(TestEnum.QUESTION_TITLE.getMessage(), TestEnum.QUESTION_DESCRIPTION.getMessage(), TestEnum.QUESTION_EXPLAIN.getMessage(), choiceList);
        //when
        Question getQuestion = questionRepository.findByIdFetchJoinCategory(1L)
                .orElseThrow(() -> new NotFoundQuestionId(1L));
        //Then
        assertThat(getQuestion.getTitle()).isEqualTo(TestEnum.QUESTION_TITLE.getMessage());
        assertThat(getQuestion.getDescription()).isEqualTo(TestEnum.QUESTION_DESCRIPTION.getMessage());
    }

}