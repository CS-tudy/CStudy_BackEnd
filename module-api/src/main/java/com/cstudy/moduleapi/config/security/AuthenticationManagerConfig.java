package com.cstudy.moduleapi.config.security;


import com.cstudy.moduleapi.config.jwt.filter.JwtAuthenticationFilter;
import com.cstudy.moduleapi.config.jwt.provider.JwtAuthenticationProvider;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.modulecommon.repository.member.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final JwtTokenizer jwtTokenizer;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

        builder.addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager, memberRepository, jwtTokenizer),
                UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(jwtAuthenticationProvider);
    }
}
