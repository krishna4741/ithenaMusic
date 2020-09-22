
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTRSN extends AbstractFrameBodyTextInfo implements ID3v23FrameBody, ID3v24FrameBody
{

    public FrameBodyTRSN()
    {
    }

    public FrameBodyTRSN(FrameBodyTRSN body)
    {
        super(body);
    }


    public FrameBodyTRSN(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTRSN(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_RADIO_NAME;
    }
}