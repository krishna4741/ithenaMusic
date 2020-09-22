
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v23Frames;

import java.nio.ByteBuffer;


public class FrameBodyRVAD extends AbstractID3v2FrameBody implements ID3v23FrameBody
{


    public FrameBodyRVAD()
    {

    }

    public FrameBodyRVAD(FrameBodyRVAD copyObject)
    {
        super(copyObject);

    }



    public FrameBodyRVAD(FrameBodyRVA2 body)
    {
        setObjectValue(DataTypes.OBJ_DATA, body.getObjectValue(DataTypes.OBJ_DATA));
    }


    public FrameBodyRVAD(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT;
    }


    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
