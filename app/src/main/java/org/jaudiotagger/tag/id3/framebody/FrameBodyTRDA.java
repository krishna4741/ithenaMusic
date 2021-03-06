
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v23Frames;

import java.nio.ByteBuffer;


public class FrameBodyTRDA extends AbstractFrameBodyTextInfo implements ID3v23FrameBody
{

    public FrameBodyTRDA()
    {
    }

    public FrameBodyTRDA(FrameBodyTRDA body)
    {
        super(body);
    }


    public FrameBodyTRDA(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTRDA(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_TRDA;
    }
}
