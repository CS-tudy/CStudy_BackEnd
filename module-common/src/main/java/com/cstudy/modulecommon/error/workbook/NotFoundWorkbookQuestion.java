package com.cstudy.modulecommon.error.workbook;


import com.cstudy.modulecommon.error.abstracts.WorkbookAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundWorkbookQuestion extends WorkbookAbstractException {

    public NotFoundWorkbookQuestion() {
        super("Not Found Question in Workbook");
    }

    public NotFoundWorkbookQuestion(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundWorkbookQuestion.getErrorCode();
    }

}
