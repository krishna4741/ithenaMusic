package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTSOC extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTSOC()
    {
    }

    public FrameBodyTSOC(FrameBodyTSOC body)
    {
        super(body);
    }


    public FrameBodyTSOC(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTSOC(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_COMPOSER_SORT_ORDER_ITUNES;
    }
}
