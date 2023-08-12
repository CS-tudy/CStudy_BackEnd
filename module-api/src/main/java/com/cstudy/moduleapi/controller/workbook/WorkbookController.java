package com.cstudy.moduleapi.controller.workbook;

import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.dto.workbook.*;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Workbook(문제집 API)", description = "문제집 관련 API(문제집 생성, 조회)")
@RestController
@RequestMapping("/api")
public class WorkbookController {

    private final WorkbookService workbookService;

    public WorkbookController(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }

    @Operation(summary = "문제집리스트", description = "문제집 리스트를 요청합니다. title, description 값을 설정하면 해당하는 문제집을 검색할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문제집 리스트 요청 성공")
    })
    @GetMapping("/workbook/list")
    @ResponseStatus(HttpStatus.OK)
    public Page<WorkbookResponseDto> getWorkbookList(
            @Parameter(description = "page: 페이지 번호, size: 한 페이지 문제 수.")
            @PageableDefault(sort = {"createdAt"}, direction = Direction.DESC) Pageable pageable,
            @Parameter(name = "title", description = "문제집 검색할 때 문제집 제목")
            @RequestParam(value = "title", defaultValue = "") String title,
            @Parameter(name = "description", description = "문제집 검색할 때 문제집 내용")
            @RequestParam(value = "description", defaultValue = "") String description,
            @Parameter(name = "description", description = "제목+내용")
            @RequestParam(value = "title_desc", defaultValue = "") String titleDesc
    ){
        return workbookService.getWorkbookList(pageable, title, description, titleDesc);
    }

    @Operation(summary = "문제집 정보 요청", description = "문제집 id를 이용해 문제집 정보를 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문제집 정보 요청 성공"),
            @ApiResponse(responseCode = "400", description = "문제집을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/workbook/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WorkbookResponseDto getWorkbook(
            @Parameter(name = "id", description = "문제집 id")
            @PathVariable("id") Long id
    ){
        return workbookService.getWorkbook(id);
    }

    @Operation(summary = "문제집 문제 요청", description = "문제집 id를 이용해 문제집에 포함된 문제를 요청합니다. title: 문제집 제목, description: 문제집 설명")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문제집 문제 요청 성공"),
            @ApiResponse(responseCode = "400", description = "문제집을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/workbook/question/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Page<WorkbookQuestionResponseDto> getQuestions(
            @Parameter(description = "page: 페이지 번호, size: 한 페이지 문제 수.")
            @PageableDefault Pageable pageable,
            @Parameter(name = "id", description = "문제집 id")
            @PathVariable("id") Long id
    ) {
        return workbookService.getQuestions(id, pageable);
    }

    @Operation(summary = "문제집 생성", description = "문제집을 생성합니다. title: 문제집 제목, description: 문제집 설명")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "문제집 생성 성공"),
            @ApiResponse(responseCode = "400", description = "문제집 생성 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/workbook/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createWorkbook(
            @Parameter(description = "title: 문제집 제목, description: 문제집 내용")
            @RequestBody CreateWorkbookRequestDto workbookDto
    ) {
        workbookService.createWorkbook(workbookDto);
    }

    @Operation(summary = "문제집에 문제 추가", description = "문제집 id와 문제 id를 이용해 문제집에 문제 리스트를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "문제 추가 성공"),
            @ApiResponse(responseCode = "400", description = "문제 추가 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/workbook/question/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addQuestion(
            @Parameter(description = "workbookId: 문제집 id, questionIds.id: 삭제할 문제 번호")
            @RequestBody WorkbookQuestionRequestDto requestDto
    ) {
        workbookService.addQuestion(requestDto);
    }

    @Operation(summary = "문제집 정보 수정", description = "문제집 이름과 설명을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "문제집 수정 성공"),
            @ApiResponse(responseCode = "400", description = "문제집 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/workbook/update")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateWorkbook(
            @Parameter(description = "id: 문제집 id, title: 문제집 제목, description: 문제집 내용")
            @RequestBody UpdateWorkbookRequestDto workbookDto
    ){
        workbookService.updateWorkbook(workbookDto);
    }

    @Operation(summary = "문제집에서 문제 삭제", description = "문제집 id, 문제 id를 이용해 문제집에서 문제를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "문제 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "문제 삭제 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/workbook/question/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(
            @Parameter(description = "workbookId: 문제집 id, questionIds.id: 삭제할 문제 번호")
            @RequestBody WorkbookQuestionRequestDto requestDto
    ){
        workbookService.deleteQuestion(requestDto);
    }


}
