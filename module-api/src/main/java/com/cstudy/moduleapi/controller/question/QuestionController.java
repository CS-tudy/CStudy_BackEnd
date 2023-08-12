package com.cstudy.moduleapi.controller.question;

import com.cstudy.moduleapi.application.question.MemberQuestionService;
import com.cstudy.moduleapi.application.question.QuestionService;
import com.cstudy.moduleapi.dto.question.*;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.cstudy.moduleapi.util.IfLogin;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.cstudy.modulecommon.dto.QuestionSearchCondition;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "문제 Question", description = "문제 생성 및 대량 데이터 Insert & 문제 페이징 및 정답 선택")

@RestController
@RequestMapping("/api")
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


    @Operation(summary = "문제 생성하기", description = "문제, 카테고리, 문제 보기, 정답을 생성을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "문제 생성하기 성공"),
            @ApiResponse(responseCode = "400", description = "문제 생성하기 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("question")
    @ResponseStatus(HttpStatus.CREATED)
    public void createQuestionWithCategory(
            @Parameter(name = "CreateQuestionAndCategoryRequestDto", description = "createQuestionRequestDto, categoryRequestDto, createChoicesAboutQuestionDto")
            @RequestBody CreateQuestionAndCategoryRequestDto requestDto
    ) {
        questionService.createQuestionChoice(requestDto);
    }


    @Operation(summary = "대량 문제 생성하기", description = "재귀를 통하여 문제 List Insert 하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "문제 대량 생성하기 성공"),
            @ApiResponse(responseCode = "400", description = "문제 대량 생성하기 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("questions")
    @ResponseStatus(HttpStatus.CREATED)
    public void buildCreateQuestionWithCategory(

            @Parameter(name = "List<CreateQuestionAndCategoryRequestDto>", description = "List<createQuestionRequestDto, categoryRequestDto, createChoicesAboutQuestionDto>")

            @RequestBody List<CreateQuestionAndCategoryRequestDto> requestDtos
    ) {
        questionService.recursiveCreateQuestionChoice(requestDtos);
    }


    @Operation(summary = "단일 문제 찾기", description = "question Id를 이용하여 단일 문제 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단일 문제 찾기 성공"),
            @ApiResponse(responseCode = "400", description = "단일 문제 찾기 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("question/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto findQuestionPathId(

            @Parameter(name = "questionId", description = "문제 번호")

            @PathVariable Long questionId
    ) {
        return questionService.findQuestionWithChoiceAndCategory(questionId);
    }


    @Operation(summary = "단일 문제 정답 선택하기", description = "question Id를 이용하여 단일 문제 정답 선택하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "단일 문제 정답 선택하기 성공"),
            @ApiResponse(responseCode = "400", description = "단일 문제 정답 선택하기 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("question/{questionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionAnswerDto choiceQuestion(
            @Parameter(name = "questionId", description = "문제 번호")
            @PathVariable Long questionId,
            @Parameter(name = "ChoiceAnswerRequestDto", description = "정답 선택 번호")
            @RequestBody ChoiceAnswerRequestDto choiceNumber,
            @Parameter(name = "LoginUserDto", description = "로그인 회원 정보")
            @IfLogin LoginUserDto loginUserDto

    ) {
        questionService.choiceQuestion(loginUserDto, questionId, choiceNumber);
        return memberQuestionService.isCorrectAnswer(loginUserDto.getMemberId(), questionId, choiceNumber);
    }


    @Operation(summary = "전체 문제 페이징", description = "전체 문제 페이징 처리 default page:0, size:10")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 문제 페이징 성공"),
            @ApiResponse(responseCode = "400", description = "전체 문제 페이징 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("questions")
    @ResponseStatus(HttpStatus.OK)
    public Page<QuestionPageWithCategoryAndTitle> findQuestionPageWithCategoryAndTitleConditionalSearch(
            @Parameter(name = "searchCondition", description = "조건 검색")
            QuestionSearchCondition searchCondition,
            @Parameter(name = "page", description = "페이징 default 0")
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @Parameter(name = "size", description = "페이징 default 10")
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @IfLogin LoginUserDto loginUserDto
    ) {
        return questionService.questionPageWithCategory(searchCondition, page, size, loginUserDto);
    }

    @Operation(summary = "내가 푼 문제 조회", description = "내가 푼 문제 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 푼 문제 조회 성공")
    })
    @GetMapping("questions/myquestion")
    @ResponseStatus(HttpStatus.OK)
    public Page<QuestionPageWithCategoryAndTitle> findMyQuestion(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @Parameter(hidden = true)
            @IfLogin LoginUserDto loginUserDto
    ) {
        QuestionSearchCondition condition = QuestionSearchCondition.builder()
                .memberId(loginUserDto.getMemberId())
                .build();
        return questionService.questionPageWithCategory(condition, page, size, loginUserDto);
    }
}
