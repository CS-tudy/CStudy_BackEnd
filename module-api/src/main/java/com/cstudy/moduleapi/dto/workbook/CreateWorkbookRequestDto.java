package com.cstudy.moduleapi.dto.workbook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkbookRequestDto {

    private String title;
    private String description;

}
