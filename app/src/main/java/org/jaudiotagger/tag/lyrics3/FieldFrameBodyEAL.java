
package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;

import java.nio.ByteBuffer;


public class FieldFrameBodyEAL extends AbstractLyrics3v2FieldFrameBody
{

    public FieldFrameBodyEAL()
    {
        //        this.setObject("Album", "");
    }

    public FieldFrameBodyEAL(FieldFrameBodyEAL body)
    {
        super(body);
    }


    public FieldFrameBodyEAL(String album)
    {
        this.setObjectValue("Album", album);
    }


    public FieldFrameBodyEAL(ByteBuffer byteBuffer) throws InvalidTagException
    {
        read(byteBuffer);

    }


    public void setAlbum(String album)
    {
        setObjectValue("Album", album);
    }


    public String getAlbum()
    {
        return (String) getObjectValue("Album");
    }


    public String getIdentifier()
    {
        return "EAL";
    }


    protected void setupObjectList()
    {
        objectList.add(new StringSizeTerminated("Album", this));
    }
}
