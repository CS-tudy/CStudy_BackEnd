package com.cstudy.moduleapi.config.oauth;

import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${front.baseURL}")
    String FRONT_BASE_URL;

    private final MemberRepository memberRepository;
    private final JwtTokenizer jwtTokenizer;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        log.info("oAuth2User {}", oAuth2User.getName());

        final String finalEmail;
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if (kakaoAccount == null) {
            finalEmail = (String) oAuth2User.getAttributes().get("email");
        } else {
            finalEmail = (String) kakaoAccount.get("email");
        }

        Member user = memberRepository.findByEmail(finalEmail)
                .orElseThrow(() -> new NotFoundMemberEmail(finalEmail));

        String access = jwtTokenizer.createAccessToken(
                user.getId(),
                user.getEmail(),
                List.of(RoleEnum.CUSTOM.getRoleName())
        );
        String refresh = jwtTokenizer.createRefreshToken(
                user.getId(),
                user.getEmail(),
                List.of(RoleEnum.CUSTOM.getRoleName())
        );

        String redirectUri = FRONT_BASE_URL+"/oauth2/login";

        Cookie accessToken = new Cookie("accessToken", access);
        accessToken.setHttpOnly(true);
        accessToken.setPath("/");
        accessToken.setMaxAge(1800); // 30 minutes
        response.addCookie(accessToken);

        Cookie refreshToken = new Cookie("refreshToken", refresh);
        accessToken.setHttpOnly(true);
        refreshToken.setPath("/");
        refreshToken.setMaxAge(604800); // 7 days
        response.addCookie(refreshToken);

        log.info("OAuth 성공");

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .build().toUriString();


        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
