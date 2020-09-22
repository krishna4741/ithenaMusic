
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.PartOfSet;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;


public class FrameBodyTRCK extends AbstractID3v2FrameBody implements ID3v23FrameBody, ID3v24FrameBody
{


    public FrameBodyTRCK()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue());
    }

    public FrameBodyTRCK(FrameBodyTRCK body)
    {
        super(body);
    }


    public FrameBodyTRCK(byte textEncoding, String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(text));
    }

    public FrameBodyTRCK(byte textEncoding, Integer trackNo,Integer trackTotal)
    {
        super();
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(trackNo,trackTotal));
    }

    public String getUserFriendlyValue()
    {
        return String.valueOf(getTrackNo());
    }


    public FrameBodyTRCK(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }



    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_TRACK;
    }

    public Integer getTrackNo()
    {
        PartOfSet.PartOfSetValue value = (PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT);
        return value.getCount();
    }

    public String getTrackNoAsText()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getCountAsText();
    }

    public String getText()
    {
        return getObjectValue(DataTypes.OBJ_TEXT).toString();
    }
    public void setTrackNo(Integer trackNo)
    {
        ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setCount(trackNo);
    }

    public void setTrackNo(String trackNo)
    {
        ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setCount(trackNo);
    }

    public Integer getTrackTotal()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getTotal();
    }

    public String getTrackTotalAsText()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getTotalAsText();
    }


    public void setTrackTotal(Integer trackTotal)
    {
         ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setTotal(trackTotal);
    }

    public void setTrackTotal(String trackTotal)
    {
        ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setTotal(trackTotal);
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