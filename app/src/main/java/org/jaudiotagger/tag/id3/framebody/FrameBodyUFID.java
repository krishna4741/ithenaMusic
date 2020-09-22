
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;



public class FrameBodyUFID extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{
    public static final String UFID_MUSICBRAINZ = "http://musicbrainz.org";
    public static final String UFID_ID3TEST = "http://www.id3.org/dummy/ufid.html";


    public FrameBodyUFID()
    {
        setOwner("");
        setUniqueIdentifier(new byte[0]);
    }

    public FrameBodyUFID(FrameBodyUFID body)
    {
        super(body);
    }


    public FrameBodyUFID(String owner, byte[] uniqueIdentifier)
    {
        setOwner(owner);
        setUniqueIdentifier(uniqueIdentifier);
    }


    public FrameBodyUFID(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_UNIQUE_FILE_ID;
    }


    public void setOwner(String owner)
    {
        setObjectValue(DataTypes.OBJ_OWNER, owner);
    }


    public String getOwner()
    {
        return (String) getObjectValue(DataTypes.OBJ_OWNER);
    }


    public void setUniqueIdentifier(byte[] uniqueIdentifier)
    {
        setObjectValue(DataTypes.OBJ_DATA, uniqueIdentifier);
    }


    public byte[] getUniqueIdentifier()
    {
        return (byte[]) getObjectValue(DataTypes.OBJ_DATA);
    }

    protected void setupObjectList()
    {
        objectList.add(new StringNullTerminated(DataTypes.OBJ_OWNER, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
