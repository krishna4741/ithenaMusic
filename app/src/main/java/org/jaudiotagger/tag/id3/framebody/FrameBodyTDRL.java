
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTDRL extends AbstractFrameBodyTextInfo implements ID3v24FrameBody
{

    public FrameBodyTDRL()
    {
    }

    public FrameBodyTDRL(FrameBodyTDRL body)
    {
        super(body);
    }


    public FrameBodyTDRL(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTDRL(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_RELEASE_TIME;
    }
}
