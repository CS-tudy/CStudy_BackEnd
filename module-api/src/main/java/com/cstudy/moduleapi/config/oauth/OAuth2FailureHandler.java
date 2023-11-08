package com.cstudy.moduleapi.config.oauth;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${front.baseURL}")
    String FRONT_BASE_URL;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        log.warn("oauth login fail");

        log.warn("OAuth exception : {}", exception.getMessage());

        log.warn("FRONT URL : {}", FRONT_BASE_URL);

        log.warn("OAuth exception Cause :", exception.getCause());

        String redirectUri = "http://localhost:3000/oauth2/fail";
        response.sendRedirect(
                redirectUri +
                        "?login_success=" + false);
    }
}
