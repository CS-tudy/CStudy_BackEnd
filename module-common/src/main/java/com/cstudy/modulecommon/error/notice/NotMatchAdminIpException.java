package com.cstudy.modulecommon.error.notice;


import com.cstudy.modulecommon.error.abstracts.RequestAbstractException;
import com.cstudy.modulecommon.error.enums.ErrorCode;

public class NotMatchAdminIpException extends RequestAbstractException {

  public NotMatchAdminIpException(Long id) {
    super("Not Equals Admin ID: " + id);
  }

  public NotMatchAdminIpException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public int getStatusCode() {
    return ErrorCode.NotMatchAdminIp.getErrorCode();
  }
}
