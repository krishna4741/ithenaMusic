
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;


public class StringSizeTerminated extends TextEncodedStringSizeTerminated
{


    public StringSizeTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public StringSizeTerminated(StringSizeTerminated object)
    {
        super(object);
    }

    public boolean equals(Object obj)
    {
        return obj instanceof StringSizeTerminated && super.equals(obj);
    }

    protected String getTextEncodingCharSet()
    {
        return TextEncoding.CHARSET_ISO_8859_1;
    }
}
