
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTDEN extends AbstractFrameBodyTextInfo implements ID3v24FrameBody
{

    public FrameBodyTDEN()
    {
    }

    public FrameBodyTDEN(FrameBodyTDEN body)
    {
        super(body);
    }


    public FrameBodyTDEN(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTDEN(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }



    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ENCODING_TIME;
    }
}
