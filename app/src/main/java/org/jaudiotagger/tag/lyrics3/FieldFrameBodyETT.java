
package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;

import java.nio.ByteBuffer;


public class FieldFrameBodyETT extends AbstractLyrics3v2FieldFrameBody
{

    public FieldFrameBodyETT()
    {
        //        this.setObject("Title", "");
    }

    public FieldFrameBodyETT(FieldFrameBodyETT body)
    {
        super(body);
    }


    public FieldFrameBodyETT(String title)
    {
        this.setObjectValue("Title", title);
    }


    public FieldFrameBodyETT(ByteBuffer byteBuffer) throws InvalidTagException
    {
        this.read(byteBuffer);
    }


    public String getIdentifier()
    {
        return "ETT";
    }


    public void setTitle(String title)
    {
        setObjectValue("Title", title);
    }


    public String getTitle()
    {
        return (String) getObjectValue("Title");
    }


    protected void setupObjectList()
    {
        objectList.add(new StringSizeTerminated("Title", this));
    }
}
