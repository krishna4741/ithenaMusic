package org.jaudiotagger.audio.asf.io;

import java.io.IOException;
import java.io.OutputStream;


public class CountingOutputstream extends OutputStream {


    private long count = 0;


    private final OutputStream wrapped;


    public CountingOutputstream(final OutputStream outputStream) {
        super();
        assert outputStream != null;
        this.wrapped = outputStream;
    }


    @Override
    public void close() throws IOException {
        this.wrapped.close();
    }


    @Override
    public void flush() throws IOException {
        this.wrapped.flush();
    }


    public long getCount() {
        return this.count;
    }


    @Override
    public void write(final byte[] bytes) throws IOException {
        this.wrapped.write(bytes);
        this.count += bytes.length;
    }


    @Override
    public void write(final byte[] bytes, final int off, final int len)
            throws IOException {
        this.wrapped.write(bytes, off, len);
        this.count += len;
    }


    @Override
    public void write(final int toWrite) throws IOException {
        this.wrapped.write(toWrite);
        this.count++;
    }

}
