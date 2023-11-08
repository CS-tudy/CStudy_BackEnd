package com.cstudy.moduleapi.application.question.Impl;

import com.cstudy.moduleapi.application.question.MemberQuestionService;
import com.cstudy.moduleapi.dto.question.QuestionAnswerDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.error.question.existByMemberQuestionDataException;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.question.MemberQuestionRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class MemberQuestionServiceImpl implements MemberQuestionService {

    private final static String RANKING_KEY = "MemberRank";

    private final MemberQuestionRepository memberQuestionRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final StringRedisTemplate redisTemplate;

    public MemberQuestionServiceImpl(
            MemberQuestionRepository memberQuestionRepository,
            MemberRepository memberRepository,
            QuestionRepository questionRepository,
            StringRedisTemplate redisTemplate
    ) {
        this.memberQuestionRepository = memberQuestionRepository;
        this.memberRepository = memberRepository;
        this.questionRepository = questionRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 만약 사용자가 정확한 정답을 선택을 하였을 때 MEMBERQUESTION TABLE에 기존의 데이터가 있는지 확인한다.
     */
    @Override
    @Transactional
    public void findMemberAndMemberQuestionSuccess(Long memberId, Long questionId, ChoiceAnswerRequestDto choiceAnswerRequestDto) {

        findByQuestionAboutMemberIdAndQuestionIdSuccess(memberId, questionId);
        findByQuestionAboutMemberIdAndQuestionIdFail(memberId, questionId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberId(memberId));

        Question question = questionRepository.findById(questionId)
                .orElseThrow();


        if (memberQuestionRepository.existsByMemberAndQuestionAndSuccess(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber())) {
            throw new existByMemberQuestionDataException(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber());
        }

        member.addRankingPoint(choiceAnswerRequestDto);
        modifyScoreForMember(member,choiceAnswerRequestDto, true);


        memberQuestionRepository.save(MemberQuestion.builder()
                .member(member)
                .question(question)
                .success(choiceAnswerRequestDto.getChoiceNumber())
                .solveTime(choiceAnswerRequestDto.getTime())
                .build());
    }



    /**
     * 만약 사용자가 정확한 오답을 선택을 하였을 때 MEMBERQUESTION TABLE에 기존의 데이터가 있는지 확인한다.
     */
    @Override
    @Transactional
    public void findMemberAndMemberQuestionFail(Long memberId, Long questionId, ChoiceAnswerRequestDto choiceAnswerRequestDto) {

        findByQuestionAboutMemberIdAndQuestionIdSuccess(memberId, questionId);
        findByQuestionAboutMemberIdAndQuestionIdFail(memberId, questionId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberId(memberId));

        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        if (memberQuestionRepository.existsByMemberAndQuestionAndFail(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber())) {
            throw new existByMemberQuestionDataException(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber());
        }

        member.minusRankingPoint(member.getRankingPoint());
        modifyScoreForMember(member, choiceAnswerRequestDto, false);
        memberQuestionRepository.save(MemberQuestion.builder()
                .member(member)
                .question(question)
                .fail(choiceAnswerRequestDto.getChoiceNumber())
                .solveTime(choiceAnswerRequestDto.getTime())
                .build());
    }


    /**
     *  MEMBERQUESTION 테이블에 기존의 정보가 있다면 삭제한다. 그리고 새로운 데이터를 삽입한다.
     */
    @Override
    @Transactional
    public void findByQuestionAboutMemberIdAndQuestionIdSuccess(Long memberId, Long questionId) {
        long count = memberQuestionRepository.countByMemberIdAndQuestionIdAndSuccessZero(memberId, questionId);
        if (count != 0) {
            Optional<MemberQuestion> questionOptional = memberQuestionRepository
                    .findByQuestionAboutMemberIdAndQuestionId(memberId, questionId);
            questionOptional.ifPresent(question -> memberQuestionRepository.deleteById(question.getId()));
            questionOptional.orElseThrow(() -> new RuntimeException("MemberQuestion not found"));
        }
    }

    /**
     * MEMBERQUESTION 테이블에 기존의 정보가 있다면 삭제한다. 그리고 새로운 데이터를 삽입한다.
     */
    @Override
    @Transactional
    public void findByQuestionAboutMemberIdAndQuestionIdFail(Long memberId, Long questionId) {
        long count = memberQuestionRepository.countByMemberIdAndQuestionIdAndFailZero(memberId, questionId);
        if (count != 0) {
            Optional<MemberQuestion> questionOptional = memberQuestionRepository.findByQuestionAboutMemberIdAndQuestionId(memberId, questionId);
            questionOptional.ifPresent(question -> memberQuestionRepository.deleteById(question.getId()));
            questionOptional.orElseThrow(() -> new RuntimeException("MemberQuestion not found"));
        }
    }

    /**
     * 4지선다의 문제에서 정답의 유무를 판단한다.
     */
    @Override
    public QuestionAnswerDto isCorrectAnswer(Long memberId, Long questionId, ChoiceAnswerRequestDto requestDto) {
        boolean answer = memberQuestionRepository.existsByMemberAndQuestionAndSuccess(memberId, questionId, requestDto.getChoiceNumber());
        return QuestionAnswerDto.builder()
                .answer(answer)
                .build();
    }

    private void modifyScoreForMember(Member member, ChoiceAnswerRequestDto choiceAnswerRequestDto, boolean answer) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Double currentScore = zSetOps.score(RANKING_KEY, member.getName());

        double newScore;
        if (answer) {
            newScore = currentScore + 3L + (1 - (choiceAnswerRequestDto.getTime() / 1000.0));
        } else {
            newScore = currentScore - 2;
        }
        zSetOps.add(RANKING_KEY, member.getName(), newScore);
    }
}
