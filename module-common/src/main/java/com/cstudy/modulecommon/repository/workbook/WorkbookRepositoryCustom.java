package com.cstudy.modulecommon.repository.workbook;

import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import com.cstudy.modulecommon.dto.WorkbookSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkbookRepositoryCustom {

    Page<WorkbookResponseDto> findWorkbookList(Pageable pageable, WorkbookSearchRequestDto requestDto);

    Page<WorkbookQuestionResponseDto> findWorkbookQuestionList(Pageable pageable, Long id);
}
