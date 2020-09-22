
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.TextEncodedStringSizeTerminated;
import org.jaudiotagger.tag.id3.ID3TextEncodingConversion;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;


public abstract class AbstractFrameBodyTextInfo extends AbstractID3v2FrameBody
{


    protected AbstractFrameBodyTextInfo()
    {
        super();
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        setObjectValue(DataTypes.OBJ_TEXT, "");
    }


    protected AbstractFrameBodyTextInfo(AbstractFrameBodyTextInfo body)
    {
        super(body);
    }


    protected AbstractFrameBodyTextInfo(byte textEncoding, String text)
    {
        super();
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_TEXT, text);
    }


    protected AbstractFrameBodyTextInfo(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public void setText(String text)
    {
        if (text == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        setObjectValue(DataTypes.OBJ_TEXT, text);
    }

    public String getUserFriendlyValue()
    {
        return getTextWithoutTrailingNulls();
    }


    public String getText()
    {
        return (String) getObjectValue(DataTypes.OBJ_TEXT);
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


    public void write(ByteArrayOutputStream tagBuffer)
    {
        //Ensure valid for type
        setTextEncoding(ID3TextEncodingConversion.getTextEncoding(getHeader(), getTextEncoding()));

        //Ensure valid for data
        if (!((TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_TEXT)).canBeEncoded())
        {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(getHeader()));
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new TextEncodedStringSizeTerminated(DataTypes.OBJ_TEXT, this));
    }
}
