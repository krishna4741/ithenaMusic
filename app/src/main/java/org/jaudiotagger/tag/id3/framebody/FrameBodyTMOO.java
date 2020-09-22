
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;


public class FrameBodyTMOO extends AbstractFrameBodyTextInfo implements ID3v24FrameBody
{

    public FrameBodyTMOO()
    {
    }

    public FrameBodyTMOO(FrameBodyTMOO body)
    {
        super(body);
    }


    public FrameBodyTMOO(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }

    public FrameBodyTMOO(FrameBodyTXXX body)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding());
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        this.setObjectValue(DataTypes.OBJ_TEXT, body.getText());
    }
    

    public FrameBodyTMOO(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_MOOD;
    }

}