
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3TextEncodingConversion;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.Languages;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;


public class FrameBodyCOMM extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{
    //Most players only read comment with description of blank
    public static final String DEFAULT = "";

    //used by iTunes for volume normalization, although uses the COMMENT field not usually displayed as a comment
    public static final String ITUNES_NORMALIZATION = "iTunNORM";

    //Various descriptions used by MediaMonkey, (note Media Monkey uses non-standard language field XXX)
    private static final String MM_PREFIX               = "Songs-DB";
    public static final String MM_CUSTOM1               = "Songs-DB_Custom1";
    public static final String MM_CUSTOM2               = "Songs-DB_Custom2";
    public static final String MM_CUSTOM3               = "Songs-DB_Custom3";
    public static final String MM_CUSTOM4               = "Songs-DB_Custom4";
    public static final String MM_CUSTOM5               = "Songs-DB_Custom5";
    public static final String MM_OCCASION              = "Songs-DB_Occasion";
    public static final String MM_QUALITY               = "Songs-DB_Preference";
    public static final String MM_TEMPO                 = "Songs-DB_Tempo";

    public boolean isMediaMonkeyFrame()
    {
        String desc = getDescription();
        if(desc!=null && !(desc.length()==0))
        {
            if(desc.startsWith(MM_PREFIX))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isItunesFrame()
    {
        String desc = getDescription();
        if(desc!=null && !(desc.length()==0))
        {
            if(desc.equals(ITUNES_NORMALIZATION))
            {
                return true;
            }
        }
        return false;
    }

    

    public FrameBodyCOMM()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        setObjectValue(DataTypes.OBJ_LANGUAGE, Languages.DEFAULT_ID);
        setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
        setObjectValue(DataTypes.OBJ_TEXT, "");
    }

    public FrameBodyCOMM(FrameBodyCOMM body)
    {
        super(body);
    }


    public FrameBodyCOMM(byte textEncoding, String language, String description, String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        setObjectValue(DataTypes.OBJ_TEXT, text);
    }


    public FrameBodyCOMM(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }



    public void setDescription(String description)
    {
        if (description == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
    }


    public String getDescription()
    {
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_COMMENT;
    }


    public void setLanguage(String language)
    {
        //TODO not sure if this might break existing code

        setObjectValue(DataTypes.OBJ_LANGUAGE, language);        
    }


    public String getLanguage()
    {
        return (String) getObjectValue(DataTypes.OBJ_LANGUAGE);
    }


    public void setText(String text)
    {
        if (text == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        setObjectValue(DataTypes.OBJ_TEXT, text);
    }


    public String getText()
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_TEXT);
        return text.getValueAtIndex(0);
    }

    public String getUserFriendlyValue()
    {
        return getText();
    }



    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringHashMap(DataTypes.OBJ_LANGUAGE, this, Languages.LANGUAGE_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new TextEncodedStringSizeTerminated(DataTypes.OBJ_TEXT, this));
    }


    public void write(ByteArrayOutputStream tagBuffer)
    {
        //Ensure valid for type
        setTextEncoding(ID3TextEncodingConversion.getTextEncoding(getHeader(), getTextEncoding()));

        //Ensure valid for data
        if (!((AbstractString) getObject(DataTypes.OBJ_TEXT)).canBeEncoded())
        {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(getHeader()));
        }
        if (!((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded())
        {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(getHeader()));
        }
        super.write(tagBuffer);
    }


    public String getTextWithoutTrailingNulls()
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_TEXT);
        return text.getValueWithoutTrailingNull();
    }


    public String getFirstTextValue()
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_TEXT);
        return text.getValueAtIndex(0);
    }


    public String getValueAtIndex(int index)
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_TEXT);
        return text.getValueAtIndex(index);
    }

    public List<String> getValues()
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_TEXT);
        return text.getValues();
    }

    public void addTextValue(String value)
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_TEXT);
        text.addValue(value);
    }


    public int getNumberOfValues()
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_TEXT);
        return text.getNumberOfValues();
    }

}
