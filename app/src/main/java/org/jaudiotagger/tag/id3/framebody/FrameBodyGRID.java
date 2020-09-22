
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyGRID extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyGRID()
    {
        //        this.setObject(ObjectTypes.OBJ_OWNER, "");
        //        this.setObject("Group Symbol", new Byte((byte) 0));
        //        this.setObject("Group Dependent Data", new byte[0]);
    }

    public FrameBodyGRID(FrameBodyGRID body)
    {
        super(body);
    }


    public FrameBodyGRID(String owner, byte groupSymbol, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner);
        this.setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, groupSymbol);
        this.setObjectValue(DataTypes.OBJ_GROUP_DATA, data);
    }


    public FrameBodyGRID(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public void setGroupSymbol(byte textEncoding)
    {
        setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, textEncoding);
    }


    public byte getGroupSymbol()
    {
        if (getObjectValue(DataTypes.OBJ_GROUP_SYMBOL) != null)
        {
            return ((Long) getObjectValue(DataTypes.OBJ_GROUP_SYMBOL)).byteValue();
        }
        else
        {
            return (byte) 0;
        }
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_GROUP_ID_REG;
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
        objectList.add(new NumberFixedLength(DataTypes.OBJ_GROUP_SYMBOL, this, 1));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_GROUP_DATA, this));
    }
}
