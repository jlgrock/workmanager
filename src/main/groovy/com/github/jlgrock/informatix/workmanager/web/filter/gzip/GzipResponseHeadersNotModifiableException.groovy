package com.github.jlgrock.informatix.workmanager.web.filter.gzip

import javax.servlet.ServletException

class GzipResponseHeadersNotModifiableException extends ServletException {

    GzipResponseHeadersNotModifiableException(String message) {
        super(message)
    }
}
