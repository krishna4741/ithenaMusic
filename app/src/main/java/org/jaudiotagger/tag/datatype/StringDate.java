
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.ID3Tags;


public class StringDate extends StringFixedLength
{

    public StringDate(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody, 8);
    }

    public StringDate(StringDate object)
    {
        super(object);
    }


    public void setValue(Object value)
    {
        if (value != null)
        {
            this.value = ID3Tags.stripChar(value.toString(), '-');
        }
    }


    public Object getValue()
    {
        if (value != null)
        {
            return ID3Tags.stripChar(value.toString(), '-');
        }
        else
        {
            return null;
        }
    }

    public boolean equals(Object obj)
    {
        return obj instanceof StringDate && super.equals(obj);

    }
}
