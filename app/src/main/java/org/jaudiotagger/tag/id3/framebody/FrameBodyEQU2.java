
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;



public class FrameBodyEQU2 extends AbstractID3v2FrameBody implements ID3v24FrameBody
{

    public FrameBodyEQU2()
    {

    }

    public FrameBodyEQU2(FrameBodyEQU2 body)
    {
        super(body);
    }


    public FrameBodyEQU2(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_EQUALISATION2;
    }


    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
