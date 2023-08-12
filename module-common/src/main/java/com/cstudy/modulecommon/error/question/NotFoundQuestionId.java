package com.cstudy.modulecommon.error.question;


import com.cstudy.modulecommon.error.abstracts.QuestionAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundQuestionId extends QuestionAbstractException {

    public NotFoundQuestionId(Long id) {
        super("Not Found Question with: " + id);
    }

    public NotFoundQuestionId(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundQuestionId.getErrorCode();
    }


}
