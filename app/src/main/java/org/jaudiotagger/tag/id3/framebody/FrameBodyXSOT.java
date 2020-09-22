package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v23Frames;

import java.nio.ByteBuffer;


public class FrameBodyXSOT extends AbstractFrameBodyTextInfo implements ID3v23FrameBody
{

    public FrameBodyXSOT()
    {
    }

    public FrameBodyXSOT(FrameBodyXSOT body)
    {
        super(body);
    }


    public FrameBodyXSOT(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyXSOT(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_TITLE_SORT_ORDER_MUSICBRAINZ;
    }
}
