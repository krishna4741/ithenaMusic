
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberVariableLength;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyPCNT extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{
    private static final int COUNTER_MINIMUM_FIELD_SIZE = 4;


    public FrameBodyPCNT()
    {
        this.setObjectValue(DataTypes.OBJ_NUMBER, 0L);
    }

    public FrameBodyPCNT(FrameBodyPCNT body)
    {
        super(body);
    }


    public FrameBodyPCNT(long counter)
    {
        this.setObjectValue(DataTypes.OBJ_NUMBER, counter);
    }


    public FrameBodyPCNT(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public long getCounter()
    {
        return ((Number) getObjectValue(DataTypes.OBJ_NUMBER)).longValue();
    }


    public void setCounter(long counter)
    {
        setObjectValue(DataTypes.OBJ_NUMBER, counter);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_PLAY_COUNTER;
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberVariableLength(DataTypes.OBJ_NUMBER, this, COUNTER_MINIMUM_FIELD_SIZE));
    }
}
