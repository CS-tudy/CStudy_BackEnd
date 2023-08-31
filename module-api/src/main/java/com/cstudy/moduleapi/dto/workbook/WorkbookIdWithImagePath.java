package com.cstudy.moduleapi.dto.workbook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkbookIdWithImagePath {
    private Long id;
    private List<String> imagePath;
}
