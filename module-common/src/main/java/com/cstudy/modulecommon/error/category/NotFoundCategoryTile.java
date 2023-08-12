package com.cstudy.modulecommon.error.category;


import com.cstudy.modulecommon.error.abstracts.CategoryAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundCategoryTile extends CategoryAbstractException {

    public NotFoundCategoryTile(String message) {
        super("Not Found Category Title" + message);
    }

    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundCategoryTile.getErrorCode();
    }
}
