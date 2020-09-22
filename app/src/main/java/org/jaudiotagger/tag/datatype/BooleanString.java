
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;

public class BooleanString extends AbstractDataType
{

    public BooleanString(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public BooleanString(BooleanString object)
    {
        super(object);
    }


    public int getSize()
    {
        return 1;
    }

    public boolean equals(Object obj)
    {
        return obj instanceof BooleanString && super.equals(obj);

    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        byte b = arr[offset];
        value = b != '0';
    }


    public String toString()
    {
        return "" + value;
    }


    public byte[] writeByteArray()
    {
        byte[] booleanValue = new byte[1];
        if (value == null)
        {
            booleanValue[0] = '0';
        }
        else
        {
            if ((Boolean) value)
            {
                booleanValue[0] = '0';
            }
            else
            {
                booleanValue[0] = '1';
            }
        }
        return booleanValue;
    }
}
