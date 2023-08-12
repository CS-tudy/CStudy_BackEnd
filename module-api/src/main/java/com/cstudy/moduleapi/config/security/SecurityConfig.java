package com.cstudy.moduleapi.config.security;


import com.cstudy.moduleapi.config.jwt.exception.CustomAccessDeniedHandler;
import com.cstudy.moduleapi.config.jwt.exception.CustomAuthenticationEntryPoint;
import com.cstudy.moduleapi.config.oauth.CustomOAuth2UserService;
import com.cstudy.moduleapi.config.oauth.OAuth2FailureHandler;
import com.cstudy.moduleapi.config.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;


@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationManagerConfig authenticationManagerConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .csrf().disable()
                .cors()
                .and()
                .apply(authenticationManagerConfig)
                .and()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/questions").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/workbook/update","/api/workbook/question/delete","/api/workbook/question/add","/api/workbook/create",
                        "/api/request/approve", "/api/question","/api/competition","/api/competition/question/add","/api/competition/question/delete")
                        .hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/upload","/api/request/mylist","/api/request/create","/api/questions/myquestion",
                        "/api/question/**", "/api/mypage", "/api/mypage/password","/api/competition/submit","/api/competition/join/**",
                        "/api/competition/result/**", "/api/competition/question/**")
                        .hasAnyAuthority("ROLE_CUSTOM", "ROLE_ADMIN")
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .mvcMatchers("/api/signup","/api/login","/api/logout","/email","/name","/api/email").permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .oauth2Login()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .userInfoEndpoint()
                .userService(oAuth2UserService);

        return http.build();
    }
}