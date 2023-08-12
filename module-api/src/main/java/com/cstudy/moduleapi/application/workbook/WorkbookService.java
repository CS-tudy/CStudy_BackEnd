package com.cstudy.moduleapi.application.workbook;

import com.cstudy.moduleapi.dto.workbook.*;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkbookService {


    Page<WorkbookResponseDto> getWorkbookList(Pageable pageable, String title, String description, String titleDesc);

    WorkbookResponseDto getWorkbook(Long id);

    Page<WorkbookQuestionResponseDto> getQuestions(Long id, Pageable pageable);

    Long createWorkbook(CreateWorkbookRequestDto workbookDto);

    void addQuestion(WorkbookQuestionRequestDto requestDto);

    void updateWorkbook(UpdateWorkbookRequestDto workbookDto);

    void deleteQuestion(WorkbookQuestionRequestDto requestDto);
}
