package com.cstudy.modulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkbookQuestionResponseDto {

    private Long workbookQuestionId;

    private Long questionId;

    private String title;

    public static WorkbookQuestionResponseDto createWorkQuestion(Long workbookQuestionId, Long questionId, String title) {
        return new WorkbookQuestionResponseDto(workbookQuestionId, questionId, title);
    }
}
