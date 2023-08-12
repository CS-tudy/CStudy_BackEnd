package com.cstudy.moduleapi.domain.workbook.application.impl;

import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.dto.member.MemberSignupRequest;
import com.cstudy.moduleapi.dto.workbook.CreateWorkbookRequestDto;
import com.cstudy.moduleapi.dto.workbook.QuestionIdRequestDto;
import com.cstudy.moduleapi.dto.workbook.WorkbookQuestionRequestDto;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WorkbookServiceImplTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WorkbookService workbookService;
    @Autowired
    private WorkbookRepository workbookRepository;
    @Autowired
    private QuestionRepository questionRepository;
    private Long memberId1;
    private Long memberId2;

    @BeforeEach
    public void setUp(){
        MemberSignupRequest memberSignupRequest1 = MemberSignupRequest.builder()
            .email("test1@test.com")
            .name("테스트 유저1")
            .password("test1234!").build();
        memberService.signUp(memberSignupRequest1);

        MemberSignupRequest memberSignupRequest2 = MemberSignupRequest.builder()
            .email("test2@test.com")
            .name("테스트 유저2")
            .password("test1234!").build();
        memberService.signUp(memberSignupRequest2);

        Member member1 = memberRepository.findByEmail("test1@test.com")
            .orElseThrow(() -> new NotFoundMemberEmail("test1@test.com"));
        memberId1 = member1.getId();

        Member member2 = memberRepository.findByEmail("test2@test.com")
            .orElseThrow(() -> new NotFoundMemberEmail("test2@test.com"));
        memberId2 = member2.getId();
    }

    @Test
    @DisplayName("문제집 생성")
    public void create(){
        CreateWorkbookRequestDto requestDto = CreateWorkbookRequestDto.builder()
                .title("문제집 제목1")
                .description("문제집 설명1")
                .build();
        Long workbookId = workbookService.createWorkbook(requestDto);
        boolean present = workbookRepository.findById(workbookId).isPresent();
        assertTrue(present);
    }

    @Test
    @DisplayName("문제집 리스트 조회")
    public void workbookList(){
        for (int i = 1; i <= 10; i++) {
            CreateWorkbookRequestDto requestDto = CreateWorkbookRequestDto.builder()
                    .title("문제집 제목"+i)
                    .description("문제집 설명"+i)
                    .build();
            Long workbookId = workbookService.createWorkbook(requestDto);
        }
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        Page<WorkbookResponseDto> workbookList =
                workbookService.getWorkbookList(pageable, null, null, null);
        for (int i = 10; i > 5; i--) {
            assertEquals(workbookList.getContent().get(10-i).getTitle(), "문제집 제목"+i);
        }
    }

    @Test
    @DisplayName("제목, 내용으로 문제집 검색")
    public void workbookSearch(){
        for (int i = 1; i <= 10; i++) {
            CreateWorkbookRequestDto requestDto = CreateWorkbookRequestDto.builder()
                .title("문제집 제목"+i)
                .description("문제집 설명"+i)
                .build();
            workbookService.createWorkbook(requestDto);
            CreateWorkbookRequestDto requestDto1 = CreateWorkbookRequestDto.builder()
                .title("문제집 제목"+i)
                .description("문제집 내용"+i)
                .build();
            workbookService.createWorkbook(requestDto1);
        }
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        Page<WorkbookResponseDto> workbookList =
                workbookService.getWorkbookList(pageable, "1", null, null);
        assertEquals(workbookList.getTotalElements(), 4);
        assertEquals(workbookList.getContent().get(0).getTitle(), "문제집 제목10");

        Page<WorkbookResponseDto> workbookList1 =
                workbookService.getWorkbookList(pageable, "1", "설명", null);
        assertEquals(workbookList1.getTotalElements(), 2);
        assertEquals(workbookList1.getContent().get(0).getDescription(), "문제집 설명10");

    }

    @Test
    @DisplayName("문제집 정보 수정")
    public void update(){
        CreateWorkbookRequestDto requestDto = CreateWorkbookRequestDto.builder()
                .title("문제집 제목1")
                .description("문제집 설명1")
                .build();
        Long workbookId = workbookService.createWorkbook(requestDto);
        UpdateWorkbookRequestDto requestDto1 = UpdateWorkbookRequestDto.builder()
                .id(workbookId)
                .title("문제집 제목2")
                .description("문제집 설명2")
                .build();
        workbookService.updateWorkbook(requestDto1);
        WorkbookResponseDto workbook = workbookService.getWorkbook(workbookId);
        assertEquals(workbook.getTitle(), "문제집 제목2");
        assertEquals(workbook.getDescription(), "문제집 설명2");
    }

    @Test
    @DisplayName("문제집에 문제 추가")
    public void addQuestion(){
        CreateWorkbookRequestDto requestDto = CreateWorkbookRequestDto.builder()
                .title("문제집 제목1")
                .description("문제집 설명1")
                .build();
        Long workbookId = workbookService.createWorkbook(requestDto);
        List<QuestionIdRequestDto> questionIdRequestDtos = createQuestionDto(10);

        WorkbookQuestionRequestDto requestDto1 = WorkbookQuestionRequestDto.builder()
                .workbookId(workbookId)
                .questionIds(questionIdRequestDtos)
                .build();
        workbookService.addQuestion(requestDto1);

        Pageable pageable = PageRequest.of(0, 5);
        Page<WorkbookQuestionResponseDto> questions = workbookService.getQuestions(workbookId, pageable);
        assertEquals(questions.getTotalElements(), 10);
        for (int i = 0; i < 5; i++) {
            assertEquals(questions.getContent().get(i).getQuestionId(), questionIdRequestDtos.get(9-i).getId());
        }

        List<QuestionIdRequestDto> questionIdRequestDtos1 = createQuestionDto(5);

        WorkbookQuestionRequestDto requestDto2 = WorkbookQuestionRequestDto.builder()
            .workbookId(workbookId)
            .questionIds(questionIdRequestDtos1)
            .build();
        workbookService.addQuestion(requestDto2);

        Page<WorkbookQuestionResponseDto> questions1 = workbookService.getQuestions(workbookId, pageable);
        assertEquals(questions1.getTotalElements(), 15);
    }

    @Test
    @DisplayName("문제집에서 문제 삭제")
    public void deleteQuestion(){
        CreateWorkbookRequestDto requestDto = CreateWorkbookRequestDto.builder()
            .title("문제집 제목1")
            .description("문제집 설명1")
            .build();
        Long workbookId = workbookService.createWorkbook(requestDto);

        List<QuestionIdRequestDto> questionIdRequestDtos = createQuestionDto(10);

        WorkbookQuestionRequestDto requestDto1 = WorkbookQuestionRequestDto.builder()
                .workbookId(workbookId)
                .questionIds(questionIdRequestDtos)
                .build();
        workbookService.addQuestion(requestDto1);

        List<QuestionIdRequestDto> questionIdRequestDtos1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            questionIdRequestDtos1.add(questionIdRequestDtos.get(i));
        }

        WorkbookQuestionRequestDto requestDto2 = WorkbookQuestionRequestDto.builder()
                .workbookId(workbookId)
                .questionIds(questionIdRequestDtos1)
                .build();
        workbookService.deleteQuestion(requestDto2);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        Page<WorkbookQuestionResponseDto> questions = workbookService.getQuestions(workbookId, pageable);
        assertEquals(questions.getTotalElements(), 5);
    }

    public List<QuestionIdRequestDto> createQuestionDto(int num){
        List<QuestionIdRequestDto> questionIdRequestDtos = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Question question = Question.builder()
                .title("문제"+i)
                .description("문제 내용"+i).build();
            questionRepository.save(question);
            QuestionIdRequestDto questionIdRequestDto = QuestionIdRequestDto.builder()
                .id(question.getId())
                .build();
            questionIdRequestDtos.add(questionIdRequestDto);
        }
        return  questionIdRequestDtos;
    }
}