
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v22Frames;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.PictureTypes;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class FrameBodyPIC extends AbstractID3v2FrameBody implements ID3v22FrameBody
{
    public static final String IMAGE_IS_URL = "-->";


    public FrameBodyPIC()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
    }

    public FrameBodyPIC(FrameBodyPIC body)
    {
        super(body);
    }


    public FrameBodyPIC(byte textEncoding, String imageFormat, byte pictureType, String description, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        this.setObjectValue(DataTypes.OBJ_IMAGE_FORMAT, imageFormat);
        this.setPictureType(pictureType);
        this.setDescription(description);
        this.setImageData(data);
    }


    public FrameBodyPIC(FrameBodyAPIC body)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding());
        this.setObjectValue(DataTypes.OBJ_IMAGE_FORMAT, ImageFormats.getFormatForMimeType((String) body.getObjectValue(DataTypes.OBJ_MIME_TYPE)));
        this.setObjectValue(DataTypes.OBJ_PICTURE_DATA, body.getObjectValue(DataTypes.OBJ_PICTURE_DATA));
        this.setDescription(body.getDescription());
        this.setImageData(body.getImageData());
    }


    public FrameBodyPIC(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
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
        return ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE;
    }



    public void write(ByteArrayOutputStream tagBuffer)
    {
        if (!((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded())
        {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        super.write(tagBuffer);
    }


    public String getFormatType()
    {
        return (String) getObjectValue(DataTypes.OBJ_IMAGE_FORMAT);
    }

    public boolean isImageUrl()
    {
        return getFormatType() != null && getFormatType().equals(IMAGE_IS_URL);
    }


    public String getMimeType()
    {
        return (String) getObjectValue(DataTypes.OBJ_MIME_TYPE);
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


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringFixedLength(DataTypes.OBJ_IMAGE_FORMAT, this, 3));
        objectList.add(new NumberHashMap(DataTypes.OBJ_PICTURE_TYPE, this, PictureTypes.PICTURE_TYPE_FIELD_SIZE));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_PICTURE_DATA, this));
    }

}
