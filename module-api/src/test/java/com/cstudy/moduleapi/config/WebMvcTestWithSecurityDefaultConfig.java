package com.cstudy.moduleapi.config;

import com.cstudy.moduleapi.config.oauth.OAuth2FailureHandler;
import com.cstudy.moduleapi.config.oauth.OAuth2SuccessHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({
})
public class WebMvcTestWithSecurityDefaultConfig {
}
