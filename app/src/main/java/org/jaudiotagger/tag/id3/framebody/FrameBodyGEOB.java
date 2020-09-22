
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class FrameBodyGEOB extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{


    public FrameBodyGEOB()
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, "");
        this.setObjectValue(DataTypes.OBJ_FILENAME, "");
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
        this.setObjectValue(DataTypes.OBJ_DATA, new byte[0]);
    }

    public FrameBodyGEOB(FrameBodyGEOB body)
    {
        super(body);
    }


    public FrameBodyGEOB(byte textEncoding, String mimeType, String filename, String description, byte[] object)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType);
        this.setObjectValue(DataTypes.OBJ_FILENAME, filename);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_DATA, object);
    }


    public FrameBodyGEOB(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
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
        return ID3v24Frames.FRAME_ID_GENERAL_ENCAPS_OBJECT;
    }



    public void write(ByteArrayOutputStream tagBuffer)
    {
        if (!((AbstractString) getObject(DataTypes.OBJ_FILENAME)).canBeEncoded())
        {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        if (!((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded())
        {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        super.write(tagBuffer);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_MIME_TYPE, this));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_FILENAME, this));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
