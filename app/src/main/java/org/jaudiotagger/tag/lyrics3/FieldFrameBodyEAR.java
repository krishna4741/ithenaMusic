
package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;

import java.nio.ByteBuffer;


public class FieldFrameBodyEAR extends AbstractLyrics3v2FieldFrameBody
{

    public FieldFrameBodyEAR()
    {
        //        this.setObject("Artist", "");
    }

    public FieldFrameBodyEAR(FieldFrameBodyEAR body)
    {
        super(body);
    }


    public FieldFrameBodyEAR(String artist)
    {
        this.setObjectValue("Artist", artist);
    }


    public FieldFrameBodyEAR(ByteBuffer byteBuffer) throws InvalidTagException
    {

        this.read(byteBuffer);

    }


    public void setArtist(String artist)
    {
        setObjectValue("Artist", artist);
    }


    public String getArtist()
    {
        return (String) getObjectValue("Artist");
    }


    public String getIdentifier()
    {
        return "EAR";
    }


    protected void setupObjectList()
    {
        objectList.add(new StringSizeTerminated("Artist", this));
    }
}
