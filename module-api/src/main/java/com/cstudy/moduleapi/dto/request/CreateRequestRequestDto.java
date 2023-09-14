package com.cstudy.moduleapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestRequestDto {
    @NotEmpty(message = "title을 입력하세요")
    private String title;
    @NotEmpty(message = "description을 입력하세요")
    private String description;

}
