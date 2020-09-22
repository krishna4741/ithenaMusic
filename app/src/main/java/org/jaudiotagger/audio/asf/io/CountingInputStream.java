package org.jaudiotagger.audio.asf.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


class CountingInputStream extends FilterInputStream {


    private long markPos;


    private long readCount;


    public CountingInputStream(final InputStream stream) {
        super(stream);
        this.markPos = 0;
        this.readCount = 0;
    }


    private synchronized void bytesRead(final long amountRead) {
        if (amountRead >= 0)
        {
            this.readCount += amountRead;
        }
    }


    public synchronized long getReadCount() {
        return this.readCount;
    }


    @Override
    public synchronized void mark(final int readlimit) {
        super.mark(readlimit);
        this.markPos = this.readCount;
    }


    @Override
    public int read() throws IOException {
        final int result = super.read();
        bytesRead(1);
        return result;
    }


    @Override
    public int read(final byte[] destination, final int off, final int len)
            throws IOException {
        final int result = super.read(destination, off, len);
        bytesRead(result);
        return result;
    }


    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        synchronized (this) {
            this.readCount = this.markPos;
        }
    }


    @Override
    public long skip(final long amount) throws IOException {
        final long skipped = super.skip(amount);
        bytesRead(skipped);
        return skipped;
    }

}
