package com.cstudy.moduleapi.application.member;


import com.cstudy.moduleapi.dto.member.DuplicateResponseDto;
import com.cstudy.moduleapi.dto.member.DuplicateType;

public interface DuplicateService {
    DuplicateResponseDto signupDivisionDuplicateCheck(String type, String value);
    DuplicateType getType();
}
