package com.cstudy.modulecommon.dto;

import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdDate;

    @QueryProjection

    public NoticeResponseDto(Long id, String title, String content, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
    }

    public static NoticeResponseDto of(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdDate(notice.getCreatedDate())
                .build();
    }
}
