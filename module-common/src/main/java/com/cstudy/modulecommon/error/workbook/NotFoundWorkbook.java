package com.cstudy.modulecommon.error.workbook;


import com.cstudy.modulecommon.error.abstracts.WorkbookAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundWorkbook extends WorkbookAbstractException {

    public NotFoundWorkbook(Long id) {
        super("Not Found Workbook: " + id);
    }

    public NotFoundWorkbook(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundWorkbook.getErrorCode();
    }

}
