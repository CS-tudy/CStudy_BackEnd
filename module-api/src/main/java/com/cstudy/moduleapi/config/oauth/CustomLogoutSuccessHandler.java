package com.cstudy.moduleapi.config.oauth;

import com.cstudy.moduleapi.application.refershToken.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RefreshTokenService refreshTokenService;

    public CustomLogoutSuccessHandler(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        String refreshToken = request.getParameter("refreshToken");

        log.info("==================================");
        log.info("refreshToken: {}" , refreshToken);
        log.info("==================================");

        refreshTokenService.deleteRefreshToken(refreshToken);

        response.sendRedirect("- ");
    }
}