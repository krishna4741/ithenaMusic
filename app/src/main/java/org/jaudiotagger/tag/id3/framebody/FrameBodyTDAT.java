
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v23Frames;

import java.nio.ByteBuffer;


public class FrameBodyTDAT extends AbstractFrameBodyTextInfo implements ID3v23FrameBody
{
    private boolean monthOnly;


    public FrameBodyTDAT()
    {
    }

    public FrameBodyTDAT(FrameBodyTDAT body)
    {
        super(body);
    }


    public FrameBodyTDAT(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTDAT(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_TDAT;
    }

    public boolean isMonthOnly()
    {
        return monthOnly;
    }

    public void setMonthOnly(boolean monthOnly)
    {
        this.monthOnly = monthOnly;
    }
}