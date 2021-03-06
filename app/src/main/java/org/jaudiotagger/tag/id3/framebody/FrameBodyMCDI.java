
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyMCDI extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyMCDI()
    {
        this.setObjectValue(DataTypes.OBJ_DATA, new byte[0]);
    }

    public FrameBodyMCDI(FrameBodyMCDI body)
    {
        super(body);
    }


    public FrameBodyMCDI(byte[] cdTOC)
    {
        this.setObjectValue(DataTypes.OBJ_DATA, cdTOC);
    }


    public FrameBodyMCDI(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_MUSIC_CD_ID;
    }


    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
