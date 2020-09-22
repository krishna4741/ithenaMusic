
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.reference.MusicalKey;

import java.nio.ByteBuffer;


public class FrameBodyTKEY extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyTKEY()
    {
    }

    public FrameBodyTKEY(FrameBodyTKEY body)
    {
        super(body);
    }


    public FrameBodyTKEY(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTKEY(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_INITIAL_KEY;
    }


    public boolean isValid()
    {
        return MusicalKey.isValid(getFirstTextValue());
    }
}