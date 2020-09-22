
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;



public class FrameBodyTDOR extends AbstractFrameBodyTextInfo implements ID3v24FrameBody
{

    public FrameBodyTDOR()
    {
    }

    public FrameBodyTDOR(FrameBodyTDOR body)
    {
        super(body);
    }


    public FrameBodyTDOR(FrameBodyTORY body)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        setObjectValue(DataTypes.OBJ_TEXT, body.getText());
    }


    public FrameBodyTDOR(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTDOR(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ORIGINAL_RELEASE_TIME;
    }

}
