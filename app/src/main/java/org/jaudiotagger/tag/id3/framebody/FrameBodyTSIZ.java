
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v23Frames;

import java.nio.ByteBuffer;


public class FrameBodyTSIZ extends AbstractFrameBodyTextInfo implements ID3v23FrameBody
{

    public FrameBodyTSIZ()
    {
    }

    public FrameBodyTSIZ(FrameBodyTSIZ body)
    {
        super(body);
    }


    public FrameBodyTSIZ(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTSIZ(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_TSIZ;
    }
}