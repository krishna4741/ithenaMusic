
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.StringFixedLength;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyLINK extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{

    public FrameBodyLINK()
    {
        //        this.setObject("Frame Identifier", "");
        //        this.setObject("URL", "");
        //        this.setObject("ID and Additional Data", "");
    }

    public FrameBodyLINK(FrameBodyLINK body)
    {
        super(body);
    }


    public FrameBodyLINK(String frameIdentifier, String url, String additionalData)
    {
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, frameIdentifier);
        this.setObjectValue(DataTypes.OBJ_URL, url);
        this.setObjectValue(DataTypes.OBJ_ID, additionalData);
    }


    public FrameBodyLINK(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getAdditionalData()
    {
        return (String) getObjectValue(DataTypes.OBJ_ID);
    }


    public void getAdditionalData(String additionalData)
    {
        setObjectValue(DataTypes.OBJ_ID, additionalData);
    }


    public String getFrameIdentifier()
    {
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }


    public void getFrameIdentifier(String frameIdentifier)
    {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, frameIdentifier);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_LINKED_INFO;
    }



    protected void setupObjectList()
    {
        objectList.add(new StringFixedLength(DataTypes.OBJ_DESCRIPTION, this, 4));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_URL, this));
        objectList.add(new StringSizeTerminated(DataTypes.OBJ_ID, this));
    }
}
