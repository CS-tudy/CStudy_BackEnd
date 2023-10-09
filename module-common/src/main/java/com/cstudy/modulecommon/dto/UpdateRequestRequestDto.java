package com.cstudy.modulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestRequestDto {
    @Positive(message = "아이디는 양수를 입력을 하세요.")
    private Long id;
    @NotEmpty(message = "title을 입력하세요")
    private String title;
    @NotEmpty(message = "description 입력하세요")
    private String description;
}
