package com.cstudy.moduleapi.controller.workbook;

import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.dto.workbook.CreateWorkbookRequestDto;
import com.cstudy.moduleapi.dto.workbook.WorkbookIdWithImagePath;
import com.cstudy.moduleapi.dto.workbook.WorkbookQuestionRequestDto;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import com.cstudy.modulecommon.dto.WorkbookSearchRequestDto;
import com.cstudy.modulecommon.error.pathvariable.PositivePatriarchal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    public Page<WorkbookResponseDto> getWorkbookList(@Parameter(name = "page", description = "페이지")
                                                     @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                     @Parameter(name = "size", description = "기본 default 10 , size")
                                                     @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                     @Parameter(name = "title,description,titleDesc", description = "조건 검색 페이징")
                                                     WorkbookSearchRequestDto requestDto) {
        return workbookService.getWorkbookList(page, size, requestDto);
    }

    @Operation(summary = "문제집 정보 요청", description = "문제집 id를 이용해 문제집 정보를 요청합니다. / PermitAll")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public WorkbookResponseDto getWorkbook(@Parameter(name = "id", description = "문제집 id")
                                           @PathVariable("id") Long id) {
        Optional.of(id)
                .filter(workbook -> workbook >= 0)
                .orElseThrow(() -> new PositivePatriarchal(id));
        return workbookService.getWorkbook(id);
    }

    @Operation(summary = "문제집 이미지 업로드", description = "문제집 이미지 업로드, FILE을 기반으로 workbookId로 매핑한다.")
    @PostMapping("/upload/{workbookId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void uploadFile(@Parameter(name = "문제집 업로드 이미지 파일", description = "MultipartFile 이미지 업로드 파일")
                           @RequestParam(value = "file") MultipartFile file,
                           @Parameter(name = "문제집 아이디", description = "workbookId")
                           @PathVariable Long workbookId) {
        Optional.of(workbookId)
                .filter(workbook -> workbook >= 0)
                .orElseThrow(() -> new PositivePatriarchal(workbookId));
        workbookService.uploadFile(file, workbookId);
    }

    @Operation(summary = "문제집 이미지 Path를 List 형태로 보여준다.", description = "문제집 아이디와 해당 아이디의 이미지 Path를 보여준다.")
    @GetMapping("/images")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public List<WorkbookIdWithImagePath> getWorkbookImagePathList() {
        return workbookService.getWorkbookImagePathList();
    }

    @Operation(summary = "문제집 문제 요청", description = "문제집 id를 이용해 문제집에 포함된 문제를 요청합니다. title: 문제집 제목, description: 문제집 설명 / PermitAll")
    @GetMapping("/{id}/questions")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public Page<WorkbookQuestionResponseDto> getQuestions(@Parameter(description = "page: 페이지 번호, size: 한 페이지 문제 수.")
                                                          @PageableDefault Pageable pageable,
                                                          @Parameter(name = "id", description = "문제집 id")
                                                          @PathVariable("id") Long id) {
        Optional.of(id)
                .filter(workbook -> workbook >= 0)
                .orElseThrow(() -> new PositivePatriarchal(id));
        return workbookService.getQuestions(id, pageable);
    }

    @Operation(summary = "문제집 생성", description = "문제집을 생성합니다. title: 문제집 제목, description: 문제집 설명 / ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void createWorkbook(@Parameter(description = "title: 문제집 제목, description: 문제집 내용")
                               @Valid @RequestBody CreateWorkbookRequestDto workbookDto) {
        workbookService.createWorkbook(workbookDto);
    }

    @Operation(summary = "문제집에 문제 추가", description = "문제집 id와 문제 id를 이용해 문제집에 문제 리스트를 추가합니다. / ROLE_ADMIN")
    @PostMapping("/workbook/questions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void addQuestion(@Parameter(description = "workbookId: 문제집 id, questionIds.id: 삭제할 문제 번호")
                            @Valid @RequestBody WorkbookQuestionRequestDto requestDto) {
        workbookService.addQuestion(requestDto);
    }

    @Operation(summary = "문제집 정보 수정", description = "문제집 이름과 설명을 수정합니다. / ROLE_ADMIN")
    @PatchMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateWorkbook(@Parameter(description = "id: 문제집 id, title: 문제집 제목, description: 문제집 내용")
                               @Valid @RequestBody UpdateWorkbookRequestDto workbookDto) {
        workbookService.updateWorkbook(workbookDto);
    }

    @Operation(summary = "문제집에서 문제 삭제", description = "문제집 id, 문제 id를 이용해 문제집에서 문제를 삭제합니다. / ROLE_ADMIN")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void deleteQuestion(@Parameter(description = "workbookId: 문제집 id, questionIds.id: 삭제할 문제 번호")
                               @Valid @RequestBody WorkbookQuestionRequestDto requestDto) {
        workbookService.deleteQuestion(requestDto);
    }


}
