package com.cstudy.moduleapi.application.comment;


import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.modulecommon.util.LoginUserDto;

public interface CommentService {
    void saveNoticeComment(NoticeCommentRequestDto noticeCommentRequestDto, LoginUserDto loginUserDto);
}
