
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTOAL extends AbstractFrameBodyTextInfo implements ID3v23FrameBody,ID3v24FrameBody
{

    public FrameBodyTOAL()
    {
    }

    public FrameBodyTOAL(FrameBodyTOAL body)
    {
        super(body);
    }


    public FrameBodyTOAL(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTOAL(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ORIG_TITLE;
    }
}