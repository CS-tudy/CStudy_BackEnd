package com.cstudy.moduleapi.application.competition.impl;

import com.cstudy.moduleapi.application.alarm.AlarmService;
import com.cstudy.moduleapi.application.competition.CompetitionService;
import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.moduleapi.application.member.impl.MemberCacheRepository;
import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.dto.competition.*;
import com.cstudy.moduleapi.dto.workbook.WorkbookQuestionRequestDto;
import com.cstudy.modulecommon.domain.alarm.AlarmArgs;
import com.cstudy.modulecommon.domain.alarm.AlarmType;
import com.cstudy.modulecommon.domain.competition.Competition;
import com.cstudy.modulecommon.domain.competition.CompetitionJoinStatus;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.domain.workbook.Workbook;
import com.cstudy.modulecommon.dto.ChoiceQuestionResponseDto;
import com.cstudy.modulecommon.dto.CompetitionQuestionDto;
import com.cstudy.modulecommon.error.competition.CompetitionStartException;
import com.cstudy.modulecommon.error.competition.NotFoundCompetitionId;
import com.cstudy.modulecommon.repository.competition.CompetitionRepository;
import com.cstudy.modulecommon.repository.competition.MemberCompetitionRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CompetitionServiceImpl implements CompetitionService {

    private final static Long ADMIN_ID = 1L;

    private final CompetitionRepository competitionRepository;
    private final WorkbookRepository workbookRepository;
    private final MemberCompetitionRepository memberCompetitionRepository;
    private final MemberCompetitionService memberCompetitionService;
    private final WorkbookService workbookService;
    private final AlarmService alarmService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    public CompetitionServiceImpl(
            CompetitionRepository competitionRepository,
            WorkbookRepository workbookRepository,
            MemberCompetitionRepository memberCompetitionRepository,
            MemberCompetitionService memberCompetitionService,
            WorkbookService workbookService,
            AlarmService alarmService,
            MemberRepository memberRepository,
            ObjectMapper objectMapper
    ) {
        this.competitionRepository = competitionRepository;
        this.workbookRepository = workbookRepository;
        this.memberCompetitionRepository = memberCompetitionRepository;
        this.memberCompetitionService = memberCompetitionService;
        this.workbookService = workbookService;
        this.alarmService = alarmService;
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * 회원의 경기 문제를 조회한다.

     */
    @Override
    @Transactional(readOnly = true)
    public List<CompetitionQuestionDto> getCompetitionQuestion(Long competitionId, LoginUserDto loginUserDto) throws JsonProcessingException {
        log.info("competitionId : {}", competitionId);

        if (!userIsAdmin(loginUserDto)) {
            checkCompetitionStart(competitionId);
        }

        List<Object[]> questionsWithChoices = competitionRepository.findQuestionsWithChoices(competitionId);

        List<CompetitionQuestionDto> competitionQuestions = new ArrayList<>();

        for (Object[] row : questionsWithChoices) {
            CompetitionQuestionDto competitionQuestionDto = mapRowToCompetitionQuestionDto(row);
            competitionQuestions.add(competitionQuestionDto);
        }

        return competitionQuestions;
    }

    private boolean userIsAdmin(LoginUserDto loginUserDto) {
        return loginUserDto.getRoles().contains(RoleEnum.ADMIN.getRoleName());
    }

    private void checkCompetitionStart(Long competitionId) throws NotFoundCompetitionId {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

        checkTimeAfter(competition.getCompetitionStart());
    }

    private CompetitionQuestionDto mapRowToCompetitionQuestionDto(Object[] row) throws JsonProcessingException {
        BigInteger questionIdBigInteger = (BigInteger) row[0];
        Long questionId = questionIdBigInteger.longValue();// 문제 아이디
        String title = (String) row[1];// 문제 제목
        String description = (String) row[2];// 문제 설명
        log.info("문제 아이디 >> {}", questionId);
        log.info("문제 제목 >> {}", title);
        log.info("문제 설명 >> {}", description);

        List<Integer> choiceNumbers = objectMapper.readValue((String) row[3], new TypeReference<>() {});
        List<String> choiceContents = objectMapper.readValue((String) row[4], new TypeReference<>() {});

        List<ChoiceQuestionResponseDto> choices = mapChoices(choiceNumbers, choiceContents);

        return CompetitionQuestionDto.builder()
                .questionId(questionId)
                .questionTitle(title)
                .description(description)
                .choices(choices)
                .build();
    }

    private List<ChoiceQuestionResponseDto> mapChoices(List<Integer> choiceNumbers, List<String> choiceContents) {
        List<ChoiceQuestionResponseDto> choices = new ArrayList<>();

        for (int i = 0; i < choiceNumbers.size(); i++) {
            int number = choiceNumbers.get(i);
            String content = choiceContents.get(i);
            choices.add(new ChoiceQuestionResponseDto(number, content));
        }

        return choices;
    }




    /**
     * 경기를 생성을 한다.
     * 시간에 따라서 생성의 조건을 처리한다.
     * 이후 문제집을 만들어서 경기에 사용하는 문제를 추가한다.
     * 이때 경기에서 시작과 끝나는 TIME을 지정한다.
     */
    @Override
    @Transactional
    public Long createCompetition(CreateCompetitionRequestDto createCompetitionRequestDto) {

        if (createCompetitionRequestDto.getCompetitionStart().isAfter(createCompetitionRequestDto.getCompetitionEnd())) {
            log.error("CompetitionStartException");
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

        log.info("모든 회원에게 알림 보내기");
        for (Member member : memberRepository.findAll()) {
            Long allMemberId = member.getId();
            alarmService.send(AlarmType.CREATE_COMPETITION, new AlarmArgs(allMemberId, allMemberId, competition.getCompetitionTitle()), allMemberId);
        }

        return competition.getId();
    }

    /**
     * 경기 ID를 기반으로 대회의 정보를 조회한다.
     * PATH를 통해서 경기의 아이디를 받는다. 이때 고민이 되는 부분이 ID를 처리를 해도 되는지 고민이다.
     * 이 부분에 대해서 UUID를 처리를 고려해보자.
     */
    @Override
    @Transactional(readOnly = true)
    public CompetitionResponseDto getCompetition(Long competitionId) {
        log.info("competitionId : {}", competitionId);
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

        int participants = memberCompetitionService.getJoinMemberCount(competitionId);

        return CompetitionResponseDto.of(competition, participants);
    }

    @Override
    @Transactional
    public CompetitionJoinStatus isJoined(LoginUserDto loginUserDto, Long competitionId) {
        log.info("competitionId : {}", competitionId);
        boolean existsByMemberIdAndCompetitionId = memberRepository.existsByMemberIdAndCompetitionId(loginUserDto.getMemberId(), competitionId);

        if (existsByMemberIdAndCompetitionId) {
            log.info("CompetitionJoinStatus.JOIN");
            return CompetitionJoinStatus.JOIN;
        } else {
            log.info("CompetitionJoinStatus.WAITING");
            return CompetitionJoinStatus.WAITING;
        }

    }

    /**
     * 대회 리스트 조회
     * 끝난 대회이면 TRUE를 반환하고 진행 전 대회이면 FALSE를 반환한다.
     * 이때 PAGEABLE 방식으로 페이징 처리를 한다.
     * TIME을 파라미터로 넣어서 테스트의 정합성을 보장하게 변경을 하였다.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompetitionListResponseDto> getCompetitionList(boolean finish, Pageable pageable, LocalDateTime now) {
        log.info("경기 끝나는 유무 : {}", finish);

        Page<Competition> competitions = null;
        if (finish) {
            log.info("경기 finish");
            competitions = competitionRepository.findByCompetitionEndBefore(now, pageable);
        } else {
            log.info("경기 not finish");
            competitions = competitionRepository.findByCompetitionEndAfter(now, pageable);
        }

        return competitions.map(CompetitionListResponseDto::of);
    }

    /**
     * 대회 랭킹.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompetitionRankingResponseDto> getCompetitionRanking(Long competitionId, Pageable pageable) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

        return memberCompetitionRepository.findByCompetition(competition, pageable)
                .map(CompetitionRankingResponseDto::of);
    }



    /**
     * 경기에 대한 문제를 추가할 수 있다.
     * 이때 문제를 추가하여 경기의 문제를 선정할 수 있다.
     * 이때 관리자만 이 로직을 사용할 수 있다.
     * Controller에서 처리
     */
    @Override
    @Transactional
    public void addCompetitionQuestion(CompetitionQuestionRequestDto requestDto) {
        log.info("경기 아이디 >>> {}", requestDto.getCompetitionId());
        Competition competition = competitionRepository.findById(requestDto.getCompetitionId())
                .orElseThrow(() -> new NotFoundCompetitionId(requestDto.getCompetitionId()));

        Long workbookId = competition.getWorkbook().getId();
        WorkbookQuestionRequestDto questionDto = WorkbookQuestionRequestDto.builder()
                .questionIds(requestDto.getQuestionIds())
                .workbookId(workbookId).build();
        workbookService.addQuestion(questionDto);
    }

    /**
     * 경기의 문제를 삭제한다.
     * private Long competitionId;
     * private List<QuestionIdRequestDto> questionIds;
     * 경기의 아이디와 LIST의 문제 아이디를 통하여 여러개의 문제를 삭제한다.
     */
    @Override
    @Transactional
    public void deleteCompetitionQuestion(CompetitionQuestionRequestDto requestDto) {
        log.info("경기 아이디 >>> {}", requestDto.getCompetitionId());
        Competition competition = competitionRepository.findById(requestDto.getCompetitionId())
                .orElseThrow(() -> new NotFoundCompetitionId(requestDto.getCompetitionId()));

        Long workbookId = competition.getWorkbook().getId();
        log.info("문제집 아이디 >>> {}", workbookId);
        WorkbookQuestionRequestDto questionDto = WorkbookQuestionRequestDto.builder()
                .questionIds(requestDto.getQuestionIds())
                .workbookId(workbookId).build();
        workbookService.deleteQuestion(questionDto);
    }

    /**
     * 경기 아이디를 받아서 시간을 조회한다.
     */
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
