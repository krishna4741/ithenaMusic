package org.jaudiotagger.audio.aiff;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;




public abstract class Chunk {

    protected long bytesLeft;
    protected RandomAccessFile raf;


    public Chunk (RandomAccessFile raf, ChunkHeader hdr)
    {
        this.raf = raf;
        bytesLeft = hdr.getSize ();
    }
    
    

    public abstract boolean readChunk () throws IOException;
    

    protected String byteBufString (byte[] b)
    {
        StringBuffer sb = new StringBuffer (b.length);
        for (int i = 0; i < b.length; i++) {
            byte c = b[i];
            if (c == 0) {
                // Terminate when we see a null
                break;
            }
            sb.append((char) c);
        }
        return sb.toString ();
    }
}
