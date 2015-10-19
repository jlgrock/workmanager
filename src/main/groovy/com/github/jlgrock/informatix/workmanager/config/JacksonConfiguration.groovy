package com.github.jlgrock.informatix.workmanager.config

import ch.qos.logback.classic.Level
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleModule
import com.github.jlgrock.informatix.workmanager.domain.util.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
public class JacksonConfiguration {

    @Bean
    Module localDateTimeTimeModule() {
        SimpleModule module = new SimpleModule("EnhancedLocalDateTimeModule", new Version(0,1,0, "alpha"))
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer())
        module
    }

    @Bean
    Module localDateModule() {
        SimpleModule module = new SimpleModule("EnhancedLocalDateModule", new Version(0,1,0, "alpha"))
        module.addSerializer(LocalDate.class, new LocalDateSerializer())
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer())
        module
    }

    @Bean
    Module levelModule() {
        SimpleModule module = new SimpleModule("LevelModule", new Version(0,1,0, "alpha"))
        module.addSerializer(Level.class, new LevelSerializer())
        module.addDeserializer(Level.class, new LevelDeserializer())
        module
    }

}
