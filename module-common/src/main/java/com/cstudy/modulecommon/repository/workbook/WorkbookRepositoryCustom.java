package com.cstudy.modulecommon.repository.workbook;

import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkbookRepositoryCustom {

    Page<WorkbookResponseDto> findWorkbookList(Pageable pageable, String title, String description, String titleDesc);

    Page<WorkbookQuestionResponseDto> findWorkbookQuestionList(Pageable pageable, Long id);
}
