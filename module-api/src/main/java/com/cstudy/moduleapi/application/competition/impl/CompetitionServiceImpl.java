package com.cstudy.moduleapi.application.competition.impl;

import com.cstudy.moduleapi.application.alarm.AlarmService;
import com.cstudy.moduleapi.application.competition.CompetitionService;
import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.dto.competition.*;
import com.cstudy.moduleapi.dto.workbook.WorkbookQuestionRequestDto;
import com.cstudy.modulecommon.domain.alarm.AlarmArgs;
import com.cstudy.modulecommon.domain.alarm.AlarmType;
import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.domain.workbook.Workbook;
import com.cstudy.modulecommon.dto.CompetitionQuestionDto;
import com.cstudy.modulecommon.error.competition.CompetitionStartException;
import com.cstudy.modulecommon.error.competition.NotFoundCompetitionId;
import com.cstudy.modulecommon.repository.competition.CompetitionRepository;
import com.cstudy.modulecommon.repository.competition.MemberCompetitionRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    private final static Long ADMIN_ID = 1L;

    private final CompetitionRepository competitionRepository;
    private final WorkbookRepository workbookRepository;
    private final QuestionRepository questionRepository;
    private final MemberCompetitionRepository memberCompetitionRepository;
    private final MemberCompetitionService memberCompetitionService;
    private final WorkbookService workbookService;
    private final AlarmService alarmService;
    private final MemberRepository memberRepository;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository, WorkbookRepository workbookRepository, QuestionRepository questionRepository, MemberCompetitionRepository memberCompetitionRepository, MemberCompetitionService memberCompetitionService, WorkbookService workbookService, AlarmService alarmService, MemberRepository memberRepository) {
        this.competitionRepository = competitionRepository;
        this.workbookRepository = workbookRepository;
        this.questionRepository = questionRepository;
        this.memberCompetitionRepository = memberCompetitionRepository;
        this.memberCompetitionService = memberCompetitionService;
        this.workbookService = workbookService;
        this.alarmService = alarmService;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public Long createCompetition(CreateCompetitionRequestDto createCompetitionRequestDto) {

        if(createCompetitionRequestDto.getCompetitionStart()
                .isAfter(createCompetitionRequestDto.getCompetitionEnd())){
            throw new CompetitionStartException();
        }

        Workbook workbook = Workbook.builder()
                .title(createCompetitionRequestDto.getCompetitionTitle())
                .description(createCompetitionRequestDto.getCompetitionTitle())
                .build();

        workbookRepository.save(workbook);

        Competition competition = Competition.builder()
                .competitionTitle(createCompetitionRequestDto.getCompetitionTitle())
                .participants(createCompetitionRequestDto.getParticipants())
                .competitionStart(createCompetitionRequestDto.getCompetitionStart())
                .competitionEnd(createCompetitionRequestDto.getCompetitionEnd())
                .workbook(workbook)
                .build();

        competitionRepository.save(competition);
        workbook.setCompetition(competition);

        for (Member member : memberRepository.findAll()) {
            Long allMemberId = member.getId();
            alarmService.send(AlarmType.CREATE_COMPETITION, new AlarmArgs(allMemberId, allMemberId, competition.getCompetitionTitle()), allMemberId);
        }

        return competition.getId();
    }

    /**
     * 대회 정보.
     *
     * @param competitionId competition id
     */
    @Override
    @Transactional(readOnly = true)
    public CompetitionResponseDto getCompetition(Long competitionId) {

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

        int participants = memberCompetitionService.getJoinMemberCount(competitionId);

        return CompetitionResponseDto.of(competition, participants);
    }

    /**
     * 대회 리스트.
     *
     * @param finish   끝난 대회이면 true, 진행 전 대회이면 false
     * @param pageable pageable
     * @param now      현재 시간
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompetitionListResponseDto> getCompetitionList(boolean finish, Pageable pageable, LocalDateTime now) {

        Page<Competition> competitions = null;
        if (finish) {
            competitions = competitionRepository.findByCompetitionEndBefore(now, pageable);
        } else {
            competitions = competitionRepository.findByCompetitionEndAfter(now, pageable);
        }

        return competitions.map(CompetitionListResponseDto::of);
    }

    /**
     * 대회 랭킹.
     *
     * @param competitionId competition id
     * @param pageable      pageable
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompetitionRankingResponseDto> getCompetitionRanking(Long competitionId, Pageable pageable) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

        return memberCompetitionRepository.findByCompetition(competition, pageable)
                .map(CompetitionRankingResponseDto::of);
    }

    @Override
    public List<CompetitionQuestionDto> getCompetitionQuestion(Long competitionId, LoginUserDto loginUserDto) {

        if (!loginUserDto.getRoles().contains(RoleEnum.ADMIN.getRoleName())) {
            Competition competition = competitionRepository.findById(competitionId)
                    .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

            checkTimeAfter(competition.getCompetitionStart());
        }
        return questionRepository.findQuestionWithCompetitionById(competitionId);
    }

    @Override
    @Transactional
    public void addCompetitionQuestion(CompetitionQuestionRequestDto requestDto) {
        Competition competition = competitionRepository.findById(requestDto.getCompetitionId())
                .orElseThrow(() -> new NotFoundCompetitionId(requestDto.getCompetitionId()));

        Long workbookId = competition.getWorkbook().getId();
        WorkbookQuestionRequestDto questionDto = WorkbookQuestionRequestDto.builder()
                .questionIds(requestDto.getQuestionIds())
                .workbookId(workbookId).build();
        workbookService.addQuestion(questionDto);
    }

    @Override
    @Transactional
    public void deleteCompetitionQuestion(CompetitionQuestionRequestDto requestDto) {
        Competition competition = competitionRepository.findById(requestDto.getCompetitionId())
                .orElseThrow(() -> new NotFoundCompetitionId(requestDto.getCompetitionId()));

        Long workbookId = competition.getWorkbook().getId();
        WorkbookQuestionRequestDto questionDto = WorkbookQuestionRequestDto.builder()
                .questionIds(requestDto.getQuestionIds())
                .workbookId(workbookId).build();
        workbookService.deleteQuestion(questionDto);
    }

    @Override
    @Transactional
    public void checkCompetitionTime(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(CompetitionStartException::new);
        checkTimeAfter(competition.getCompetitionStart());
        checkTimeBefore(competition.getCompetitionEnd());
    }

    private void checkTimeBefore(LocalDateTime time) {
        if (LocalDateTime.now().isAfter(time)) {
            throw new CompetitionStartException();
        }
    }

    private void checkTimeAfter(LocalDateTime time) {
        if (LocalDateTime.now().isBefore(time)) {
            throw new CompetitionStartException();
        }
    }

}
