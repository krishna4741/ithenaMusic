package org.jaudiotagger.audio.mp4.atom;

import java.nio.ByteBuffer;


public class NullPadding extends Mp4BoxHeader
{

    public NullPadding(long startPosition,long fileSize)
    {
        setFilePos(startPosition);
        length=((int)(fileSize - startPosition));
    }
}
