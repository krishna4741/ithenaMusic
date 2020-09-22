
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.reference.Languages;

import java.nio.ByteBuffer;


public class FrameBodyTLAN extends AbstractFrameBodyTextInfo implements ID3v24FrameBody, ID3v23FrameBody
{


    public FrameBodyTLAN()
    {
        super();
    }

    public FrameBodyTLAN(FrameBodyTLAN body)
    {
        super(body);
    }


    public FrameBodyTLAN(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTLAN(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_LANGUAGE;
    }


    public boolean isValid()
    {
        return Languages.getInstanceOf().getValueForId(getFirstTextValue())!=null;
    }
}
