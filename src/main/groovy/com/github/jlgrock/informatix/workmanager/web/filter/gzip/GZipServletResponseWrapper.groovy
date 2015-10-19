package com.github.jlgrock.informatix.workmanager.web.filter.gzip
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper
import java.util.zip.GZIPOutputStream

class GZipServletResponseWrapper extends HttpServletResponseWrapper {

    GZipServletOutputStream gzipOutputStream = null
    PrintWriter printWriter = null
    boolean disableFlushBuffer = false

    GZipServletResponseWrapper(HttpServletResponse response, GZIPOutputStream gzout)
            throws IOException {
        super(response)
        gzipOutputStream = new GZipServletOutputStream(gzout)
    }

    void close() throws IOException {

        //PrintWriter.close does not throw exceptions. Thus, the call does not need
        //be inside a try-catch block.
        if (printWriter != null) {
            printWriter.close()
        }

        if (gzipOutputStream != null) {
            gzipOutputStream.close()
        }
    }

    /**
     * Flush OutputStream or PrintWriter
     *
     * @throws IOException
     */
    @Override
    void flushBuffer() throws IOException {

        //PrintWriter.flush() does not throw exception
        if (printWriter != null) {
            printWriter.flush()
        }

        if (gzipOutputStream != null) {
            gzipOutputStream.flush()
        }

        // doing this might leads to response already committed exception
        // when the PageInfo has not yet built but the buffer already flushed
        // Happens in Weblogic when a servlet forward to a JSP page and the forward
        // method trigger a flush before it forwarded to the JSP
        // disableFlushBuffer for that purpose is 'true' by default
        if (!disableFlushBuffer) {
            super.flushBuffer()
        }
    }

    @Override
    ServletOutputStream getOutputStream() throws IOException {
        if (printWriter != null) {
            throw new IllegalStateException(
                    "PrintWriter obtained already - cannot get OutputStream")
        }

        gzipOutputStream
    }

    @Override
    PrintWriter getWriter() throws IOException {
        if (printWriter == null) {
            gzipOutputStream = new GZipServletOutputStream(response.outputStream)
            printWriter = new PrintWriter(new OutputStreamWriter(gzipOutputStream, response.characterEncoding), true)
        }

        printWriter
    }


    @Override
    public void setContentLength(int length) {
        //ignore, since content length of zipped content
        //does not match content length of unzipped content.
    }

    /**
     * Flushes all the streams for this response.
     */
    public void flush() throws IOException {
        if (printWriter != null) {
            printWriter.flush()
        }

        if (gzipOutputStream != null) {
            gzipOutputStream.flush()
        }
    }
}
