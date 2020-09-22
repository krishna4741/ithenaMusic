

package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.utils.EqualsUtil;

import java.nio.ByteBuffer;
import java.util.logging.Logger;



public abstract class AbstractTagItem
{

    //Logger
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.id3");


    public AbstractTagItem()
    {
    }

    public AbstractTagItem(AbstractTagItem copyObject)
    {
        // no copy constructor in super class
    }


    abstract public String getIdentifier();


    abstract public int getSize();


    abstract public void read(ByteBuffer byteBuffer) throws TagException;


    public boolean isSubsetOf(Object obj)
    {
        return obj instanceof AbstractTagItem;
    }


    public boolean equals(Object obj)
    {
        if ( this == obj ) return true;
        return obj instanceof AbstractTagItem;
    }
}
