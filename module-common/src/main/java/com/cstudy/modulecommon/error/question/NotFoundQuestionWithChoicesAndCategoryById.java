package com.cstudy.modulecommon.error.question;


import com.cstudy.modulecommon.error.abstracts.QuestionAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundQuestionWithChoicesAndCategoryById extends QuestionAbstractException {
    public NotFoundQuestionWithChoicesAndCategoryById(Long message) {
        super("Not Found Question With Choices And Category" + message);
    }

    public NotFoundQuestionWithChoicesAndCategoryById(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundQuestionWithChoicesAndCategoryById.getErrorCode();
    }

}
