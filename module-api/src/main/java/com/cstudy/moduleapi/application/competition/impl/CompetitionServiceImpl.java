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
import com.cstudy.modulecommon.dto.ChoiceQuestionResponseDto;
import com.cstudy.modulecommon.dto.CompetitionQuestionDto;
import com.cstudy.modulecommon.error.competition.CompetitionStartException;
import com.cstudy.modulecommon.error.competition.NotFoundCompetitionId;
import com.cstudy.modulecommon.repository.competition.CompetitionRepository;
import com.cstudy.modulecommon.repository.competition.MemberCompetitionRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
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

    /**
     *  경기를 생성을 한다.
     *  시간에 따라서 생성의 조건을 처리한다.
     *  이후 문제집을 만들어서 경기에 사용하는 문제를 추가한다.
     *  이때 경기에서 시작과 끝나는 TIME을 지정한다.
     */
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
     * 경기 ID를 기반으로 대회의 정보를 조회한다.
     * PATH를 통해서 경기의 아이디를 받는다. 이때 고민이 되는 부분이 ID를 처리를 해도 되는지 고민이다.
     * 이 부분에 대해서 UUID를 처리를 고려해보자.
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
     * 대회 리스트 조회
     * 끝난 대회이면 TRUE를 반환하고 진행 전 대회이면 FALSE를 반환한다.
     * 이때 PAGEABLE 방식으로 페이징 처리를 한다.
     * TIME을 파라미터로 넣어서 테스트의 정합성을 보장하게 변경을 하였다.
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
     * 회원의 경기 문제를 조회한다.
     *
     *  여기서 문제의 보기, 내용을 List 형태로 가져옴
     *  따로 가져와서 Dto에 Mapping 보다는 List Object[] 로 가져와서 Mapping을 하여 한방 쿼리로 만듬
     *
     * 1	자바의 접근 제어자 중에서 부모 클래스에 대해서는 public 멤버처럼 취급되며, 외부에서는 private 멤버처럼 취급됩니다.	1,2,3,4	protected,private,public,default
     * 2	객체지향의 특징 중에서 객체 내부의 접근을 제어하며 객체 사이의 결합도를 낮출 수 있고, 응집도가 증가하여 유지보수에 좋은 특징은?	1,2,3,4	캡슐화,상속,추상화,다형성
     * 3	finalize의 설명을 적절하게 설명한 보기를 찾으시오.	1,2,3,4	변수, 메서드 클래스가 변경 불가능 하도록 만든다.,참조 변수가 힙 내의 다른 객체를 가리키도록 변경할 수 없다.,가비지 컬렉터가 더 이상의 참조가 존재하지 않는 객체를 메모리에서 삭제하겠다고 결정하는 순간 호출된다.,try catch 블록 뒤에서 항상 실행될 코드 블록을 정의하기 위해 사용한다.
     */
    @Override
    public List<CompetitionQuestionDto> getCompetitionQuestion(Long competitionId, LoginUserDto loginUserDto) {
        if (!loginUserDto.getRoles().contains(RoleEnum.ADMIN.getRoleName())) {
            Competition competition = competitionRepository.findById(competitionId)
                    .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

            checkTimeAfter(competition.getCompetitionStart());
        }

        List<Object[]> questionsWithChoices = competitionRepository.findQuestionsWithChoices(competitionId);

        List<CompetitionQuestionDto> competitionQuestions = new ArrayList<>();

        for (Object[] row : questionsWithChoices) {
            BigInteger questionIdBigInteger = (BigInteger) row[0];


            Long questionId = questionIdBigInteger.longValue();// 문제 아이디
            String description = (String) row[1];// 문제 설명

            // List []  형태로 가져옴.
            String[] choiceNumbers = ((String) row[2]).split(",");// 보기 번호
            String[] choiceContents = ((String) row[3]).split(",");// 보기 내용

            List<ChoiceQuestionResponseDto> choices = new ArrayList<>();

            for (int i = 0; i < choiceNumbers.length; i++) {
                int number = Integer.parseInt(choiceNumbers[i]);
                String content = choiceContents[i];
                choices.add(new ChoiceQuestionResponseDto(number, content));
            }

            CompetitionQuestionDto competitionQuestionDto = CompetitionQuestionDto.builder()
                    .questionId(questionId)
                    .description(description)
                    .choices(choices)
                    .build();

            competitionQuestions.add(competitionQuestionDto);
        }

        return competitionQuestions;
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
        Competition competition = competitionRepository.findById(requestDto.getCompetitionId())
                .orElseThrow(() -> new NotFoundCompetitionId(requestDto.getCompetitionId()));

        Long workbookId = competition.getWorkbook().getId();
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
