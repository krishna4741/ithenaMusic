
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.ID3Tags;



public class NumberFixedLength extends AbstractDataType
{

    public NumberFixedLength(String identifier, AbstractTagFrameBody frameBody, int size)
    {
        super(identifier, frameBody);
        if (size < 0)
        {
            throw new IllegalArgumentException("Length is less than zero: " + size);
        }
        this.size = size;

    }

    public NumberFixedLength(NumberFixedLength copy)
    {
        super(copy);
        this.size = copy.size;
    }



    public void setSize(int size)
    {
        if (size > 0)
        {
            this.size = size;
        }
    }


    public int getSize()
    {
        return size;
    }

    public void setValue(Object value)
    {
        if (!(value instanceof Number))
        {
            throw new IllegalArgumentException("Invalid value type for NumberFixedLength:" + value.getClass());
        }
        super.setValue(value);
    }



    public boolean equals(Object obj)
    {
        if (!(obj instanceof NumberFixedLength))
        {
            return false;
        }
        NumberFixedLength object = (NumberFixedLength) obj;
        return this.size == object.size && super.equals(obj);
    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        if (arr == null)
        {
            throw new NullPointerException("Byte array is null");
        }
        if ((offset < 0) || (offset >= arr.length))
        {
            throw new InvalidDataTypeException("Offset to byte array is out of bounds: offset = " + offset + ", array.length = " + arr.length);
        }

        if(offset + size > arr.length)
        {
            throw new InvalidDataTypeException("Offset plus size to byte array is out of bounds: offset = "
                    + offset + ", size = "+size  +" + arr.length "+ arr.length );
        }

        long lvalue = 0;
        for (int i = offset; i < (offset + size); i++)
        {
            lvalue <<= 8;
            lvalue += (arr[i] & 0xff);
        }
        value = lvalue;
        logger.config("Read NumberFixedlength:" + value);
    }



    public String toString()
    {
        if (value == null)
        {
            return "";
        }
        else
        {
            return value.toString();
        }
    }


    public byte[] writeByteArray()
    {
        byte[] arr;
        arr = new byte[size];
        if (value != null)
        {
            //Convert value to long
            long temp = ID3Tags.getWholeNumber(value);

            for (int i = size - 1; i >= 0; i--)
            {
                arr[i] = (byte) (temp & 0xFF);
                temp >>= 8;
            }
        }
        return arr;
    }
}
