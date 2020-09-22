package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;

public class NameChunk extends TextChunk {

    private AiffAudioHeader aiffHeader;
    

    public NameChunk (
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
        aiffHeader.setName (chunkText);
        return true;
    }
}
