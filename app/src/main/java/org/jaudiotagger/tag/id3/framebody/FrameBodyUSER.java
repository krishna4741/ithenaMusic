
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.Languages;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class FrameBodyUSER extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyUSER()
    {
        //        setObject("Text Encoding", new Byte((byte) 0));
        //        setObject("Language", "");
        //        setObject("Text", "");
    }

    public FrameBodyUSER(FrameBodyUSER body)
    {
        super(body);
    }


    public FrameBodyUSER(byte textEncoding, String language, String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
        setObjectValue(DataTypes.OBJ_TEXT, text);
    }



    public FrameBodyUSER(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_TERMS_OF_USE;
    }


    public String getLanguage()
    {
        return (String) getObjectValue(DataTypes.OBJ_LANGUAGE);
    }


    public void setOwner(String language)
    {
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
    }


    public void write(ByteArrayOutputStream tagBuffer)
    {
        if (!((AbstractString) getObject(DataTypes.OBJ_TEXT)).canBeEncoded())
        {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringHashMap(DataTypes.OBJ_LANGUAGE, this, Languages.LANGUAGE_FIELD_SIZE));
        objectList.add(new StringSizeTerminated(DataTypes.OBJ_TEXT, this));
    }
}
