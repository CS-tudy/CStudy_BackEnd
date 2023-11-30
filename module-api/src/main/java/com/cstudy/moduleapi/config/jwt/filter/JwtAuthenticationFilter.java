package com.cstudy.moduleapi.config.jwt.filter;


import com.cstudy.moduleapi.config.jwt.exception.CustomAccessDeniedHandler;
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
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token="";
        try {
            token = getToken(request);
            if (StringUtils.hasText(token)) {
                getAuthentication(token);
            }
            filterChain.doFilter(request, response);
        }
        catch (NullPointerException | IllegalStateException e) {
            request.setAttribute("exception", JwtExceptionCode.NOT_FOUND_TOKEN.getCode());
            customAccessDeniedHandler.handle(request,response);
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.INVALID_TOKEN.getCode());
            customAccessDeniedHandler.handle(request,response);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.EXPIRED_TOKEN.getCode());
            customAccessDeniedHandler.handle(request,response);
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
            customAccessDeniedHandler.handle(request,response);
        } catch (Exception e) {
            customAccessDeniedHandler.handle(request,response);
        }
    }


    private void getAuthentication(String token) {
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext()
                .setAuthentication(authenticate); // 현재 요청에서 언제든지 인증정보를 꺼낼 수 있도록 해준다.
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