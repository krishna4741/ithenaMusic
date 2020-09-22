
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;


public class FrameBodyTYER extends AbstractFrameBodyTextInfo implements ID3v23FrameBody
{

    public FrameBodyTYER()
    {
    }

    public FrameBodyTYER(FrameBodyTYER body)
    {
        super(body);
    }


    public FrameBodyTYER(FrameBodyTDRC body)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        setObjectValue(DataTypes.OBJ_TEXT, body.getText());
    }


    public FrameBodyTYER(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTYER(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_TYER;
    }
}
