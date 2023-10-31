package com.cstudy.modulecommon.error.alarm;


import com.cstudy.modulecommon.error.abstracts.AlarmAbstractException;
import com.cstudy.modulecommon.error.abstracts.CategoryAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class AlarmInvalidException extends AlarmAbstractException {

    public AlarmInvalidException(String message) {
        super("알람의 데이터를 확인하세요 : {}" + message);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundCategoryTile.getErrorCode();
    }
}
