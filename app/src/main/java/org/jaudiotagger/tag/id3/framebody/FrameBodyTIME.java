
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v23Frames;

import java.nio.ByteBuffer;


public class FrameBodyTIME extends AbstractFrameBodyTextInfo implements ID3v23FrameBody
{
    private boolean hoursOnly;

    public FrameBodyTIME()
    {
    }

    public FrameBodyTIME(FrameBodyTIME body)
    {
        super(body);
    }


    public FrameBodyTIME(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTIME(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_TIME;
    }

    public boolean isHoursOnly()
    {
        return hoursOnly;
    }

    public void setHoursOnly(boolean hoursOnly)
    {
        this.hoursOnly = hoursOnly;
    }
}