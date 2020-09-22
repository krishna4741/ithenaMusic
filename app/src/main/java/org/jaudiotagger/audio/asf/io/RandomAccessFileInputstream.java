package org.jaudiotagger.audio.asf.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;


public final class RandomAccessFileInputstream extends InputStream {


    private final RandomAccessFile source;


    public RandomAccessFileInputstream(final RandomAccessFile file) {
        super();
        if (file == null) {
            throw new IllegalArgumentException("null");
        }
        this.source = file;
    }


    @Override
    public int read() throws IOException {
        return this.source.read();
    }


    @Override
    public int read(final byte[] buffer, final int off, final int len)
            throws IOException {
        return this.source.read(buffer, off, len);
    }


    @Override
    public long skip(final long amount) throws IOException {
        if (amount < 0) {
            throw new IllegalArgumentException("invalid negative value");
        }
        long left = amount;
        while (left > Integer.MAX_VALUE) {
            this.source.skipBytes(Integer.MAX_VALUE);
            left -= Integer.MAX_VALUE;
        }
        return this.source.skipBytes((int) left);
    }

}
