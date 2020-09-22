
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTIT3 extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTIT3()
    {
    }

    public FrameBodyTIT3(FrameBodyTIT3 body)
    {
        super(body);
    }


    public FrameBodyTIT3(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTIT3(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_TITLE_REFINEMENT;
    }
}