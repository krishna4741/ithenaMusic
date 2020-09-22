package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;


public class AiffInputStream extends InputStream {

    private final static int BUFSIZE = 2048;

    private RandomAccessFile raf;
    

    private byte[] fileBuf;
    

    private int fileBufSize;
    

    private int fileBufOffset;
    

    private boolean eof;
    
    public AiffInputStream (RandomAccessFile raf) {
        this.raf = raf;
        eof = false;
        fileBuf = new byte[BUFSIZE];
        fileBufSize = 0;
        fileBufOffset = 0;
    }
    
    
    @Override
    public int read() throws IOException {
        for (;;) {
            if (eof) {
                return -1;
            }
            if (fileBufOffset < fileBufSize) {
                return ((int) fileBuf[fileBufOffset++]) & 0XFF;
            }
            else {
                fillBuf ();
            }
        }
    }
    

    

    private void fillBuf () throws IOException  {
        int bytesRead = raf.read (fileBuf, 0, BUFSIZE);
        fileBufOffset = 0;
        fileBufSize = bytesRead;
        if (fileBufSize == 0) {
            eof = true;
        }
    }

}
