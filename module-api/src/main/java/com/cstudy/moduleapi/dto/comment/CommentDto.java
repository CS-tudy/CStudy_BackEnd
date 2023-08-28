package com.cstudy.moduleapi.dto.comment;

import com.cstudy.modulecommon.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private Long memberId;
    private String memberName;

    @Builder.Default
    private List<CommentDto> children = new ArrayList<>();

    public CommentDto(Long id, String content, Long memberId, String memberName) {
        this.id = id;
        this.content = content;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public static CommentDto convertCommentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getContent(), comment.getMember().getId(), comment.getMember().getName());
    }
}
