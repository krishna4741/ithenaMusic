package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v23Frames;

import java.nio.ByteBuffer;


public class FrameBodyXSOA extends AbstractFrameBodyTextInfo implements ID3v23FrameBody
{

    public FrameBodyXSOA()
    {
    }

    public FrameBodyXSOA(FrameBodyXSOA body)
    {
        super(body);
    }


    public FrameBodyXSOA(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyXSOA(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_MUSICBRAINZ;
    }
}
