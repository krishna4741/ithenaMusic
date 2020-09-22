
package org.jaudiotagger.audio.flac.metadatablock;


public class MetadataBlockDataPadding implements MetadataBlockData
{
    private int length;

    public MetadataBlockDataPadding(int length)
    {
        this.length = length;
    }

    public byte[] getBytes()
    {
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++)
        {
            data[i] = 0;
        }
        return data;
    }

    public int getLength()
    {
        return length;
    }
}
