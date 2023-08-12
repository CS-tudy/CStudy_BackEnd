package com.cstudy.modulecommon.error.request;


import com.cstudy.modulecommon.error.abstracts.RequestAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotFoundRequest extends RequestAbstractException {

  public NotFoundRequest(Long id) {
    super("Not Found Request With: " + id);
  }

  public NotFoundRequest(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public int getStatusCode() {
    return ErrorCode.NotFoundRequest.getErrorCode();
  }
}
