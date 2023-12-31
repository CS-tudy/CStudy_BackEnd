package com.cstudy.moduleapi.application.question.Impl;

import com.cstudy.moduleapi.application.member.MemberLoadComponent;
import com.cstudy.moduleapi.application.member.impl.MemberCacheRepository;
import com.cstudy.moduleapi.application.question.MemberQuestionService;
import com.cstudy.moduleapi.dto.question.QuestionAnswerDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.error.question.NotFoundQuestionId;
import com.cstudy.modulecommon.error.question.existByMemberQuestionDataException;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.question.MemberQuestionRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
public class MemberQuestionServiceImpl implements MemberQuestionService {

    private final static String RANKING_KEY = "MemberRank";

    private final MemberQuestionRepository memberQuestionRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final StringRedisTemplate redisTemplate;
    private final MemberLoadComponent memberLoadComponent;
    private final MemberCacheRepository memberCacheRepository;

    public MemberQuestionServiceImpl(
            MemberQuestionRepository memberQuestionRepository,
            MemberRepository memberRepository,
            QuestionRepository questionRepository,
            StringRedisTemplate redisTemplate,
            MemberLoadComponent memberLoadComponent,
            MemberCacheRepository memberCacheRepository
    ) {
        this.memberQuestionRepository = memberQuestionRepository;
        this.memberRepository = memberRepository;
        this.questionRepository = questionRepository;
        this.redisTemplate = redisTemplate;
        this.memberLoadComponent = memberLoadComponent;
        this.memberCacheRepository = memberCacheRepository;
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
                .orElseThrow(() -> new NotFoundQuestionId(questionId));


        if (memberQuestionRepository.existsByMemberAndQuestionAndSuccess(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber())) {
            log.error("이미 관련 데이터가 있습니다. : excection : {}", existByMemberQuestionDataException.class);
            throw new existByMemberQuestionDataException(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber());
        }

        member.addRankingPoint(choiceAnswerRequestDto);
//        modifyScoreForMember(member, choiceAnswerRequestDto, true);


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
                .orElseThrow(() -> new NotFoundQuestionId(questionId));

        if (memberQuestionRepository.existsByMemberAndQuestionAndFail(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber())) {
            log.error("이미 관련 데이터가 있습니다. : excection : {}", existByMemberQuestionDataException.class);
            throw new existByMemberQuestionDataException(memberId, questionId, choiceAnswerRequestDto.getChoiceNumber());
        }

        member.minusRankingPoint(member.getRankingPoint());
//        modifyScoreForMember(member, choiceAnswerRequestDto, false);
        memberQuestionRepository.save(MemberQuestion.builder()
                .member(member)
                .question(question)
                .fail(choiceAnswerRequestDto.getChoiceNumber())
                .solveTime(choiceAnswerRequestDto.getTime())
                .build());
    }


    /**
     * MEMBERQUESTION 테이블에 기존의 정보가 있다면 삭제한다. 그리고 새로운 데이터를 삽입한다.
     */
    @Override
    @Transactional
    public void findByQuestionAboutMemberIdAndQuestionIdSuccess(Long memberId, Long questionId) {
        long count = memberQuestionRepository.countByMemberIdAndQuestionIdAndSuccessZero(memberId, questionId);
        log.info("기존에 있는지 성공 count : {} / memberId : {}, questionId : {}", count, memberId, questionId);
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
        log.info("기존에 있는지 실패 count: {} / memberId : {}, questionId : {}", count, memberId, questionId);
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
    @Transactional
    public QuestionAnswerDto isCorrectAnswer(Long memberId, Long questionId, ChoiceAnswerRequestDto requestDto) {
        boolean answer = memberQuestionRepository.existsByMemberAndQuestionAndSuccess(memberId, questionId, requestDto.getChoiceNumber());
        log.info("문제의 성공 실패 여부 : {}", answer);
        return QuestionAnswerDto.builder()
                .answer(answer)
                .build();
    }

    private void modifyScoreForMember(Member member, ChoiceAnswerRequestDto choiceAnswerRequestDto, boolean answer) {
        log.info("member email : {}", member.getEmail());
//        Member redisCacheMember = memberLoadComponent.loadMemberByEmail(member.getEmail());
//        memberCacheRepository.modifyMemberRankingPoint(redisCacheMember, choiceAnswerRequestDto, answer);
    }
}
