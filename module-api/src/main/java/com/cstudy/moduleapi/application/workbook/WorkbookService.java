package com.cstudy.moduleapi.application.workbook;

import com.cstudy.moduleapi.dto.workbook.CreateWorkbookRequestDto;
import com.cstudy.moduleapi.dto.workbook.WorkbookIdWithImagePath;
import com.cstudy.moduleapi.dto.workbook.WorkbookQuestionRequestDto;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import com.cstudy.modulecommon.dto.WorkbookSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkbookService {

//page,size, requestDto
    Page<WorkbookResponseDto> getWorkbookList(int page, int size, WorkbookSearchRequestDto requestDto);

    List<WorkbookIdWithImagePath> getWorkbookImagePathList();

    WorkbookResponseDto getWorkbook(Long id);

    Page<WorkbookQuestionResponseDto> getQuestions(Long id, Pageable pageable);

    Long createWorkbook(CreateWorkbookRequestDto workbookDto);

    void addQuestion(WorkbookQuestionRequestDto requestDto);

    void updateWorkbook(UpdateWorkbookRequestDto workbookDto);

    void deleteQuestion(WorkbookQuestionRequestDto requestDto);

    void uploadFile(MultipartFile file, Long workbookId);
}
