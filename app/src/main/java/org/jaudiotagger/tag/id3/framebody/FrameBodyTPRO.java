
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTPRO extends AbstractFrameBodyTextInfo implements ID3v24FrameBody
{

    public FrameBodyTPRO()
    {
    }

    public FrameBodyTPRO(FrameBodyTPRO body)
    {
        super(body);
    }


    public FrameBodyTPRO(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTPRO(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_PRODUCED_NOTICE;
    }
}