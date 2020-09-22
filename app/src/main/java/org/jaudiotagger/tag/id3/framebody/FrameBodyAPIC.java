
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.PictureTypes;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class FrameBodyAPIC extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{
    public static final String IMAGE_IS_URL = "-->";


    public FrameBodyAPIC()
    {
        //Initilise default text encoding
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
    }

    public FrameBodyAPIC(FrameBodyAPIC body)
    {
        super(body);
    }


    public FrameBodyAPIC(FrameBodyPIC body)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding());
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, ImageFormats.getMimeTypeForFormat((String) body.getObjectValue(DataTypes.OBJ_IMAGE_FORMAT)));
        this.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, body.getObjectValue(DataTypes.OBJ_PICTURE_TYPE));
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, body.getDescription());
        this.setObjectValue(DataTypes.OBJ_PICTURE_DATA, body.getObjectValue(DataTypes.OBJ_PICTURE_DATA));

    }


    public FrameBodyAPIC(byte textEncoding, String mimeType, byte pictureType, String description, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        this.setMimeType(mimeType);
        this.setPictureType(pictureType);
        this.setDescription(description);
        this.setImageData(data);
    }


    public FrameBodyAPIC(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    public String getUserFriendlyValue()
    {
      return getMimeType()+":"+getDescription()+":"+getImageData().length;
    }



    public void setDescription(String description)
    {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
    }


    public String getDescription()
    {
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }


    public void setMimeType(String mimeType)
    {
        setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType);
    }


    public String getMimeType()
    {
        return (String) getObjectValue(DataTypes.OBJ_MIME_TYPE);
    }


    public void setImageData(byte[] imageData)
    {
        setObjectValue(DataTypes.OBJ_PICTURE_DATA, imageData);
    }


    public byte[] getImageData()
    {
        return (byte[]) getObjectValue(DataTypes.OBJ_PICTURE_DATA);
    }


    public void setPictureType(byte pictureType)
    {
        setObjectValue(DataTypes.OBJ_PICTURE_TYPE, pictureType);
    }


    public int getPictureType()
    {
        return ((Long) getObjectValue(DataTypes.OBJ_PICTURE_TYPE)).intValue();
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ATTACHED_PICTURE;
    }



    public void write(ByteArrayOutputStream tagBuffer)
    {
        if(TagOptionSingleton.getInstance().isAPICDescriptionITunesCompatible())
        {
            this.setTextEncoding(TextEncoding.ISO_8859_1);
            if (!((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded())
            {
                setDescription("");
            }
        }
        else
        {
            if (!((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded())
            {
                this.setTextEncoding(TextEncoding.UTF_16);
            }
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_MIME_TYPE, this));
        objectList.add(new NumberHashMap(DataTypes.OBJ_PICTURE_TYPE, this, PictureTypes.PICTURE_TYPE_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_PICTURE_DATA, this));
    }


    public boolean isImageUrl()
    {
        return getMimeType() != null && getMimeType().equals(IMAGE_IS_URL);
    }


    public String getImageUrl()
    {
        if (isImageUrl())
        {
            return Utils.getString(((byte[]) getObjectValue(DataTypes.OBJ_PICTURE_DATA)), 0, ((byte[]) getObjectValue(DataTypes.OBJ_PICTURE_DATA)).length, TextEncoding.CHARSET_ISO_8859_1);
        }
        else
        {
            return "";
        }
    }

}
