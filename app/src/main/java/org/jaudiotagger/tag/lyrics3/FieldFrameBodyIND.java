
package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.BooleanString;

import java.nio.ByteBuffer;


public class FieldFrameBodyIND extends AbstractLyrics3v2FieldFrameBody
{

    public FieldFrameBodyIND()
    {
        //        this.setObject("Lyrics Present", new Boolean(false));
        //        this.setObject("Timestamp Present", new Boolean(false));
    }

    public FieldFrameBodyIND(FieldFrameBodyIND body)
    {
        super(body);
    }


    public FieldFrameBodyIND(boolean lyricsPresent, boolean timeStampPresent)
    {
        this.setObjectValue("Lyrics Present", lyricsPresent);
        this.setObjectValue("Timestamp Present", timeStampPresent);
    }


    public FieldFrameBodyIND(ByteBuffer byteBuffer) throws InvalidTagException
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
        return "IND";
    }


    protected void setupObjectList()
    {
        objectList.add(new BooleanString("Lyrics Present", this));
        objectList.add(new BooleanString("Timestamp Present", this));
    }
}
