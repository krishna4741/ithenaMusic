
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v2ChapterFrames;

import java.nio.ByteBuffer;


public class FrameBodyCTOC extends AbstractID3v2FrameBody implements ID3v2ChapterFrameBody
{

    public FrameBodyCTOC()
    {
    }


    public FrameBodyCTOC(FrameBodyCTOC body)
    {
        super(body);
    }


    public FrameBodyCTOC(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v2ChapterFrames.FRAME_ID_TABLE_OF_CONTENT;
    }


    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
