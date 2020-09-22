package org.jaudiotagger.audio.mp4.atom;

import org.jaudiotagger.audio.exceptions.CannotReadException;

import java.nio.ByteBuffer;


public class Mp4DrmsBox extends AbstractMp4Box
{

    public Mp4DrmsBox(Mp4BoxHeader header, ByteBuffer dataBuffer)
    {
        this.header = header;
        this.dataBuffer = dataBuffer;
    }


    public void processData() throws CannotReadException
    {
        while (dataBuffer.hasRemaining())
        {
            byte next = dataBuffer.get();
            if (next != (byte) 'e')
            {
                continue;
            }

            //Have we found esds identifier, if so adjust buffer to start of esds atom
            ByteBuffer tempBuffer = dataBuffer.slice();
            if ((tempBuffer.get() == (byte) 's') & (tempBuffer.get() == (byte) 'd') & (tempBuffer.get() == (byte) 's'))
            {
                dataBuffer.position(dataBuffer.position() - 1 - Mp4BoxHeader.OFFSET_LENGTH);
                return;
            }
        }
    }
}
