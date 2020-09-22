
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.PartOfSet;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;


public class FrameBodyTPOS extends AbstractID3v2FrameBody implements ID3v23FrameBody, ID3v24FrameBody
{

    public FrameBodyTPOS()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue());
    }

    public FrameBodyTPOS(FrameBodyTPOS body)
    {
        super(body);
    }


    public FrameBodyTPOS(byte textEncoding, String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(text));
    }

    public FrameBodyTPOS(byte textEncoding, Integer discNo,Integer discTotal)
    {
        super();
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(discNo,discTotal));
    }



    public FrameBodyTPOS(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }



    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_SET;
    }

    public String getUserFriendlyValue()
    {
        return String.valueOf(getDiscNo());
    }

    public String getText()
    {
        return getObjectValue(DataTypes.OBJ_TEXT).toString();
    }

    public Integer getDiscNo()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getCount();
    }

    public String getDiscNoAsText()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getCountAsText();
    }

    public void setDiscNo(Integer discNo)
    {
        ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setCount(discNo);
    }

    public void setDiscNo(String discNo)
    {
        ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setCount(discNo);
    }


    public Integer getDiscTotal()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getTotal();
    }

    public String getDiscTotalAsText()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getTotalAsText();
    }

    public void setDiscTotal(Integer discTotal)
    {
         ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setTotal(discTotal);
    }

    public void setDiscTotal(String discTotal)
    {
        ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setTotal(discTotal);
    }

    public void setText(String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(text));
    }

    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new PartOfSet(DataTypes.OBJ_TEXT, this));
    }
}
