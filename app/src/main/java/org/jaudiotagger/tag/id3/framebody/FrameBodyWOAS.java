
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyWOAS extends AbstractFrameBodyUrlLink implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyWOAS()
    {
    }


    public FrameBodyWOAS(String urlLink)
    {
        super(urlLink);
    }

    public FrameBodyWOAS(FrameBodyWOAS body)
    {
        super(body);
    }


    public FrameBodyWOAS(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_URL_SOURCE_WEB;
    }
}