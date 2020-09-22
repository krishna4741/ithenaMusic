
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTCOM extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTCOM()
    {
    }

    public FrameBodyTCOM(FrameBodyTCOM body)
    {
        super(body);
    }


    public FrameBodyTCOM(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTCOM(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }



    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_COMPOSER;
    }
}
