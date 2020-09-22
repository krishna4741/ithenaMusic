package org.jaudiotagger.audio.mp4.atom;

import org.jaudiotagger.audio.exceptions.CannotReadException;

import java.nio.ByteBuffer;


public class Mp4StsdBox extends AbstractMp4Box
{
    public static final int VERSION_FLAG_POS = 0;
    public static final int OTHER_FLAG_POS = 1;
    public static final int NO_OF_DESCRIPTIONS_POS = 4;

    public static final int VERSION_FLAG_LENGTH = 1;
    public static final int OTHER_FLAG_LENGTH = 3;
    public static final int NO_OF_DESCRIPTIONS_POS_LENGTH = 4;


    public Mp4StsdBox(Mp4BoxHeader header, ByteBuffer dataBuffer)
    {
        this.header = header;
        this.dataBuffer = dataBuffer;
    }

    public void processData() throws CannotReadException
    {
        //Skip the data
        dataBuffer.position(dataBuffer.position() + VERSION_FLAG_LENGTH + OTHER_FLAG_LENGTH + NO_OF_DESCRIPTIONS_POS_LENGTH);
    }
}
