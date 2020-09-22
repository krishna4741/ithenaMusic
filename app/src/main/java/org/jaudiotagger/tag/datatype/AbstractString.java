
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;


public abstract class AbstractString extends AbstractDataType
{

    protected AbstractString(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }


    public AbstractString(String identifier, AbstractTagFrameBody frameBody, String value)
    {
        super(identifier, frameBody, value);
    }


    protected AbstractString(AbstractString object)
    {
        super(object);
    }


    public int getSize()
    {
        return size;
    }


    protected void setSize(int size)
    {
        this.size = size;
    }


    public String toString()
    {
        return (String) value;
    }


    public boolean canBeEncoded()
    {
        //Try and write to buffer using the CharSet defined by the textEncoding field (note if using UTF16 we dont
        //need to worry about LE,BE at this point it makes no difference)
        byte textEncoding = this.getBody().getTextEncoding();
        String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
        CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();

        if (encoder.canEncode((String) value))
        {
            return true;
        }
        else
        {
            logger.finest("Failed Trying to decode" + value + "with" + encoder.toString());
            return false;
        }
    }
}
