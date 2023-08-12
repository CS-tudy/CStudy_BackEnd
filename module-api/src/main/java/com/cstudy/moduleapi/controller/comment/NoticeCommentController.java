package com.cstudy.moduleapi.controller.comment;

import com.cstudy.moduleapi.application.comment.CommentService;
import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.moduleapi.util.IfLogin;
import com.cstudy.modulecommon.util.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "notice 댓글", description = "댓글 생성")
@RestController
public class NoticeCommentController {

    private final CommentService commentService;

    public NoticeCommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @Operation(summary = "댓글 생성하기", description = "계층형으로 N차 댓글을 생성할 수 있습니다.")
    @PostMapping("/api/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void postNewNoticeComment(
            @Parameter(name = "noticeCommentRequestDto", description = "공지사항 아이디, 부모 댓글 Id, 내용")
            @RequestBody NoticeCommentRequestDto noticeCommentRequestDto,
            @IfLogin LoginUserDto loginUserDto
    ) {
        commentService.saveNoticeComment(noticeCommentRequestDto, loginUserDto);
    }
}
