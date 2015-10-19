package com.github.jlgrock.informatix.workmanager.config.metrics;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
class JHipsterHealthIndicatorConfiguration {

    @Inject
    JavaMailSenderImpl javaMailSender;

    @Inject
    DataSource dataSource;

    @Bean
    HealthIndicator dbHealthIndicator() {
        new DatabaseHealthIndicator(dataSource);
    }

    @Bean
    HealthIndicator mailHealthIndicator() {
        new JavaMailHealthIndicator(javaMailSender);
    }
}
