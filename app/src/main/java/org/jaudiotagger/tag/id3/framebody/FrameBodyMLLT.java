
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v24Frames;


public class FrameBodyMLLT extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyMLLT()
    {
    }

    public FrameBodyMLLT(FrameBodyMLLT body)
    {
        super(body);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_MPEG_LOCATION_LOOKUP_TABLE;
    }



    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }

}