
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;



public class FrameBodyTIT1 extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTIT1()
    {
    }

    public FrameBodyTIT1(FrameBodyTIT1 body)
    {
        super(body);
    }


    public FrameBodyTIT1(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTIT1(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_CONTENT_GROUP_DESC;
    }
}