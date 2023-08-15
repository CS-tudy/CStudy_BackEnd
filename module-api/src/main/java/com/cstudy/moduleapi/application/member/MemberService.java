package com.cstudy.moduleapi.application.member;


import com.cstudy.moduleapi.dto.member.*;
import com.cstudy.modulecommon.domain.member.Member;
import org.springframework.mail.MailException;

import javax.mail.MessagingException;

public interface MemberService {
    MemberSignupResponse signUp(MemberSignupRequest request);


    MemberLoginResponse login(MemberLoginRequest request);

    Member oauthSignUp(String email, String name);

    MemberLoginResponse oauthLogin(String email);

    MyPageResponseDto getMyPage(Long id);

    void changePassword(MemberPasswordChangeRequest request, Long id);

    MemberSignupResponse signUpForTest(MemberSignupRequest request);

    String sendEmail(String recipientEmail) throws MailException, MessagingException;
}
