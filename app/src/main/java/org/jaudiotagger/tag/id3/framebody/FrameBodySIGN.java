
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodySIGN extends AbstractID3v2FrameBody implements ID3v24FrameBody
{

    public FrameBodySIGN()
    {
        //        this.setObject("Group Symbol", new Byte((byte) 0));
        //        this.setObject("Signature", new byte[0]);
    }

    public FrameBodySIGN(FrameBodySIGN body)
    {
        super(body);
    }


    public FrameBodySIGN(byte groupSymbol, byte[] signature)
    {
        this.setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, groupSymbol);
        this.setObjectValue(DataTypes.OBJ_SIGNATURE, signature);
    }


    public FrameBodySIGN(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public void setGroupSymbol(byte groupSymbol)
    {
        setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, groupSymbol);
    }


    public byte getGroupSymbol()
    {
        if (getObjectValue(DataTypes.OBJ_GROUP_SYMBOL) != null)
        {
            return (Byte) getObjectValue(DataTypes.OBJ_GROUP_SYMBOL);
        }
        else
        {
            return (byte) 0;
        }
    }



    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_SIGNATURE;
    }


    public void setSignature(byte[] signature)
    {
        setObjectValue(DataTypes.OBJ_SIGNATURE, signature);
    }


    public byte[] getSignature()
    {
        return (byte[]) getObjectValue(DataTypes.OBJ_SIGNATURE);
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberFixedLength(DataTypes.OBJ_GROUP_SYMBOL, this, 1));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_SIGNATURE, this));
    }
}
