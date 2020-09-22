
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodySEEK extends AbstractID3v2FrameBody implements ID3v24FrameBody
{

    public FrameBodySEEK()
    {
        //        this.setObject("Minimum Offset to Next Tag", new Integer(0));
    }


    public FrameBodySEEK(int minOffsetToNextTag)
    {
        this.setObjectValue(DataTypes.OBJ_OFFSET, minOffsetToNextTag);
    }

    public FrameBodySEEK(FrameBodySEEK body)
    {
        super(body);
    }


    public FrameBodySEEK(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_AUDIO_SEEK_POINT_INDEX;
    }


    protected void setupObjectList()
    {
        objectList.add(new NumberFixedLength(DataTypes.OBJ_OFFSET, this, 4));
    }
}
