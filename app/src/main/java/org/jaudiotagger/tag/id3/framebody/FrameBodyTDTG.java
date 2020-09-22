
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;



public class FrameBodyTDTG extends AbstractFrameBodyTextInfo implements ID3v24FrameBody
{


    public FrameBodyTDTG()
    {
    }

    public FrameBodyTDTG(FrameBodyTDTG body)
    {
        super(body);
    }


    public FrameBodyTDTG(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTDTG(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_TAGGING_TIME;
    }


}
