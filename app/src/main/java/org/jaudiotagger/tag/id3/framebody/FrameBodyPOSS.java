
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.NumberVariableLength;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.EventTimingTimestampTypes;

import java.nio.ByteBuffer;


public class FrameBodyPOSS extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyPOSS()
    {
        //        this.setObject(ObjectNumberHashMap.OBJ_TIME_STAMP_FORMAT, new Byte((byte) 0));
        //        this.setObject("Position", new Long(0));
    }

    public FrameBodyPOSS(FrameBodyPOSS body)
    {
        super(body);
    }


    public FrameBodyPOSS(byte timeStampFormat, long position)
    {
        this.setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, timeStampFormat);
        this.setObjectValue(DataTypes.OBJ_POSITION, position);
    }


    public FrameBodyPOSS(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_POSITION_SYNC;
    }



    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TIME_STAMP_FORMAT, this, EventTimingTimestampTypes.TIMESTAMP_KEY_FIELD_SIZE));
        objectList.add(new NumberVariableLength(DataTypes.OBJ_POSITION, this, 1));
    }
}
