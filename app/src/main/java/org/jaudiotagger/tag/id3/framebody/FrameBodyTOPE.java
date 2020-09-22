
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTOPE extends AbstractFrameBodyTextInfo implements ID3v23FrameBody, ID3v24FrameBody
{

    public FrameBodyTOPE()
    {
    }

    public FrameBodyTOPE(FrameBodyTOPE body)
    {
        super(body);
    }


    public FrameBodyTOPE(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTOPE(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ORIGARTIST;
    }
}