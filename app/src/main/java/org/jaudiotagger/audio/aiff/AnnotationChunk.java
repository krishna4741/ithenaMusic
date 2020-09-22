package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;

public class AnnotationChunk extends TextChunk {

    private AiffAudioHeader aiffHeader;
    

    public AnnotationChunk (
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
        aiffHeader.addAnnotation (chunkText);
        return true;
    }

}
