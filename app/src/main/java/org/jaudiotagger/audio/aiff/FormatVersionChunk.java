package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.aiff.AiffTagFieldKey;

public class FormatVersionChunk extends Chunk {
    
    private AiffAudioHeader aiffHeader;
    

    public FormatVersionChunk (
            ChunkHeader hdr, 
            RandomAccessFile raf,
            AiffAudioHeader aHdr)
    {
        super (raf, hdr);
        aiffHeader = aHdr;
    }
    

    public boolean readChunk () throws IOException
    {
        long rawTimestamp = Utils.readUint32(raf);
        // The timestamp is in seconds since January 1, 1904.
        // We must convert to Java time.
        Date timestamp = AiffUtil.timestampToDate (rawTimestamp);
        aiffHeader.setTimestamp(timestamp);
        return true;
    }

}
