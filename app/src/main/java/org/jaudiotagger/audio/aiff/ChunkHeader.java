package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ChunkHeader {

    private long _size;              // This does not include the 8 bytes of header
    private String _chunkID;         // 4-character ID of the chunk
    

    public ChunkHeader ()
    {
    }
    
    

    public boolean readHeader (RandomAccessFile raf) throws IOException
    {
        StringBuffer id = new StringBuffer(4);
        for (int i = 0; i < 4; i++) {
            int ch = raf.read();
            if (ch < 32) {
                String hx = Integer.toHexString (ch);
                if (hx.length () < 2) {
                    hx = "0" + hx;
                }
                return false;
            }
            id.append((char) ch);
        }
        _chunkID = id.toString ();
        _size = AiffUtil.readUINT32 (raf); 
        return true;
    }
    
    

    public void setID (String id)
    {
        _chunkID = id;
    }
    

    public String getID ()
    {
        return _chunkID;
    }
    

    public long getSize ()
    {
        return _size;
    }
}
