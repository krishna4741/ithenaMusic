
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyWPUB extends AbstractFrameBodyUrlLink implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyWPUB()
    {
    }


    public FrameBodyWPUB(String urlLink)
    {
        super(urlLink);
    }

    public FrameBodyWPUB(FrameBodyWPUB body)
    {
        super(body);
    }


    public FrameBodyWPUB(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_URL_PUBLISHERS;
    }
}