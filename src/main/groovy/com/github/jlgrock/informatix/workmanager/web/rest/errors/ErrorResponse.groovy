package com.github.jlgrock.informatix.workmanager.web.rest.errors

import groovy.transform.ToString
import org.springframework.http.HttpStatus

/**
 *
 */
@ToString
class ErrorResponse {
    HttpStatus httpStatus
    String errorMsg

    ErrorResponse(String errorMsgIn, HttpStatus httpStatusIn) {
        httpStatus = httpStatusIn
        errorMsg = errorMsgIn
    }
}
