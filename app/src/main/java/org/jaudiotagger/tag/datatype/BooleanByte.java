
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;


public class BooleanByte extends AbstractDataType
{

    int bitPosition = -1;


    public BooleanByte(String identifier, AbstractTagFrameBody frameBody, int bitPosition)
    {
        super(identifier, frameBody);
        if ((bitPosition < 0) || (bitPosition > 7))
        {
            throw new IndexOutOfBoundsException("Bit position needs to be from 0 - 7 : " + bitPosition);
        }

        this.bitPosition = bitPosition;
    }

    public BooleanByte(BooleanByte copy)
    {
        super(copy);
        this.bitPosition = copy.bitPosition;
    }


    public int getBitPosition()
    {
        return bitPosition;
    }


    public int getSize()
    {
        return 1;
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof BooleanByte))
        {
            return false;
        }

        BooleanByte object = (BooleanByte) obj;

        return this.bitPosition == object.bitPosition && super.equals(obj);

    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        if (arr == null)
        {
            throw new NullPointerException("Byte array is null");
        }

        if ((offset < 0) || (offset >= arr.length))
        {
            throw new IndexOutOfBoundsException("Offset to byte array is out of bounds: offset = " + offset + ", array.length = " + arr.length);
        }

        byte newValue = arr[offset];

        newValue >>= bitPosition;
        newValue &= 0x1;
        this.value = newValue == 1;
    }


    public String toString()
    {
        return "" + value;
    }


    public byte[] writeByteArray()
    {
        byte[] retValue;

        retValue = new byte[1];

        if (value != null)
        {
            retValue[0] = (byte) ((Boolean) value ? 1 : 0);
            retValue[0] <<= bitPosition;
        }

        return retValue;
    }
}
