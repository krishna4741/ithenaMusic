
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;


public class StringNullTerminated extends TextEncodedStringNullTerminated
{

    public StringNullTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public StringNullTerminated(StringNullTerminated object)
    {
        super(object);
    }

    public boolean equals(Object obj)
    {
        return obj instanceof StringNullTerminated && super.equals(obj);
    }

    protected String getTextEncodingCharSet()
    {
        return TextEncoding.CHARSET_ISO_8859_1;
    }
}
