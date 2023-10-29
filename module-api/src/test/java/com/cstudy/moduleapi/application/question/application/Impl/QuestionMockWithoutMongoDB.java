package com.cstudy.moduleapi.application.question.application.Impl;

import com.cstudy.moduleapi.application.question.Impl.MemberQuestionServiceImpl;
import com.cstudy.moduleapi.application.question.MemberQuestionService;
import com.cstudy.moduleapi.dto.question.QuestionAnswerDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.question.MemberQuestionRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
public class QuestionMockWithoutMongoDB {

    @Mock
    private MemberQuestionRepository memberQuestionRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private  StringRedisTemplate redisTemplate;

    private MemberQuestionService memberQuestionService;
    
    @BeforeEach
    void setUp(){
        openMocks(this);

        memberQuestionService = new MemberQuestionServiceImpl(memberQuestionRepository, memberRepository, questionRepository, redisTemplate);
    }


    @Test
    public void testFindMemberAndMemberQuestionSuccess() {
        Long memberId = 1L;
        Long questionId = 1L;
        ChoiceAnswerRequestDto choiceAnswerRequestDto = new ChoiceAnswerRequestDto(1, 10L);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(new Question()));
        when(memberQuestionRepository.existsByMemberAndQuestionAndSuccess(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber())).thenReturn(false);

        assertDoesNotThrow(() -> memberQuestionService.findMemberAndMemberQuestionSuccess(memberId, questionId, choiceAnswerRequestDto));
        verify(memberQuestionRepository, times(1)).save(any());
    }

    @Test
    public void testFindMemberAndMemberQuestionFail() {
        Long memberId = 1L;
        Long questionId = 1L;
        ChoiceAnswerRequestDto choiceAnswerRequestDto = new ChoiceAnswerRequestDto(1, 10L);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(new Question()));
        when(memberQuestionRepository.existsByMemberAndQuestionAndFail(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber())).thenReturn(false);

        assertDoesNotThrow(() -> memberQuestionService.findMemberAndMemberQuestionFail(memberId, questionId, choiceAnswerRequestDto));
        verify(memberQuestionRepository, times(1)).save(any());
    }

    @Test
    public void testFindByQuestionAboutMemberIdAndQuestionIdSuccess() {
        Long memberId = 1L;
        Long questionId = 1L;

        when(memberQuestionRepository.countByMemberIdAndQuestionIdAndSuccessZero(memberId, questionId)).thenReturn(1L);
        when(memberQuestionRepository.findByQuestionAboutMemberIdAndQuestionId(memberId, questionId)).thenReturn(Optional.of(new MemberQuestion()));

        assertDoesNotThrow(() -> memberQuestionService.findByQuestionAboutMemberIdAndQuestionIdSuccess(memberId, questionId));
        verify(memberQuestionRepository, times(1)).deleteById(any());
    }

    @Test
    public void testFindByQuestionAboutMemberIdAndQuestionIdFail() {
        Long memberId = 1L;
        Long questionId = 1L;

        when(memberQuestionRepository.countByMemberIdAndQuestionIdAndFailZero(memberId, questionId)).thenReturn(1L);
        when(memberQuestionRepository.findByQuestionAboutMemberIdAndQuestionId(memberId, questionId)).thenReturn(Optional.of(new MemberQuestion()));

        assertDoesNotThrow(() -> memberQuestionService.findByQuestionAboutMemberIdAndQuestionIdFail(memberId, questionId));
        verify(memberQuestionRepository, times(1)).deleteById(any());
    }

    @Test
    public void testIsCorrectAnswer() {
        Long memberId = 1L;
        Long questionId = 1L;
        ChoiceAnswerRequestDto requestDto = new ChoiceAnswerRequestDto(1, 10L);

        when(memberQuestionRepository.existsByMemberAndQuestionAndSuccess(memberId, questionId, requestDto.getChoiceNumber())).thenReturn(true);

        QuestionAnswerDto answerDto = memberQuestionService.isCorrectAnswer(memberId, questionId, requestDto);
        assertNotNull(answerDto);
        assertTrue(answerDto.isAnswer());
    }
}
