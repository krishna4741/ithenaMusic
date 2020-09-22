
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyWCOM extends AbstractFrameBodyUrlLink implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyWCOM()
    {
    }


    public FrameBodyWCOM(String urlLink)
    {
        super(urlLink);
    }

    public FrameBodyWCOM(FrameBodyWCOM body)
    {
        super(body);
    }


    public FrameBodyWCOM(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_URL_COMMERCIAL;
    }
}