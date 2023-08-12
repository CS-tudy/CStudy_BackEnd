package com.cstudy.moduleapi.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeSaveRequestDto {
    @NotEmpty(message = "공지사항 제목을 입력하세요.")
    private String title;
    @NotEmpty(message = "공지사항 내용을 입력하세요.")
    private String content;
}
