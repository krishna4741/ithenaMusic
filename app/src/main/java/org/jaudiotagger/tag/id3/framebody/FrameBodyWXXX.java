
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;


public class FrameBodyWXXX extends AbstractFrameBodyUrlLink implements ID3v24FrameBody, ID3v23FrameBody
{

    public static final String URL_DISCOGS_RELEASE_SITE = "DISCOGS_RELEASE";
    public static final String URL_WIKIPEDIA_RELEASE_SITE = "WIKIPEDIA_RELEASE";
    public static final String URL_OFFICIAL_RELEASE_SITE = "OFFICIAL_RELEASE";
    public static final String URL_DISCOGS_ARTIST_SITE = "DISCOGS_ARTIST";
    public static final String URL_WIKIPEDIA_ARTIST_SITE = "WIKIPEDIA_ARTIST";
    public static final String URL_LYRICS_SITE = "LYRICS_SITE";


    public FrameBodyWXXX()
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
        this.setObjectValue(DataTypes.OBJ_URLLINK, "");
    }

    public FrameBodyWXXX(FrameBodyWXXX body)
    {
        super(body);
    }


    public FrameBodyWXXX(byte textEncoding, String description, String urlLink)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_URLLINK, urlLink);
    }


    public FrameBodyWXXX(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
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
        return ID3v24Frames.FRAME_ID_USER_DEFINED_URL;
    }


    public void write(ByteArrayOutputStream tagBuffer)
    {
        if (!((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded())
        {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new StringSizeTerminated(DataTypes.OBJ_URLLINK, this));
    }


    public String getUrlLinkWithoutTrailingNulls()
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_URLLINK);
        return text.getValueWithoutTrailingNull();
    }


    public String getFirstUrlLink()
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_URLLINK);
        return text.getValueAtIndex(0);
    }


    public String getUrlLinkAtIndex(int index)
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_URLLINK);
        return text.getValueAtIndex(index);
    }

    public List<String> getUrlLinks()
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_URLLINK);
        return text.getValues();
    }


    public void addUrlLink(String value)
    {
        TextEncodedStringSizeTerminated text = (TextEncodedStringSizeTerminated) getObject(DataTypes.OBJ_URLLINK);
        text.addValue(value);
    }
}
