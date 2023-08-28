package com.cstudy.moduleapi.dto.comment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NoticeCommentResponse {
    private Long id;
    private String content;
    private Long memberId;
    private String author;
    private Long parentCommentId;
    private List<NoticeCommentResponse> childComments = new ArrayList<>();

    @Builder
    public NoticeCommentResponse(
            Long id,
            String content,
            Long memberId,
            String author,
            Long parentCommentId
    ) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
        this.author = author;
        this.parentCommentId = parentCommentId;
    }
}