package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v23Frames;

import java.nio.ByteBuffer;


public class FrameBodyXSOP extends AbstractFrameBodyTextInfo implements ID3v23FrameBody
{

    public FrameBodyXSOP()
    {
    }

    public FrameBodyXSOP(FrameBodyXSOP body)
    {
        super(body);
    }


    public FrameBodyXSOP(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyXSOP(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_ARTIST_SORT_ORDER_MUSICBRAINZ;
    }
}
