
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.ID3v23Frames;

import java.nio.ByteBuffer;


public class FrameBodyEncrypted extends AbstractID3v2FrameBody implements ID3v24FrameBody, ID3v23FrameBody
{
    private String identifier=null;


    public FrameBodyEncrypted(String identifier)
    {
        this.identifier=identifier;
    }

    public FrameBodyEncrypted(FrameBodyEncrypted body)
    {
        super(body);
    }


    public FrameBodyEncrypted(String identifier,ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
    {
        super(byteBuffer, frameSize);
        this.identifier=identifier;
    }

    public String getIdentifier()
    {
        return identifier;
    }


    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}