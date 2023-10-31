package com.cstudy.moduleapi.config.jwt.exception;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        String exception = String.valueOf(request.getAttribute("exception"));

        if(exception == null) {
            log.error("entry point >> exception is null");
            setResponse(
                    response,
                    JwtExceptionCode.NOT_FOUND_TOKEN
            );
        }
        else if(exception.equals(JwtExceptionCode.INVALID_TOKEN.getCode())) {
            log.error("잘못된 토큰인 경우");
            setResponse(
                    response,
                    JwtExceptionCode.INVALID_TOKEN
            );
        }
        else if(exception.equals(JwtExceptionCode.EXPIRED_TOKEN.getCode())) {
            log.error("토큰 만료된 경우");
            setResponse(
                    response,
                    JwtExceptionCode.EXPIRED_TOKEN
            );
        }
        else if(exception.equals(JwtExceptionCode.UNSUPPORTED_TOKEN.getCode())) {
            log.error("지원되지 않는 토큰인 경우");
            setResponse(
                    response,
                    JwtExceptionCode.UNSUPPORTED_TOKEN
            );
        }
        else if (exception.equals(JwtExceptionCode.NOT_FOUND_TOKEN.getCode())) {
            log.error("Headers에 토큰 형식의 값 찾을 수 없음");
            setResponse(
                    response,
                    JwtExceptionCode.NOT_FOUND_TOKEN
            );
        }
        else {
            setResponse(
                    response,
                    JwtExceptionCode.UNKNOWN_ERROR
            );
        }
    }

    private void setResponse(HttpServletResponse response, JwtExceptionCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        HashMap<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("code", "401");
        errorInfo.put("message", exceptionCode.getMessage());

        Gson gson = new Gson();

        String responseJson = gson.toJson(errorInfo);

        response.getWriter().print(responseJson);
    }
}
