package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CopyrightChunk extends TextChunk {

    private AiffAudioHeader aiffHeader;
    

    public CopyrightChunk (
            ChunkHeader hdr, 
            RandomAccessFile raf,
            AiffAudioHeader aHdr)
    {
        super (hdr, raf);
        aiffHeader = aHdr;
    }
    
    @Override
    public boolean readChunk() throws IOException {
        if (!super.readChunk ()) {
            return false;
        }
        aiffHeader.setCopyright (chunkText);
        return true;
    }
    
}
