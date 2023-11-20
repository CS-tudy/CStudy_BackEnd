package com.cstudy.moduleapi.config.security;


import com.cstudy.moduleapi.config.argumentResolver.IfLoginArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    SecurityConfig에서 처리

    @Value("${front.baseURL}")
    String FRONT_BASE_URL;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(FRONT_BASE_URL)
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("Set-Cookie")
                .allowedMethods("GET", "POST", "PATCH", "PUT", "OPTIONS", "DELETE");
//        registry.addMapping("/**")
//                .allowedOrigins(FRONT_BASE_URL)
//                .allowCredentials(true)
//                .allowedHeaders("*")
//                .exposedHeaders("Set-Cookie")
//                .allowedMethods(String.valueOf(List.of(HttpMethod.HEAD.name(),
//                        HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
//                        HttpMethod.DELETE.name(), HttpMethod.PATCH.name(),
//                        HttpMethod.OPTIONS.name())));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new IfLoginArgumentResolver());
    }
}
