package com.github.jlgrock.informatix.workmanager.web.rest.dto

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.fasterxml.jackson.annotation.JsonCreator

class LoggerDTO {

    String name

    Level level

    public LoggerDTO(Logger logger) {
        name = logger.name
        level = logger.effectiveLevel
    }

    @JsonCreator
    public LoggerDTO() {
    }

}
