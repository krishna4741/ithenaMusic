
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.tag.Tag;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public abstract class AbstractTagCreator
{

    public ByteBuffer convert(Tag tag) throws UnsupportedEncodingException
    {
        return convert(tag, 0);
    }


    public abstract ByteBuffer convert(Tag tag, int padding) throws UnsupportedEncodingException;
}
