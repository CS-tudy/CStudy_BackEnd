package com.cstudy.moduleapi.config.oauth;

import com.cstudy.moduleapi.application.member.MemberService;
import com.cstudy.moduleapi.dto.member.MemberLoginRequest;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.modulecommon.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Member member = (Member) oAuth2User.getAttributes().get("member");

        MemberLoginRequest loginRequest = MemberLoginRequest.builder()
                .email(member.getEmail())
                .build();
        MemberLoginResponse loginResponse = memberService.oauthLogin(member.getEmail());

        String redirectUri = "http://localhost:3000/oauth2/login";


        Cookie tokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(3600);
        response.addCookie(tokenCookie);

        Cookie refreshToken = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshToken.setPath("/");
        refreshToken.setMaxAge(3600);
        response.addCookie(refreshToken);


        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .build().toUriString();


        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
