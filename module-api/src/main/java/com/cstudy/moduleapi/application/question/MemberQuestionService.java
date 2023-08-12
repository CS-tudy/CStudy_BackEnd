package com.cstudy.moduleapi.application.question;


import com.cstudy.moduleapi.dto.question.QuestionAnswerDto;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;

public interface MemberQuestionService {
    void findMemberAndMemberQuestionSuccess(Long memberId, Long questionId, ChoiceAnswerRequestDto choiceAnswerRequestDto);
    void findMemberAndMemberQuestionFail(Long memberId, Long questionId, ChoiceAnswerRequestDto choiceAnswerRequestDto);
    void findByQuestionAboutMemberIdAndQuestionIdSuccess(Long memberId, Long questionId);
    void findByQuestionAboutMemberIdAndQuestionIdFail(Long memberId, Long questionId);

    QuestionAnswerDto isCorrectAnswer(Long memberId, Long questionId, ChoiceAnswerRequestDto requestDto);
}
