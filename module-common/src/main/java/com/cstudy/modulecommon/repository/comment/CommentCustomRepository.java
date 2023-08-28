package com.cstudy.modulecommon.repository.comment;

import com.cstudy.modulecommon.domain.comment.Comment;

import java.util.List;

public interface CommentCustomRepository {
    List<Comment> findNoticePage(Long noticeId);
}
