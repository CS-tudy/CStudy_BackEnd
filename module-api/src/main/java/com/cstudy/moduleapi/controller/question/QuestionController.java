package com.cstudy.moduleapi.controller.question;

import com.cstudy.moduleapi.application.question.MemberQuestionService;
import com.cstudy.moduleapi.application.question.QuestionService;
import com.cstudy.moduleapi.config.argumentResolver.IfLogin;
import com.cstudy.moduleapi.dto.question.CreateQuestionAndCategoryRequestDto;
import com.cstudy.moduleapi.dto.question.QuestionAnswerDto;
import com.cstudy.moduleapi.dto.question.QuestionResponseDto;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import com.cstudy.modulecommon.dto.QuestionSearchCondition;
import com.cstudy.modulecommon.error.pathvariable.PositivePatriarchal;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Tag(name = "문제 Question", description = "문제 생성 및 대량 데이터 Insert & 문제 페이징 및 정답 선택")
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberQuestionService memberQuestionService;

    public QuestionController(
            QuestionService questionService,
            MemberQuestionService memberQuestionService
    ) {
        this.questionService = questionService;
        this.memberQuestionService = memberQuestionService;
    }

    @Operation(summary = "문제 생성하기", description = "문제, 카테고리, List<선택>을 받아서 문제를 생성한다. / ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void createQuestionWithCategory(@Parameter(name = "CreateQuestionAndCategoryRequestDto", description = "문제 생성 Dto, {문제, 카테고리, List<선택>")
                                           @Valid @RequestBody CreateQuestionAndCategoryRequestDto requestDto) {
        questionService.createQuestionChoice(requestDto);
    }

    @Operation(summary = "대량 문제 생성하기", description = "List<문제, 카테고리, List<선택>>를 Request를 받아서 대량의 문제를 생성한다. / ROLE_ADMIN")
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void buildCreateQuestionWithCategory(@Parameter(name = "List<CreateQuestionAndCategoryRequestDto>", description = "대량 문제 생성을 위한 문제, 카테고리, 선택")
                                                @Valid @RequestBody List<CreateQuestionAndCategoryRequestDto> createQuestionAndCategoryRequestDtos) {
        questionService.bulkCreateQuestionChoice(createQuestionAndCategoryRequestDtos);
    }

    @Operation(summary = "단일 문제 탐색하기", description = "문제 아이디를 받아서 단일 문제 탐색하기 / PermitAll")
    @GetMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public QuestionResponseDto findQuestionPathId(@Parameter(name = "문제 아이디", description = "문제 아이디")
                                                  @PathVariable Long questionId) {
        Optional.of(questionId)
                .filter(id -> id >= 0)
                .orElseThrow(() -> new PositivePatriarchal(questionId));
        return questionService.findQuestionWithChoiceAndCategory(questionId);
    }

    @Operation(summary = "단일 문제에 대한 정답 선택하기", description = "4지선다 문제에 대해서 단일 문제 선택하기 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @PostMapping("/{questionId}/answer")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public QuestionAnswerDto choiceQuestion(@Parameter(name = "문제 아이디", description = "문제 아이디")
                                            @PathVariable Long questionId,
                                            @Parameter(name = "ChoiceAnswerRequestDto", description = "문제 번호, 시간")
                                            @Valid @RequestBody ChoiceAnswerRequestDto choiceNumber,
                                            @Parameter(hidden = true)
                                            @IfLogin LoginUserDto loginUserDto) {
        Optional.of(questionId)
                .filter(id -> id >= 0)
                .orElseThrow(() -> new PositivePatriarchal(questionId));
        questionService.choiceQuestion(loginUserDto, questionId, choiceNumber);
        return memberQuestionService.isCorrectAnswer(loginUserDto.getMemberId(), questionId, choiceNumber);
    }

    @Operation(summary = "전체 문제 탐색하기 (페이징)", description = "기본 10개의 사이즈를 가지는 전체 문제 페이징 / PermitAll")
    @GetMapping
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    public Page<QuestionPageWithCategoryAndTitle> findQuestionPageWithCategoryAndTitleConditionalSearch(@Parameter(name = "QuestionSearchCondition", description = "페이징 조건( 문제 카테고리 제목, 상태, 회원 아이디")
                                                                                                        QuestionSearchCondition searchCondition,
                                                                                                        @Parameter(name = "page", description = "페이지")
                                                                                                        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                                                        @Parameter(name = "size", description = "기본 default 10 , size")
                                                                                                        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                                                        @Parameter(hidden = true)
                                                                                                        @IfLogin LoginUserDto loginUserDto) {


        return questionService.questionPageWithCategory(searchCondition, page, size, loginUserDto);
    }

    @Operation(summary = "단일 회원의 문제 찾기", description = "단일 회원의 문제 5개의 사이즈를 가지는 문제 페이징 / ROLE_CUSTOM', 'ROLE_ADMIN")
    @GetMapping("/my-questions")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public Page<QuestionPageWithCategoryAndTitle> findMyQuestion(@Parameter(name = "page", description = "페이지")
                                                                 @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                 @Parameter(name = "size", description = "기본 default 10 , size")
                                                                 @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                                 @Parameter(hidden = true)
                                                                 @IfLogin LoginUserDto loginUserDto) {
        QuestionSearchCondition condition = QuestionSearchCondition.builder()
                .memberId(loginUserDto.getMemberId())
                .build();
        return questionService.questionPageWithCategory(condition, page, size, loginUserDto);
    }


}
