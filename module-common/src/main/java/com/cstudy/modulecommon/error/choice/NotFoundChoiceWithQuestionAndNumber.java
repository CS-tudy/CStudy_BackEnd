package com.cstudy.modulecommon.error.choice;


import com.cstudy.modulecommon.error.abstracts.ChoiceAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundChoiceWithQuestionAndNumber extends ChoiceAbstractException {

    public NotFoundChoiceWithQuestionAndNumber(Long questionId, int number) {
        super("Not Found Choice with questionId: " + questionId + " and number: " + number);
    }


    @Override
    public int getStatusCode() {
        return ErrorCode.NotFoundChoiceWithQuestionAndNumber.getErrorCode();
    }
}
