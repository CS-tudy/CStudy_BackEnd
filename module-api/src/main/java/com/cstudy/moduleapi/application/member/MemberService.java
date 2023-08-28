package com.cstudy.moduleapi.application.member;


import com.cstudy.moduleapi.dto.member.*;
import com.cstudy.modulecommon.domain.member.Member;
import org.springframework.mail.MailException;

import javax.mail.MessagingException;
import java.util.concurrent.CompletableFuture;

public interface MemberService {
    MemberSignupResponse signUp(MemberSignupRequest request);
//    MemberSignupResponse signUp(MemberSignupRequest request, GeoLocationDto geoLocationDto);
    MemberLoginResponse login(MemberLoginRequest request);
    Member oauthSignUp(String email, String name);
    MemberLoginResponse oauthLogin(String email);
    MyPageResponseDto getMyPage(Long id);
    void changePassword(MemberPasswordChangeRequest request, Long id);
    MemberSignupResponse signUpForTest(MemberSignupRequest request);
    CompletableFuture<String> sendEmail(String recipientEmail) throws MailException, MessagingException;
}
