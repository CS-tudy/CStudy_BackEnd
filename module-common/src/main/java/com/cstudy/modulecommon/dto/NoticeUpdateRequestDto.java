package com.cstudy.modulecommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUpdateRequestDto {
    @NotEmpty(message = "제목을 입력하세요.")
    private String title;
    @NotEmpty(message = "내용을 입력하세요.")
    private String content;
}
