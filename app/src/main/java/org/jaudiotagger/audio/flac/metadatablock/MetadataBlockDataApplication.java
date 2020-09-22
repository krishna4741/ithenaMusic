
package org.jaudiotagger.audio.flac.metadatablock;

import java.io.IOException;
import java.io.RandomAccessFile;



public class MetadataBlockDataApplication implements MetadataBlockData
{

    private byte[] data;

    public MetadataBlockDataApplication(MetadataBlockHeader header, RandomAccessFile raf) throws IOException
    {
        data = new byte[header.getDataLength()];
        raf.readFully(data);
    }

    public byte[] getBytes()
    {
        return data;
    }

    public int getLength()
    {
        return data.length;
    }
}
