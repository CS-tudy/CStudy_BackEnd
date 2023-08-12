package com.cstudy.moduleapi.config.jwt.filter;


import com.cstudy.moduleapi.config.jwt.exception.JwtExceptionCode;
import com.cstudy.moduleapi.config.jwt.token.JwtAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String token = "";
        try {
            token = getToken(request);
            if (StringUtils.hasText(token)) {
                getAuthentication(token);
            } else {
                extracted(request, JwtExceptionCode.NOT_FOUND_TOKEN, "Not found Token // token : {}", token, "throw new not found token exception");
            }
        } catch (NullPointerException | IllegalStateException e) {
            extracted(request, JwtExceptionCode.NOT_FOUND_TOKEN, "Not found Token // token : {}", token, "throw new not found token exception");
        } catch (SecurityException | MalformedJwtException e) {//JWT 토큰의 서명이나 암호화와 관련된 오류입니다. -> 시그니처 오류
            extracted(request, JwtExceptionCode.INVALID_TOKEN, "Invalid Token // token : {}", token, "throw new invalid token exception");
        } catch (ExpiredJwtException e) {//JWT 토큰의 만료 시간이 지났을 때 발생하는 오류입니다.
            extracted(request, JwtExceptionCode.EXPIRED_TOKEN, "EXPIRED Token // token : {}", token, "throw new expired token exception");
        } catch (UnsupportedJwtException e) {//지원되지 않는 JWT 토큰 형식이나 구조를 사용했을 때 발생하는 오류입니다.
            extracted(request, JwtExceptionCode.UNSUPPORTED_TOKEN, "Unsupported Token // token : {}", token, "throw new unsupported token exception");
        } catch (Exception e) {
            log.error("JwtFilter - doFilterInternal() 오류 발생");
            log.error("token : {}", token);
            log.error("Exception Message : {}", e.getMessage());
            //throw new BadCredentialsException("throw new exception");
        }
        filterChain.doFilter(request, response);
    }

    private static void extracted(HttpServletRequest request, JwtExceptionCode notFoundToken, String format, String token, String msg) {
        request.setAttribute("exception", notFoundToken.getCode());
        log.error(format, token);
        log.error("Set Request Exception Code : {}", request.getAttribute("exception"));
        //throw new BadCredentialsException(msg);
    }

    private void getAuthentication(String token) {
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate); // 현재 요청에서 언제든지 인증정보를 꺼낼 수 있도록 해준다.
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")){
            String[] arr = authorization.split(" ");
            return arr[1];
        }
        return null;
    }
}
