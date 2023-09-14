package com.cstudy.moduleapi.controller.comment;

import com.cstudy.moduleapi.application.comment.CommentService;
import com.cstudy.moduleapi.config.argumentResolver.IfLogin;
import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.moduleapi.dto.comment.NoticeCommentResponse;
import com.cstudy.modulecommon.error.pathvariable.PositivePatriarchal;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "notice 댓글", description = "댓글 생성")
@RestController
@RequestMapping("/api/comment")
public class NoticeCommentController {

    private final CommentService commentService;

    public NoticeCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 생성하기", description = "계층형으로 N차 댓글을 생성할 수 있습니다. / ROLE_CUSTOM', 'ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOM', 'ROLE_ADMIN')")
    public void postNewNoticeComment(@Parameter(name = "noticeCommentRequestDto", description = "공지사항 아이디, 부모 댓글 Id, 내용")
                                     @Valid @RequestBody NoticeCommentRequestDto noticeCommentRequestDto,
                                     @Parameter(hidden = true)
                                     @IfLogin LoginUserDto loginUserDto) {
        commentService.saveNoticeComment(noticeCommentRequestDto, loginUserDto);
    }


    @Operation(summary = "계층형 댓글 조회", description = "계층형 댓글 조회하기 / PermitAll")
    @GetMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public List<NoticeCommentResponse> getCommentsForNotice(@Parameter(name = "noticeId", description = "공지사항 아이디")
                                                            @PathVariable Long noticeId) {
        Optional.of(noticeId)
                .filter(id -> id >= 0)
                .orElseThrow(() -> new PositivePatriarchal(noticeId));
        return commentService.getCommentsForNotice(noticeId);
    }

    @Operation(summary = "계층형 댓글 삭제", description = "계층형 댓글 삭제하기 (최상위 댓글만 삭제 - 자식 대댓글 모두 삭제) / ROLE_ADMIN")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void deleteComment(@Parameter(name = "commentId", description = "댓글 아이디")
                              @PathVariable Long commentId) {
        Optional.of(commentId)
                .filter(id -> id >= 0)
                .orElseThrow(() -> new PositivePatriarchal(commentId));
        commentService.deleteComment(commentId);
    }

}
