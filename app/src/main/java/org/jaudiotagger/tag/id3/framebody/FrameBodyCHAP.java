
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.id3.ID3v2ChapterFrames;

import java.nio.ByteBuffer;


public class FrameBodyCHAP extends AbstractID3v2FrameBody implements ID3v2ChapterFrameBody
{

    public FrameBodyCHAP()
    {
    }


    public FrameBodyCHAP(FrameBodyCHAP body)
    {
        super(body);
    }


    public FrameBodyCHAP(String elementId, int startTime, int endTime, int startOffset, int endOffset)
    {
        this.setObjectValue(DataTypes.OBJ_ELEMENT_ID, elementId);
        this.setObjectValue(DataTypes.OBJ_START_TIME, startTime);
        this.setObjectValue(DataTypes.OBJ_END_TIME, endTime);
        this.setObjectValue(DataTypes.OBJ_START_OFFSET, startOffset);
        this.setObjectValue(DataTypes.OBJ_END_OFFSET, endOffset);
    }


    public FrameBodyCHAP(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v2ChapterFrames.FRAME_ID_CHAPTER;
    }


    protected void setupObjectList()
    {
        objectList.add(new StringNullTerminated(DataTypes.OBJ_ELEMENT_ID, this));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_START_TIME, this, 4));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_END_TIME, this, 4));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_START_OFFSET, this, 4));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_END_OFFSET, this, 4));
    }
}
