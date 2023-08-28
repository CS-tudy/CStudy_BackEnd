package com.cstudy.moduleapi.argumentResolver;

import com.cstudy.moduleapi.config.jwt.token.JwtAuthenticationToken;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Collection;

public class IfLoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(IfLogin.class) != null
                && parameter.getParameterType() == LoginUserDto.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = null;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception ex) {
            return null;
        }
        if (authentication == null) {
            return null;
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        LoginUserDto loginUserDto = new LoginUserDto();

        Object principal = jwtAuthenticationToken.getPrincipal();
        if (principal == null)
            return null;

        LoginInfoDto loginInfoDto = (LoginInfoDto) principal;
        loginUserDto.setMemberId(loginInfoDto.getMemberId());
        loginUserDto.setRoles(loginInfoDto.getRoles());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            String role = grantedAuthority.getAuthority();
            loginUserDto.addRole(role);
        }

        return loginUserDto;
    }
}

