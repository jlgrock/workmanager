package com.github.jlgrock.informatix.workmanager.web.filter.gzip

import javax.servlet.ServletOutputStream
import javax.servlet.WriteListener

class GZipServletOutputStream extends ServletOutputStream {
    OutputStream stream

    GZipServletOutputStream(OutputStream output)
            throws IOException {
        super()
        stream = output
    }

    @Override
    void close() throws IOException {
        stream.close()
    }

    @Override
    void flush() throws IOException {
        stream.flush()
    }

    @Override
    void write(byte[] b) throws IOException {
        stream.write(b)
    }

    @Override
    void write(byte[] b, int off, int len) throws IOException {
        stream.write(b, off, len)
    }

    @Override
    void write(int b) throws IOException {
        stream.write(b)
    }

    @Override
    boolean isReady() {
        true
    }

    @Override
    void setWriteListener(WriteListener listener) {
        //NOOP
    }
}
