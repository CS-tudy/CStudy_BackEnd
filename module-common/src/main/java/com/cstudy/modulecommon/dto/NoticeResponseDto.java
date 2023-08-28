package com.cstudy.modulecommon.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class NoticeResponseDto {
    private String title;
    private String content;

    @QueryProjection
    public NoticeResponseDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
