
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyENCR extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyENCR()
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, "");
        this.setObjectValue(DataTypes.OBJ_METHOD_SYMBOL, (byte) 0);
        this.setObjectValue(DataTypes.OBJ_ENCRYPTION_INFO, new byte[0]);
    }

    public FrameBodyENCR(FrameBodyENCR body)
    {
        super(body);
    }


    public FrameBodyENCR(String owner, byte methodSymbol, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner);
        this.setObjectValue(DataTypes.OBJ_METHOD_SYMBOL, methodSymbol);
        this.setObjectValue(DataTypes.OBJ_ENCRYPTION_INFO, data);
    }


    public FrameBodyENCR(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ENCRYPTION;
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
        objectList.add(new NumberFixedLength(DataTypes.OBJ_METHOD_SYMBOL, this, 1));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_ENCRYPTION_INFO, this));
    }
}
