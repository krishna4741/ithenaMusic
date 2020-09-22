package org.jaudiotagger.audio.asf.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;


public final class RandomAccessFileOutputStream extends OutputStream {


    private final RandomAccessFile targetFile;


    public RandomAccessFileOutputStream(final RandomAccessFile target) {
        super();
        this.targetFile = target;
    }


    @Override
    public void write(final byte[] bytes, final int off, final int len)
            throws IOException {
        this.targetFile.write(bytes, off, len);
    }


    @Override
    public void write(final int toWrite) throws IOException {
        this.targetFile.write(toWrite);
    }

}
