
package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;

import java.nio.ByteBuffer;

public class FieldFrameBodyAUT extends AbstractLyrics3v2FieldFrameBody
{

    public FieldFrameBodyAUT()
    {
        //        this.setObject("Author", "");
    }

    public FieldFrameBodyAUT(FieldFrameBodyAUT body)
    {
        super(body);
    }


    public FieldFrameBodyAUT(String author)
    {
        this.setObjectValue("Author", author);
    }


    public FieldFrameBodyAUT(ByteBuffer byteBuffer) throws InvalidTagException
    {
        this.read(byteBuffer);
    }


    public void setAuthor(String author)
    {
        setObjectValue("Author", author);
    }


    public String getAuthor()
    {
        return (String) getObjectValue("Author");
    }


    public String getIdentifier()
    {
        return "AUT";
    }


    protected void setupObjectList()
    {
        objectList.add(new StringSizeTerminated("Author", this));
    }
}
