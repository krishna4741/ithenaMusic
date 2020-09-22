
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.id3.ID3v22Frames;

import java.nio.ByteBuffer;


public class FrameBodyCRM extends AbstractID3v2FrameBody implements ID3v22FrameBody
{

    public FrameBodyCRM()
    {
        //        this.setObject(ObjectTypes.OBJ_OWNER, "");
        //        this.setObject(ObjectTypes.OBJ_DESCRIPTION, "");
        //        this.setObject("Encrypted datablock", new byte[0]);
    }

    public FrameBodyCRM(FrameBodyCRM body)
    {
        super(body);
    }


    public FrameBodyCRM(String owner, String description, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_ENCRYPTED_DATABLOCK, data);
    }


    public FrameBodyCRM(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v22Frames.FRAME_ID_V2_ENCRYPTED_FRAME;
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
        objectList.add(new StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_ENCRYPTED_DATABLOCK, this));
    }
}
