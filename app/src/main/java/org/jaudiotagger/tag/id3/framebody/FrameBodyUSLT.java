
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3TextEncodingConversion;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.Languages;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class FrameBodyUSLT extends AbstractID3v2FrameBody implements ID3v23FrameBody, ID3v24FrameBody
{

    public FrameBodyUSLT()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        setObjectValue(DataTypes.OBJ_LANGUAGE, "");
        setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
        setObjectValue(DataTypes.OBJ_LYRICS, "");
    }


    public FrameBodyUSLT(FrameBodyUSLT body)
    {
        super(body);
    }


    public FrameBodyUSLT(byte textEncoding, String language, String description, String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        setObjectValue(DataTypes.OBJ_LYRICS, text);
    }


    public FrameBodyUSLT(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    public String getUserFriendlyValue()
    {
        return getFirstTextValue();
    }



    public void setDescription(String description)
    {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
    }


    public String getDescription()
    {
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_UNSYNC_LYRICS;
    }


    public void setLanguage(String language)
    {
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
    }


    public String getLanguage()
    {
        return (String) getObjectValue(DataTypes.OBJ_LANGUAGE);
    }


    public void setLyric(String lyric)
    {
        setObjectValue(DataTypes.OBJ_LYRICS, lyric);
    }


    public String getLyric()
    {
        return (String) getObjectValue(DataTypes.OBJ_LYRICS);
    }


    public String getFirstTextValue()
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_LYRICS);
        return text.getValueAtIndex(0);
    }


    public void addLyric(String text)
    {
        setLyric(getLyric() + text);
        }


    public void addLyric(Lyrics3Line line)
    {
        setLyric(getLyric() + line.writeString());
    }


    public void write(ByteArrayOutputStream tagBuffer)
    {

        //Ensure valid for type
        this.setTextEncoding(ID3TextEncodingConversion.getTextEncoding(getHeader(), getTextEncoding()));

        //Ensure valid for data                    
        if (!((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded())
        {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(getHeader()));
        }
        if (!((AbstractString) getObject(DataTypes.OBJ_LYRICS)).canBeEncoded())
        {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(getHeader()));
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringHashMap(DataTypes.OBJ_LANGUAGE, this, Languages.LANGUAGE_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new TextEncodedStringSizeTerminated(DataTypes.OBJ_LYRICS, this));
    }

}
