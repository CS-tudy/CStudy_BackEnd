package com.cstudy.modulecommon.error.request;


import com.cstudy.modulecommon.error.abstracts.RequestAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotMathRequestAuthCheckAop extends RequestAbstractException {

  public NotMathRequestAuthCheckAop(Long id) {
    super("요청 문제 request의 권한이 일치하지 않습니다 :  " + id);
  }

  public NotMathRequestAuthCheckAop(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public int getStatusCode() {
    return ErrorCode.NotFoundRequest.getErrorCode();
  }
}
