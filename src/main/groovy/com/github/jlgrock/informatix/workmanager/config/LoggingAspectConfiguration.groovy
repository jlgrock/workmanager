package com.github.jlgrock.informatix.workmanager.config;

import com.github.jlgrock.informatix.workmanager.aop.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableAspectJAutoProxy
class LoggingAspectConfiguration {

    @Bean
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    LoggingAspect loggingAspect() {
        return new LoggingAspect()
    }
}
