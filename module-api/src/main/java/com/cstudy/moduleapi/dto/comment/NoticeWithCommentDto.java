package com.cstudy.moduleapi.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeWithCommentDto {
    private Long id;
    private Long memberId;
    private Set<NoticeCommentResponseDto> noticeCommentResponseDtos;
    private String title;
    private String content;
    private LocalDateTime createdAt;




}
