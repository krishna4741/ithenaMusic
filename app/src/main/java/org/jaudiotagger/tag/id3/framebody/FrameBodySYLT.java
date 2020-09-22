
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.EventTimingTimestampTypes;
import org.jaudiotagger.tag.id3.valuepair.SynchronisedLyricsContentType;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.Languages;

import java.nio.ByteBuffer;


public class FrameBodySYLT extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodySYLT()
    {

    }


    public FrameBodySYLT(FrameBodySYLT body)
    {
        super(body);
    }


    public FrameBodySYLT(int textEncoding, String language, int timeStampFormat, int contentType, String description, byte[] lyrics)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
        setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, timeStampFormat);
        setObjectValue(DataTypes.OBJ_CONTENT_TYPE, contentType);
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        setObjectValue(DataTypes.OBJ_DATA, lyrics);
    }


    public FrameBodySYLT(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getLanguage()
    {
        return (String) getObjectValue(DataTypes.OBJ_LANGUAGE);
    }


    public int getTimeStampFormat()
    {
        return ((Number) getObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT)).intValue();
    }


    public int getContentType()
    {
        return ((Number) getObjectValue(DataTypes.OBJ_CONTENT_TYPE)).intValue();
    }


    public String getDescription()
    {
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_SYNC_LYRIC;
    }



    public void setLyrics(byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_DATA, data);
    }


    public byte[] getLyrics()
    {
        return (byte[]) this.getObjectValue(DataTypes.OBJ_DATA);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringHashMap(DataTypes.OBJ_LANGUAGE, this, Languages.LANGUAGE_FIELD_SIZE));
        objectList.add(new NumberHashMap(DataTypes.OBJ_TIME_STAMP_FORMAT, this, EventTimingTimestampTypes.TIMESTAMP_KEY_FIELD_SIZE));
        objectList.add(new NumberHashMap(DataTypes.OBJ_CONTENT_TYPE, this, SynchronisedLyricsContentType.CONTENT_KEY_FIELD_SIZE));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));

        //TODO:This hold the actual lyrics
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
