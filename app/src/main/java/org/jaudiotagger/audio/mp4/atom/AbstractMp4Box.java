package org.jaudiotagger.audio.mp4.atom;

import java.nio.ByteBuffer;


public class AbstractMp4Box
{
    protected Mp4BoxHeader header;
    protected ByteBuffer dataBuffer;


    public Mp4BoxHeader getHeader()
    {
        return header;
    }


    public ByteBuffer getData()
    {
        return dataBuffer;
    }


}
