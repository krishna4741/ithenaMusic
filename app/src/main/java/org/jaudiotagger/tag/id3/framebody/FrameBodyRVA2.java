
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;

public class FrameBodyRVA2 extends AbstractID3v2FrameBody implements ID3v24FrameBody
{


    public FrameBodyRVA2()
    {
    }

    public FrameBodyRVA2(FrameBodyRVA2 body)
    {
        super(body);
    }


    public FrameBodyRVA2(FrameBodyRVAD body)
    {
        setObjectValue(DataTypes.OBJ_DATA, body.getObjectValue(DataTypes.OBJ_DATA));
    }



    public FrameBodyRVA2(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2;
    }


    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }


}
