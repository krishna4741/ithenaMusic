
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTPE2 extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTPE2()
    {
    }

    public FrameBodyTPE2(FrameBodyTPE2 body)
    {
        super(body);
    }


    public FrameBodyTPE2(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTPE2(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ACCOMPANIMENT;
    }
}