
package org.jaudiotagger.audio.flac.metadatablock;


public class MetadataBlock
{
    private MetadataBlockHeader mbh;
    private MetadataBlockData mbd;

    public MetadataBlock(MetadataBlockHeader mbh, MetadataBlockData mbd)
    {
        this.mbh = mbh;
        this.mbd = mbd;
    }

    public MetadataBlockHeader getHeader()
    {
        return mbh;
    }

    public MetadataBlockData getData()
    {
        return mbd;
    }

    public int getLength()
    {
        return MetadataBlockHeader.HEADER_LENGTH + mbh.getDataLength();
    }
}
