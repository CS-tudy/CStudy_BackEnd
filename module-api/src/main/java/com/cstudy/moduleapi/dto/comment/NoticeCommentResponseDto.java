package com.cstudy.moduleapi.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeCommentResponseDto {
    private Long id;
    private Long articleId;
    private Long memberId;
    Long parentCommentId;
    String content;
    LocalDateTime createdAt;
}
