package com.cstudy.modulecommon.dto;

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
    private String author;
    private Long parentCommentId;
    private List<NoticeCommentResponse> childComments = new ArrayList<>();

    @Builder
    public NoticeCommentResponse(
            Long id,
            String content,
            String author,
            Long parentCommentId
    ) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.parentCommentId = parentCommentId;
    }
}