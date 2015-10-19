package com.github.jlgrock.informatix.workmanager.web.rest

import com.github.jlgrock.informatix.workmanager.web.rest.errors.ErrorResponse
import com.github.jlgrock.informatix.workmanager.web.rest.errors.WebException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
/**
 *
 */
class AbstractSpringResource {
    private static Logger LOGGER = LoggerFactory.getLogger(AbstractSpringResource.class)

    @ExceptionHandler(WebException.class)
    protected ResponseEntity<? extends ErrorResponse> processWebException(WebException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.message, e.httpStatus);
        LOGGER.debug(errorResponse.toString(), e)
        return new ResponseEntity<ErrorResponse>(errorResponse, errorResponse.httpStatus);
    }
}
