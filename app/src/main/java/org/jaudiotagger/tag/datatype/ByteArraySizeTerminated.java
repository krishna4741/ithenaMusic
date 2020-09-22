
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;


public class ByteArraySizeTerminated extends AbstractDataType
{
    public ByteArraySizeTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public ByteArraySizeTerminated(ByteArraySizeTerminated object)
    {
        super(object);
    }


    public int getSize()
    {
        int len = 0;

        if (value != null)
        {
            len = ((byte[]) value).length;
        }

        return len;
    }

    public boolean equals(Object obj)
    {
        return obj instanceof ByteArraySizeTerminated && super.equals(obj);

    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        if (arr == null)
        {
            throw new NullPointerException("Byte array is null");
        }

        if (offset < 0)
        {
            throw new IndexOutOfBoundsException("Offset to byte array is out of bounds: offset = " + offset + ", array.length = " + arr.length);
        }

        //Empty Byte Array
        if (offset >= arr.length)
        {
            value = null;
            return;
        }

        int len = arr.length - offset;
        value = new byte[len];
        System.arraycopy(arr, offset, value, 0, len);
    }


    public String toString()
    {
        return getSize() + " bytes";
    }


    public byte[] writeByteArray()
    {
        logger.config("Writing byte array" + this.getIdentifier());
        return (byte[]) value;
    }
}
