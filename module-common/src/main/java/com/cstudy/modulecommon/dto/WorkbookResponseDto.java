package com.cstudy.modulecommon.dto;

import com.cstudy.modulecommon.domain.workbook.Workbook;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkbookResponseDto {

    private Long id;
    private String title;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static WorkbookResponseDto of(Workbook workbook) {
        return WorkbookResponseDto.builder()
                .id(workbook.getId())
                .title(workbook.getTitle())
                .description(workbook.getDescription())
                .createdAt(workbook.getCreatedAt())
                .build();
    }

    public static WorkbookResponseDto createWorkbook(Long id, String title, String description, LocalDateTime createdAt) {
        return new WorkbookResponseDto(id, title, description, createdAt);
    }
}
