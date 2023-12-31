package com.cstudy.moduleapi.config.security;


import com.cstudy.moduleapi.config.jwt.exception.CustomAccessDeniedHandler;
import com.cstudy.moduleapi.config.jwt.filter.JwtAuthenticationFilter;
import com.cstudy.moduleapi.config.jwt.provider.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig extends AbstractHttpConfigurer<AuthenticationManagerConfig, HttpSecurity> {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

        builder.addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager, customAccessDeniedHandler),
                UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(jwtAuthenticationProvider);
    }
}
