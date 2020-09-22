
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.AbstractTagFrameBody;



public class StringDateTime extends StringSizeTerminated
{

    public StringDateTime(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public StringDateTime(StringDateTime object)
    {
        super(object);
    }


    public void setValue(Object value)
    {
        if (value != null)
        {
            this.value = value.toString().replace(' ', 'T');
        }
    }


    public Object getValue()
    {
        if (value != null)
        {
            return value.toString().replace(' ', 'T');
        }
        else
        {
            return null;
        }
    }

    public boolean equals(Object obj)
    {
        return obj instanceof StringDateTime && super.equals(obj);

    }
}
