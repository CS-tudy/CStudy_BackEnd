package com.cstudy.moduleapi.config.jpaPackage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaRepositories(basePackages =  "com.cstudy.modulecommon.repository")
@EntityScan(basePackages = "com.cstudy.modulecommon.domain")
@EnableMongoRepositories(basePackages = "com.cstudy.modulecommon.repository.reviewNote")
@Configuration
public class JpaAndEntityPackagePathConfig {
}
