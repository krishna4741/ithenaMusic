
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;

import java.nio.ByteBuffer;


public class FrameBodyUnsupported extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody, ID3v22FrameBody
{

    private String identifier = "";


    public FrameBodyUnsupported()
    {

    }


    public FrameBodyUnsupported(String identifier)
    {
        this.identifier = identifier;
    }


    public FrameBodyUnsupported(String identifier, byte[] value)
    {
        this.identifier = identifier;
        setObjectValue(DataTypes.OBJ_DATA, value);
    }


    public FrameBodyUnsupported(byte[] value)
    {
        setObjectValue(DataTypes.OBJ_DATA, value);
    }


    public FrameBodyUnsupported(FrameBodyUnsupported copyObject)
    {
        super(copyObject);
        this.identifier = copyObject.identifier;

    }


    public FrameBodyUnsupported(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }


    public String getIdentifier()
    {
        return identifier;
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof FrameBodyUnsupported))
        {
            return false;
        }

        FrameBodyUnsupported object = (FrameBodyUnsupported) obj;
        return this.identifier.equals(object.identifier) && super.equals(obj);
    }



    public String toString()
    {
        return getIdentifier();
    }


    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }

}
