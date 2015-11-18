package com.github.jlgrock.informatix.workmanager.web.filter.gzip

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.zip.GZIPOutputStream

class GZipServletFilter implements Filter {

    Logger log = LoggerFactory.getLogger(GZipServletFilter.class)

    @Override
    void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to initialize
    }

    @Override
    void destroy() {
        // Nothing to destroy
    }

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request
        HttpServletResponse httpResponse = (HttpServletResponse) response

        if (!isIncluded(httpRequest) && acceptsGZipEncoding(httpRequest) && !response.isCommitted()) {
            // Client accepts zipped content
            if (log.traceEnabled) {
                log.trace("{} Written with gzip compression", httpRequest.getRequestURL())
            }

            // Create a gzip stream
            final ByteArrayOutputStream compressed = new ByteArrayOutputStream()
            final GZIPOutputStream gzout = new GZIPOutputStream(compressed)

            // Handle the request
            final GZipServletResponseWrapper wrapper = new GZipServletResponseWrapper(httpResponse, gzout)
            wrapper.setDisableFlushBuffer(true)
            chain.doFilter(request, wrapper)
            wrapper.flush()

            gzout.close()

            // double check one more time before writing out
            // repsonse might have been committed due to error
            if (response.committed) {
                return
            }

            // return on these special cases when content is empty or unchanged
            if([HttpServletResponse.SC_NO_CONTENT, HttpServletResponse.SC_RESET_CONTENT, HttpServletResponse.SC_NOT_MODIFIED].contains(wrapper.status)) {
                return
            }

            // Saneness checks
            byte[] compressedBytes = compressed.toByteArray()
            boolean shouldGzippedBodyBeZero = GZipResponseUtil.shouldGzippedBodyBeZero(compressedBytes, httpRequest)
            boolean shouldBodyBeZero = GZipResponseUtil.shouldBodyBeZero(httpRequest, wrapper.status)
            if (shouldGzippedBodyBeZero || shouldBodyBeZero) {
                // No reason to add GZIP headers or write body if no content was written or status code specifies no
                // content
                response.contentLength = 0
                return
            }

            // Write the zipped body
            GZipResponseUtil.addGzipHeader(httpResponse)

            response.contentLength = compressedBytes.length

            response.outputStream.write(compressedBytes)

        } else {
            // Client does not accept zipped content - don't bother zipping
            if (log.traceEnabled) {
                log.trace("{} Written without gzip compression because the request does not accept gzip", httpRequest.getRequestURL())
            }
            chain.doFilter(request, response)
        }
    }

    /**
     * Checks if the request uri is an include. These cannot be gzipped.
     */
    private boolean isIncluded(final HttpServletRequest request) {
        String uri = (String) request.getAttribute("javax.servlet.include.request_uri")
        boolean includeRequest = !(uri == null)
        if (includeRequest && log.debugEnabled) {
            log.debug("{} resulted in an include request. This is unusable, because"
                    + "the response will be assembled into the overrall response. Not gzipping.",
                    request.requestURL)
        }
        return includeRequest
    }

    private boolean acceptsGZipEncoding(HttpServletRequest httpRequest) {
        String acceptEncoding = httpRequest.getHeader("Accept-Encoding");
        return acceptEncoding != null && acceptEncoding.contains("gzip")
    }
}
