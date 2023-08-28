package com.cstudy.moduleapi.application.comment;


import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.moduleapi.dto.comment.NoticeCommentResponse;
import com.cstudy.modulecommon.util.LoginUserDto;

import java.util.List;
public interface CommentService {
    void saveNoticeComment(NoticeCommentRequestDto noticeCommentRequestDto, LoginUserDto loginUserDto);
    List<NoticeCommentResponse> getCommentsForNotice(Long noticeId);
}
