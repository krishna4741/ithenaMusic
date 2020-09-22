
package org.jaudiotagger.tag.id3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Iterator;


public abstract class AbstractTag extends AbstractTagItem
{
    protected static final String TYPE_TAG = "tag";


    public AbstractTag()
    {
    }

    public AbstractTag(AbstractTag copyObject)
    {
        super(copyObject);
    }


    abstract public boolean seek(ByteBuffer byteBuffer);


    public abstract void write(RandomAccessFile file) throws IOException;



    abstract public void delete(RandomAccessFile file) throws IOException;



    public boolean equals(Object obj)
    {
        return (obj instanceof AbstractTag) && super.equals(obj);

    }


    abstract public Iterator iterator();
}



