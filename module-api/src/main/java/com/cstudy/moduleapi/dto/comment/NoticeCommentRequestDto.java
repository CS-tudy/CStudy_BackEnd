package com.cstudy.moduleapi.dto.comment;

import com.cstudy.modulecommon.domain.comment.Comment;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공지사항 댓글 Request Dto")
public class NoticeCommentRequestDto {
    @Positive(message = "noticeId는 양수여야 합니다.")
    private Long noticeId;

    @Nullable
    @Positive(message = "parentCommentId는 양수여야 합니다.")
    @Schema(description = "부모 댓글 ID")
    private Long parentCommentId;

    @NotNull(message = "content는 필수 항목입니다.")
    private String content;

    public Comment toEntity(Notice notice, Member member) {
        return Comment.of(notice, member, this.content);
    }
}
