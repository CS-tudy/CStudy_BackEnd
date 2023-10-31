package com.cstudy.moduleapi.application.question;


import com.cstudy.moduleapi.dto.question.*;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.dto.QuestionSearchCondition;
import com.cstudy.moduleapi.dto.question.QuestionResponseDto;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService {
    void createQuestionChoice(CreateQuestionAndCategoryRequestDto createChoicesAboutQuestionDto);

    void bulkCreateQuestionChoice(List<CreateQuestionAndCategoryRequestDto> requestDtos);

    QuestionResponseDto findQuestionWithChoiceAndCategory(Long questionId);

    void choiceQuestion(LoginUserDto loginUserDto, Long questionId, ChoiceAnswerRequestDto choiceNumber);

    Page<QuestionPageWithCategoryAndTitle> questionPageWithCategory(QuestionSearchCondition searchCondition, int page, int size, LoginUserDto loginUserDto);
}
