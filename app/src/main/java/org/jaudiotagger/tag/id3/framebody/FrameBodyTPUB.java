
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTPUB extends AbstractFrameBodyTextInfo implements ID3v23FrameBody, ID3v24FrameBody
{

    public FrameBodyTPUB()
    {
    }

    public FrameBodyTPUB(FrameBodyTPUB body)
    {
        super(body);
    }


    public FrameBodyTPUB(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTPUB(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_PUBLISHER;
    }
}