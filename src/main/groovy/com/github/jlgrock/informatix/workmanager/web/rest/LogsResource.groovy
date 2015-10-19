package com.github.jlgrock.informatix.workmanager.web.rest

import ch.qos.logback.classic.LoggerContext
import com.codahale.metrics.annotation.Timed
import com.github.jlgrock.informatix.workmanager.web.rest.dto.LoggerDTO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/api")
public class LogsResource extends AbstractSpringResource {

    @RequestMapping(value = "/logs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LoggerDTO> getList() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory()
        return context.getLoggerList().collect {
            new LoggerDTO(it)
        }
    }

    @RequestMapping(value = "/logs",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Timed
    public void changeLevel(@RequestBody LoggerDTO jsonLogger) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory()
        context.getLogger(jsonLogger.name).level = jsonLogger.level
    }
}
