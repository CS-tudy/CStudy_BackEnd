package com.cstudy.modulecommon.error.comment;

import com.cstudy.modulecommon.error.abstracts.CommentAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundCommentParentId extends CommentAbstractException {

    public NotFoundCommentParentId(String message) {
        super("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 {}" + message);
    }

    public NotFoundCommentParentId(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundCommentParentId.getErrorCode();
    }
}
