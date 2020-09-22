package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTSO2 extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTSO2()
    {
    }

    public FrameBodyTSO2(FrameBodyTSO2 body)
    {
        super(body);
    }


    public FrameBodyTSO2(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTSO2(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ALBUM_ARTIST_SORT_ORDER_ITUNES;
    }
}
