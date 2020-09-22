
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Logger;


public abstract class AbstractDataType
{
    protected static final String TYPE_ELEMENT = "element";

    //Logger
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.datatype");


    protected Object value = null;


    protected String identifier = "";


    protected AbstractTagFrameBody frameBody = null;


    protected int size;


    protected AbstractDataType(String identifier, AbstractTagFrameBody frameBody)
    {
        this.identifier = identifier;
        this.frameBody  = frameBody;
    }


    protected AbstractDataType(String identifier, AbstractTagFrameBody frameBody, Object value)
    {
        this.identifier = identifier;
        this.frameBody  = frameBody;
        setValue(value);
    }


    public AbstractDataType(AbstractDataType copyObject)
    {
        // no copy constructor in super class
        this.identifier = copyObject.identifier;
        if (copyObject.value == null)
        {
            this.value = null;
        }
        else if (copyObject.value instanceof String)
        {
            this.value = copyObject.value;
        }
        else if (copyObject.value instanceof Boolean)
        {
            this.value = copyObject.value;
        }
        else if (copyObject.value instanceof Byte)
        {
            this.value = copyObject.value;
        }
        else if (copyObject.value instanceof Character)
        {
            this.value = copyObject.value;
        }
        else if (copyObject.value instanceof Double)
        {
            this.value = copyObject.value;
        }
        else if (copyObject.value instanceof Float)
        {
            this.value = copyObject.value;
        }
        else if (copyObject.value instanceof Integer)
        {
            this.value = copyObject.value;
        }
        else if (copyObject.value instanceof Long)
        {
            this.value = copyObject.value;
        }
        else if (copyObject.value instanceof Short)
        {
            this.value = copyObject.value;
        }
        else if(copyObject.value instanceof MultipleTextEncodedStringNullTerminated.Values)
        {
            this.value = copyObject.value;
        }
        else if(copyObject.value instanceof PairedTextEncodedStringNullTerminated.ValuePairs)
        {
            this.value = copyObject.value;
        }
        else if(copyObject.value instanceof PartOfSet.PartOfSetValue)
        {
            this.value = copyObject.value;
        }
        else if (copyObject.value instanceof boolean[])
        {
            this.value = ((boolean[]) copyObject.value).clone();
        }
        else if (copyObject.value instanceof byte[])
        {
            this.value = ((byte[]) copyObject.value).clone();
        }
        else if (copyObject.value instanceof char[])
        {
            this.value = ((char[]) copyObject.value).clone();
        }
        else if (copyObject.value instanceof double[])
        {
            this.value = ((double[]) copyObject.value).clone();
        }
        else if (copyObject.value instanceof float[])
        {
            this.value = ((float[]) copyObject.value).clone();
        }
        else if (copyObject.value instanceof int[])
        {
            this.value = ((int[]) copyObject.value).clone();
        }
        else if (copyObject.value instanceof long[])
        {
            this.value = ((long[]) copyObject.value).clone();
        }
        else if (copyObject.value instanceof short[])
        {
            this.value = ((short[]) copyObject.value).clone();
        }
        else if (copyObject.value instanceof Object[])
        {
            this.value = ((Object[]) copyObject.value).clone();
        }
        else if (copyObject.value instanceof ArrayList)
        {
            this.value = ((ArrayList) copyObject.value).clone();
        }
        else if (copyObject.value instanceof LinkedList)
        {
            this.value = ((LinkedList) copyObject.value).clone();
        }
        else
        {
            throw new UnsupportedOperationException("Unable to create copy of class " + copyObject.getClass());
        }
    }


    public void setBody(AbstractTagFrameBody frameBody)
    {
        this.frameBody = frameBody;
    }


    public AbstractTagFrameBody getBody()
    {
        return frameBody;
    }


    public String getIdentifier()
    {
        return identifier;
    }


    public void setValue(Object value)
    {
        this.value = value;
    }


    public Object getValue()
    {
        return value;
    }


    final public void readByteArray(byte[] arr) throws InvalidDataTypeException
    {
        readByteArray(arr, 0);
    }


    abstract public int getSize();


    public boolean equals(Object obj)
    {
        if(this==obj)
        {
            return true;
        }

        if (!(obj instanceof AbstractDataType))
        {
            return false;
        }
        AbstractDataType object = (AbstractDataType) obj;
        if (!this.identifier.equals(object.identifier))
        {
            return false;
        }
        if ((this.value == null) && (object.value == null))
        {
            return true;
        }
        else if ((this.value == null) || (object.value == null))
        {
            return false;
        }
        // boolean[]
        if (this.value instanceof boolean[] && object.value instanceof boolean[])
        {
            if (!Arrays.equals((boolean[]) this.value, (boolean[]) object.value))
            {
                return false;
            }
            // byte[]
        }
        else if (this.value instanceof byte[] && object.value instanceof byte[])
        {
            if (!Arrays.equals((byte[]) this.value, (byte[]) object.value))
            {
                return false;
            }
            // char[]
        }
        else if (this.value instanceof char[] && object.value instanceof char[])
        {
            if (!Arrays.equals((char[]) this.value, (char[]) object.value))
            {
                return false;
            }
            // double[]
        }
        else if (this.value instanceof double[] && object.value instanceof double[])
        {
            if (!Arrays.equals((double[]) this.value, (double[]) object.value))
            {
                return false;
            }
            // float[]
        }
        else if (this.value instanceof float[] && object.value instanceof float[])
        {
            if (!Arrays.equals((float[]) this.value, (float[]) object.value))
            {
                return false;
            }
            // int[]
        }
        else if (this.value instanceof int[] && object.value instanceof int[])
        {
            if (!Arrays.equals((int[]) this.value, (int[]) object.value))
            {
                return false;
            }
            // long[]
        }
        else if (this.value instanceof long[] && object.value instanceof long[])
        {
            if (!Arrays.equals((long[]) this.value, (long[]) object.value))
            {
                return false;
            }
            // Object[]
        }
        else if (this.value instanceof Object[] && object.value instanceof Object[])
        {
            if (!Arrays.equals((Object[]) this.value, (Object[]) object.value))
            {
                return false;
            }
            // short[]
        }
        else if (this.value instanceof short[] && object.value instanceof short[])
        {
            if (!Arrays.equals((short[]) this.value, (short[]) object.value))
            {
                return false;
            }
        }
        else if (!this.value.equals(object.value))
        {
            return false;
        }
        return true;
    }


    public abstract void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException;



    public abstract byte[] writeByteArray();


    public void createStructure()
    {
        MP3File.getStructureFormatter().addElement(identifier, getValue().toString());
    }

}
