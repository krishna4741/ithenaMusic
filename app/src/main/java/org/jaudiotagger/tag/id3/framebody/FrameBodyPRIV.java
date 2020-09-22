
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyPRIV extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyPRIV()
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, "");
        this.setObjectValue(DataTypes.OBJ_DATA, new byte[0]);
    }

    public FrameBodyPRIV(FrameBodyPRIV body)
    {
        super(body);
    }


    public FrameBodyPRIV(String owner, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner);
        this.setObjectValue(DataTypes.OBJ_DATA, data);
    }


    public FrameBodyPRIV(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public void setData(byte[] data)
    {
        setObjectValue(DataTypes.OBJ_DATA, data);
    }


    public byte[] getData()
    {
        return (byte[]) getObjectValue(DataTypes.OBJ_DATA);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_PRIVATE;
    }


    public void setOwner(String owner)
    {
        setObjectValue(DataTypes.OBJ_OWNER, owner);
    }


    public String getOwner()
    {
        return (String) getObjectValue(DataTypes.OBJ_OWNER);
    }


    protected void setupObjectList()
    {
        objectList.add(new StringNullTerminated(DataTypes.OBJ_OWNER, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
