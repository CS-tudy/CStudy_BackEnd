package com.cstudy.moduleapi.application.competition.impl;

import com.cstudy.moduleapi.application.competition.CompetitionScoreService;
import com.cstudy.moduleapi.dto.competition.CompetitionScoreRequestDto;
import com.cstudy.moduleapi.dto.competition.CompetitionScoreResponseDto;
import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.competition.CompetitionScore;
import com.cstudy.modulecommon.domain.competition.MemberCompetition;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.error.choice.NotFoundChoiceWithQuestionAndNumber;
import com.cstudy.modulecommon.error.competition.NotFoundMemberCompetition;
import com.cstudy.modulecommon.error.question.NotFoundQuestionId;
import com.cstudy.modulecommon.repository.choice.ChoiceRepository;
import com.cstudy.modulecommon.repository.competition.CompetitionScoreRepository;
import com.cstudy.modulecommon.repository.competition.MemberCompetitionRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CompetitionScoreServiceImpl implements CompetitionScoreService {

    private final CompetitionScoreRepository competitionScoreRepository;
    private final MemberCompetitionRepository memberCompetitionRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;

    public CompetitionScoreServiceImpl(
            CompetitionScoreRepository competitionScoreRepository,
            MemberCompetitionRepository memberCompetitionRepository,
            QuestionRepository questionRepository,
            ChoiceRepository choiceRepository
    ) {
        this.competitionScoreRepository = competitionScoreRepository;
        this.memberCompetitionRepository = memberCompetitionRepository;
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
    }

    /**
     * 일반 회원 대회에서 제출한 답안을 채점한다.
     */
    @Override
    @Transactional
    public void scoring(CompetitionScoreRequestDto requestDto, LoginUserDto userDto) {
        log.info("경기 아이디 : {}", requestDto.getCompetitionId());
        log.info("끝나는 시간 : {}", requestDto.getEndTime());
        MemberCompetition memberCompetition = memberCompetitionRepository
                .findByMemberIdAndCompetitionId(userDto.getMemberId(), requestDto.getCompetitionId())
                .orElseThrow(NotFoundMemberCompetition::new);

        int score = 0;
        memberCompetition.setEndTime(requestDto.getEndTime());

        for (CompetitionScoreRequestDto.CompetitionAnswerRequestDto questionDto : requestDto.getQuestions()) {
            Question question = questionRepository.findById(questionDto.getQuestionId())
                    .orElseThrow(() -> new NotFoundQuestionId(questionDto.getQuestionId()));

            CompetitionScore competitionScore = CompetitionScore.builder()
                    .memberCompetition(memberCompetition)
                    .question(question)
                    .choiceNumber(questionDto.getChoiceNumber())
                    .build();

            if (questionDto.getChoiceNumber() != null) {
                boolean correct = isCorrectAnswer(question, questionDto.getChoiceNumber());
                log.info("정답 유무 : {}", correct);
                if (correct) {
                    competitionScore.setSuccess(true);
                    score++;
                }
            }

            competitionScoreRepository.save(competitionScore);
            memberCompetition.addCompetitionScore(competitionScore);
        }

        memberCompetition.setScore(score);
    }

    @Override
    @Transactional(readOnly = true)
    public CompetitionScoreResponseDto getAnswer(Long memberId, Long competitionId) {
        if (!memberCompetitionRepository.existsByMemberIdAndCompetitionId(memberId, competitionId)) {
            log.error("회원은 지금 해당 경기에 참여하고 있습니다.");
            throw new NotFoundMemberCompetition();
        }

        log.info("memberId : {}", memberId);
        log.info("competitionId : {}", competitionId);
        List<CompetitionScore> memberScores = competitionScoreRepository
                .findByCompetitionIdAndMemberId(memberId, competitionId);

        List<CompetitionScoreResponseDto.ScoreDetail> answer = new ArrayList<>();

        int score = 0;
        for (CompetitionScore competitionScore : memberScores) {
            answer.add(CompetitionScoreResponseDto.ScoreDetail.builder()
                    .questionId(competitionScore.getQuestion().getId())
                    .choiceNumber(competitionScore.getChoiceNumber())
                    .correct(competitionScore.isSuccess())
                    .build()
            );

            if (competitionScore.isSuccess()) {
                score++;
                log.info("성공");
            }
        }
        return CompetitionScoreResponseDto.builder()
                .score(score)
                .total(memberScores.size())
                .details(answer)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public int getScore(Long memberId, Long competitionId) {
        log.info("memberId : {}", memberId);
        log.info("competitionId : {}", competitionId);
        MemberCompetition memberCompetition = memberCompetitionRepository.findByMemberIdAndCompetitionId(memberId, competitionId)
                .orElseThrow(NotFoundMemberCompetition::new);
        return memberCompetition.getScore();
    }

    @Transactional(readOnly = true)
    public boolean isCorrectAnswer(Question question, int number) {
        Choice choice = choiceRepository.findByQuestionAndNumber(question, number)
                .orElseThrow(() -> new NotFoundChoiceWithQuestionAndNumber(question.getId(), number));
        log.info("choice 성공유무 : {}", choice.isAnswer());
        return choice.isAnswer();
    }
}
