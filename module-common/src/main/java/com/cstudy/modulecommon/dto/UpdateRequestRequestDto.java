package com.cstudy.modulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestRequestDto {
    private Long id;
    @NotEmpty(message = "title을 입력하세요")
    private String title;
    @NotEmpty(message = "description 입력하세요")
    private String description;
}
