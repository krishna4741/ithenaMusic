
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.ID3Tags;


public class NumberVariableLength extends AbstractDataType
{
    private static final int MINIMUM_NO_OF_DIGITS = 1;
    private static final int MAXIMUM_NO_OF_DIGITS = 8;

    int minLength = MINIMUM_NO_OF_DIGITS;



    public NumberVariableLength(String identifier, AbstractTagFrameBody frameBody, int minimumSize)
    {
        super(identifier, frameBody);

        //Set minimum length, which can be zero if optional
        this.minLength = minimumSize;

    }

    public NumberVariableLength(NumberVariableLength copy)
    {
        super(copy);
        this.minLength = copy.minLength;
    }


    public int getMaximumLenth()
    {
        return MAXIMUM_NO_OF_DIGITS;
    }


    public int getMinimumLength()
    {
        return minLength;
    }


    public void setMinimumSize(int minimumSize)
    {
        if (minimumSize > 0)
        {
            this.minLength = minimumSize;
        }
    }


    public int getSize()
    {
        if (value == null)
        {
            return 0;
        }
        else
        {
            int current;
            long temp = ID3Tags.getWholeNumber(value);
            int size = 0;

            for (int i = MINIMUM_NO_OF_DIGITS; i <= MAXIMUM_NO_OF_DIGITS; i++)
            {
                current = (byte) temp & 0xFF;

                if (current != 0)
                {
                    size = i;
                }

                temp >>= MAXIMUM_NO_OF_DIGITS;
            }

            return (minLength > size) ? minLength : size;
        }
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof NumberVariableLength))
        {
            return false;
        }

        NumberVariableLength object = (NumberVariableLength) obj;

        return this.minLength == object.minLength && super.equals(obj);

    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        //Coding error, should never happen
        if (arr == null)
        {
            throw new NullPointerException("Byte array is null");
        }

        //Coding error, should never happen as far as I can see
        if (offset < 0)
        {
            throw new IllegalArgumentException("negativer offset into an array offset:" + offset);
        }

        //If optional then set value to zero, this will mean that if this frame is written back to file it will be created
        //with this additional datatype wheras it didnt exist but I think this is probably an advantage the frame is
        //more likely to be parsed by other applications if it contains optional fields.
        //if not optional problem with this frame
        if (offset >= arr.length)
        {
            if (minLength == 0)
            {
                value = (long) 0;
                return;
            }
            else
            {
                throw new InvalidDataTypeException("Offset to byte array is out of bounds: offset = " + offset + ", array.length = " + arr.length);
            }
        }

        long lvalue = 0;

        //Read the bytes (starting from offset), the most significant byte of the number being constructed is read first,
        //we then shift the resulting long one byte over to make room for the next byte
        for (int i = offset; i < arr.length; i++)
        {
            lvalue <<= 8;
            lvalue += (arr[i] & 0xff);
        }

        value = lvalue;
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
        int size = getSize();
        byte[] arr;

        if (size == 0)
        {
            arr = new byte[0];
        }
        else
        {
            long temp = ID3Tags.getWholeNumber(value);
            arr = new byte[size];

            //keeps shifting the number downwards and masking the last 8 bist to get the value for the next byte
            //to be written
            for (int i = size - 1; i >= 0; i--)
            {
                arr[i] = (byte) (temp & 0xFF);
                temp >>= 8;
            }
        }
        return arr;
    }
}
