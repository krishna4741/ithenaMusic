
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTMCL extends AbstractFrameBodyTextInfo implements ID3v24FrameBody
{

    public FrameBodyTMCL()
    {
    }

    public FrameBodyTMCL(FrameBodyTMCL body)
    {
        super(body);
    }


    public FrameBodyTMCL(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTMCL(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_MUSICIAN_CREDITS;
    }
}