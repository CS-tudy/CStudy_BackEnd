package com.cstudy.moduleapi.config.jwt.exception;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Instanceof;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        HashMap<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("message", JwtExceptionCode.NO_AUTHORITY.getMessage());
        errorInfo.put("code", JwtExceptionCode.NO_AUTHORITY.getCode());

        Gson gson = new Gson();

        String responseJson = gson.toJson(errorInfo);

        response.getWriter().print(responseJson);
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        HashMap<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("code", HttpStatus.UNAUTHORIZED.value());
        errorInfo.put("message", JwtExceptionCode.NO_AUTHORITY.getMessage());

        Gson gson = new Gson();

        String responseJson = gson.toJson(errorInfo);

        response.getWriter().print(responseJson);
    }
}
