
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyAENC extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{


    public FrameBodyAENC()
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, "");
        this.setObjectValue(DataTypes.OBJ_PREVIEW_START, (short) 0);
        this.setObjectValue(DataTypes.OBJ_PREVIEW_LENGTH, (short) 0);
        this.setObjectValue(DataTypes.OBJ_ENCRYPTION_INFO, new byte[0]);
    }

    public FrameBodyAENC(FrameBodyAENC body)
    {
        super(body);
    }


    public FrameBodyAENC(String owner, short previewStart, short previewLength, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner);
        this.setObjectValue(DataTypes.OBJ_PREVIEW_START, previewStart);
        this.setObjectValue(DataTypes.OBJ_PREVIEW_LENGTH, previewLength);
        this.setObjectValue(DataTypes.OBJ_ENCRYPTION_INFO, data);
    }


    public FrameBodyAENC(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_AUDIO_ENCRYPTION;
    }


    public String getOwner()
    {
        return (String) getObjectValue(DataTypes.OBJ_OWNER);
    }


    public void getOwner(String description)
    {
        setObjectValue(DataTypes.OBJ_OWNER, description);
    }


    protected void setupObjectList()
    {
        objectList.add(new StringNullTerminated(DataTypes.OBJ_OWNER, this));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_PREVIEW_START, this, 2));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_PREVIEW_LENGTH, this, 2));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_ENCRYPTION_INFO, this));
    }
}
