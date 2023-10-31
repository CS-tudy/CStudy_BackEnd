package com.cstudy.moduleapi.application.member;


import com.cstudy.moduleapi.dto.alarm.response.AlarmResponseDto;
import com.cstudy.moduleapi.dto.member.*;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;

import javax.mail.MessagingException;
import java.util.concurrent.CompletableFuture;

public interface MemberService {
    MemberSignupResponse signUp(MemberSignupRequest request);
//    MemberSignupResponse signUp(MemberSignupRequest request, GeoLocationDto geoLocationDto);
    MemberLoginResponse login(MemberLoginRequest request);
    MyPageResponseDto getMyPage(LoginUserDto loginUserDto);
    void changePassword(MemberPasswordChangeRequest request, LoginUserDto loginUserDto);
    MemberSignupResponse signUpForTest(MemberSignupRequest request);
    CompletableFuture<String> sendEmail(String recipientEmail) throws MailException, MessagingException;
    Page<AlarmResponseDto>alarmList(LoginUserDto loginUserDto, Pageable pageable);
}
