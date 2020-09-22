
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyTFLT extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTFLT()
    {
    }

    public FrameBodyTFLT(FrameBodyTFLT body)
    {
        super(body);
    }


    public FrameBodyTFLT(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTFLT(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_FILE_TYPE;
    }
}
