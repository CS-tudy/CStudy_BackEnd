package com.cstudy.moduleapi.controller.workbook;

import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.dto.workbook.CreateWorkbookRequestDto;
import com.cstudy.moduleapi.dto.workbook.WorkbookQuestionRequestDto;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@Tag(name = "Workbook(문제집 API)", description = "문제집 관련 API(문제집 생성, 조회)")
@RestController
@RequestMapping("/api/workbook")
public class WorkbookController {

    private final WorkbookService workbookService;

    public WorkbookController(WorkbookService workbookService) {
        this.workbookService = workbookService;
    }

    @PermitAll
    @Operation(summary = "문제집리스트", description = "문제집 리스트를 요청합니다. title, description 값을 설정하면 해당하는 문제집을 검색할 수 있습니다. / PermitAll")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<WorkbookResponseDto> getWorkbookList(@Parameter(description = "page: 페이지 번호, size: 한 페이지 문제 수.")
                                                     @PageableDefault(sort = {"createdAt"}, direction = Direction.DESC) Pageable pageable,
                                                     @Parameter(name = "title", description = "문제집 검색할 때 문제집 제목")
                                                     @RequestParam(value = "title", defaultValue = "") String title,
                                                     @Parameter(name = "description", description = "문제집 검색할 때 문제집 내용")
                                                     @RequestParam(value = "description", defaultValue = "") String description,
                                                     @Parameter(name = "description", description = "제목+내용")
                                                     @RequestParam(value = "title_desc", defaultValue = "") String titleDesc) {
        return workbookService.getWorkbookList(pageable, title, description, titleDesc);
    }

    @Operation(summary = "문제집 정보 요청", description = "문제집 id를 이용해 문제집 정보를 요청합니다. / PermitAll")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public WorkbookResponseDto getWorkbook(@Parameter(name = "id", description = "문제집 id")
                                           @PathVariable("id") Long id) {
        return workbookService.getWorkbook(id);
    }

    @Operation(summary = "문제집 문제 요청", description = "문제집 id를 이용해 문제집에 포함된 문제를 요청합니다. title: 문제집 제목, description: 문제집 설명 / PermitAll")
    @GetMapping("/{id}/questions")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public Page<WorkbookQuestionResponseDto> getQuestions(@Parameter(description = "page: 페이지 번호, size: 한 페이지 문제 수.")
                                                          @PageableDefault Pageable pageable,
                                                          @Parameter(name = "id", description = "문제집 id")
                                                          @PathVariable("id") Long id) {
        return workbookService.getQuestions(id, pageable);
    }

    @Operation(summary = "문제집 생성", description = "문제집을 생성합니다. title: 문제집 제목, description: 문제집 설명 / ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void createWorkbook(@Parameter(description = "title: 문제집 제목, description: 문제집 내용")
                               @RequestBody CreateWorkbookRequestDto workbookDto) {
        workbookService.createWorkbook(workbookDto);
    }

    @Operation(summary = "문제집에 문제 추가", description = "문제집 id와 문제 id를 이용해 문제집에 문제 리스트를 추가합니다. / ROLE_ADMIN")
    @PostMapping("/workbook/questions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void addQuestion(@Parameter(description = "workbookId: 문제집 id, questionIds.id: 삭제할 문제 번호")
                            @RequestBody WorkbookQuestionRequestDto requestDto) {
        workbookService.addQuestion(requestDto);
    }

    @Operation(summary = "문제집 정보 수정", description = "문제집 이름과 설명을 수정합니다. / ROLE_ADMIN")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateWorkbook(@Parameter(description = "id: 문제집 id, title: 문제집 제목, description: 문제집 내용")
                               @RequestBody UpdateWorkbookRequestDto workbookDto) {
        workbookService.updateWorkbook(workbookDto);
    }

    @Operation(summary = "문제집에서 문제 삭제", description = "문제집 id, 문제 id를 이용해 문제집에서 문제를 삭제합니다. / ROLE_ADMIN")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void deleteQuestion(@Parameter(description = "workbookId: 문제집 id, questionIds.id: 삭제할 문제 번호")
                               @RequestBody WorkbookQuestionRequestDto requestDto) {
        workbookService.deleteQuestion(requestDto);
    }


}
