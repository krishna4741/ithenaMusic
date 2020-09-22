
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTSRC extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTSRC()
    {
    }

    public FrameBodyTSRC(FrameBodyTSRC body)
    {
        super(body);
    }


    public FrameBodyTSRC(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTSRC(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ISRC;
    }
}