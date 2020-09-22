
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.BooleanByte;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;



public class FrameBodyRBUF extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{
    private static int BUFFER_FIELD_SIZE = 3;
    private static int EMBED_FLAG_BIT_POSITION = 1;
    private static int OFFSET_FIELD_SIZE = 4;


    public FrameBodyRBUF()
    {
        this.setObjectValue(DataTypes.OBJ_BUFFER_SIZE, (byte) 0);
        this.setObjectValue(DataTypes.OBJ_EMBED_FLAG, Boolean.FALSE);
        this.setObjectValue(DataTypes.OBJ_OFFSET, (byte) 0);
    }

    public FrameBodyRBUF(FrameBodyRBUF body)
    {
        super(body);
    }


    public FrameBodyRBUF(byte bufferSize, boolean embeddedInfoFlag, byte offsetToNextTag)
    {
        this.setObjectValue(DataTypes.OBJ_BUFFER_SIZE, bufferSize);
        this.setObjectValue(DataTypes.OBJ_EMBED_FLAG, embeddedInfoFlag);
        this.setObjectValue(DataTypes.OBJ_OFFSET, offsetToNextTag);
    }


    public FrameBodyRBUF(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_RECOMMENDED_BUFFER_SIZE;
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberFixedLength(DataTypes.OBJ_BUFFER_SIZE, this, BUFFER_FIELD_SIZE));
        objectList.add(new BooleanByte(DataTypes.OBJ_EMBED_FLAG, this, (byte) EMBED_FLAG_BIT_POSITION));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_OFFSET, this, OFFSET_FIELD_SIZE));
    }
}
