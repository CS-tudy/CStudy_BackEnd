package com.cstudy.modulecommon.error.notice;


import com.cstudy.modulecommon.error.abstracts.RequestAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundNoticeId extends RequestAbstractException {

  public NotFoundNoticeId(Long id) {
    super("Not Found Notice Id: " + id);
  }

  public NotFoundNoticeId(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public int getStatusCode() {
    return ErrorCode.NotFoundNoticeId.getErrorCode();
  }
}
