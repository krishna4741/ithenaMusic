
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v23Frames;


public class FrameBodyEQUA extends AbstractID3v2FrameBody implements ID3v23FrameBody
{

    public FrameBodyEQUA()
    {
    }

    public FrameBodyEQUA(FrameBodyEQUA body)
    {
        super(body);
    }


    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_EQUALISATION;
    }


    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}