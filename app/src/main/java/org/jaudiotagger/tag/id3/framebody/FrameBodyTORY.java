
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;


public class FrameBodyTORY extends AbstractFrameBodyTextInfo implements ID3v23FrameBody
{
    private static final int NUMBER_OF_DIGITS_IN_YEAR = 4;


    public FrameBodyTORY()
    {
    }

    public FrameBodyTORY(FrameBodyTORY body)
    {
        super(body);
    }


    public FrameBodyTORY(byte textEncoding, String text)
    {
        super(textEncoding, text);
    }


    public FrameBodyTORY(FrameBodyTDOR body)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        String year=body.getText();
        if(body.getText().length()> NUMBER_OF_DIGITS_IN_YEAR)
        {
            year=body.getText().substring(0, NUMBER_OF_DIGITS_IN_YEAR);
        }
        setObjectValue(DataTypes.OBJ_TEXT, year);
    }


    public FrameBodyTORY(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_TORY;
    }
}