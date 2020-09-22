package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;


public abstract class TextChunk extends Chunk {

    private AiffAudioHeader aiffHeader;
    
    protected String chunkText;
    

    public TextChunk (
            ChunkHeader hdr, 
            RandomAccessFile raf)
    {
        super (raf, hdr);
    }


    @Override
    public boolean readChunk() throws IOException {
        
        byte[] buf = new byte[(int) bytesLeft];
        raf.read(buf);
        chunkText = new String (buf, "ISO-8859-1");
        return true;
    }

}
