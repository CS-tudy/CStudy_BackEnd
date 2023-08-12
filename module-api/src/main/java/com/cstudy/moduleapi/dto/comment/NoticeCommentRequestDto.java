package com.cstudy.moduleapi.dto.comment;

import com.cstudy.modulecommon.domain.comment.Comment;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeCommentRequestDto {
    private Long noticeId;
    private Long parentCommentId;
    private String content;

    public Comment toEntity(Notice notice, Member member) {
        return Comment.of(notice, member, this.content);
    }
}
