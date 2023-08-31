package com.cstudy.modulecommon.error.comment;

import com.cstudy.modulecommon.error.abstracts.CommentAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundCommentId extends CommentAbstractException {

    public NotFoundCommentId(Long message) {
        super("댓글을 찾을 수 없습니다. {}" + message);
    }

    public NotFoundCommentId(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundCommentParentId.getErrorCode();
    }
}
