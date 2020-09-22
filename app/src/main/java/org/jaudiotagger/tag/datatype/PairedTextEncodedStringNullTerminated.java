package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.utils.EqualsUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;


public class PairedTextEncodedStringNullTerminated extends AbstractDataType
{
    public PairedTextEncodedStringNullTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
        value = new PairedTextEncodedStringNullTerminated.ValuePairs();
    }

    public PairedTextEncodedStringNullTerminated(TextEncodedStringSizeTerminated object)
    {
        super(object);
        value = new PairedTextEncodedStringNullTerminated.ValuePairs();
    }

    public PairedTextEncodedStringNullTerminated(PairedTextEncodedStringNullTerminated object)
    {
        super(object);
    }

    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (!(obj instanceof PairedTextEncodedStringNullTerminated))
        {
            return false;
        }

        PairedTextEncodedStringNullTerminated that = (PairedTextEncodedStringNullTerminated) obj;

        return EqualsUtil.areEqual(value, that.value);
    }


    public int getSize()
    {
        return size;
    }


    public boolean canBeEncoded()
    {
        for (Pair entry : ((ValuePairs) value).mapping)
        {
            TextEncodedStringNullTerminated next = new TextEncodedStringNullTerminated(identifier, frameBody, entry.getValue());
            if (!next.canBeEncoded())
            {
                return false;
            }
        }
        return true;
    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        logger.finer("Reading PairTextEncodedStringNullTerminated from array from offset:" + offset);
        //Continue until unable to read a null terminated String
        while (true)
        {
            try
            {
                //Read Key
                TextEncodedStringNullTerminated key = new TextEncodedStringNullTerminated(identifier, frameBody);
                key.readByteArray(arr, offset);
                size   += key.getSize();
                offset += key.getSize();
                if (key.getSize() == 0)
                {
                    break;
                }

                try
                {
                    //Read Value
                    TextEncodedStringNullTerminated result = new TextEncodedStringNullTerminated(identifier, frameBody);
                    result.readByteArray(arr, offset);
                    size   += result.getSize();
                    offset += result.getSize();
                    if (result.getSize() == 0)
                    {
                        break;
                    }
                    //Add to value
                    ((ValuePairs) value).add((String) key.getValue(),(String) result.getValue());
                }
                catch (InvalidDataTypeException idte)
                {
                    //Value may not be null terminated if it is the last value
                    //Read Value
                    if(offset>=arr.length)
                    {
                        break;
                    }
                    TextEncodedStringSizeTerminated result = new TextEncodedStringSizeTerminated(identifier, frameBody);
                    result.readByteArray(arr, offset);
                    size   += result.getSize();
                    offset += result.getSize();
                    if (result.getSize() == 0)
                    {
                        break;
                    }
                    //Add to value
                    ((ValuePairs) value).add((String) key.getValue(),(String) result.getValue());
                    break;
                }
            }
            catch (InvalidDataTypeException idte)
            {
                break;
            }

            if (size == 0)
            {
                logger.warning("No null terminated Strings found");
                throw new InvalidDataTypeException("No null terminated Strings found");
            }
        }
        logger.finer("Read  PairTextEncodedStringNullTerminated:" + value + " size:" + size);
    }



    public byte[] writeByteArray()
    {
        logger.finer("Writing PairTextEncodedStringNullTerminated");

        int localSize = 0;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try
        {
            for (Pair pair : ((ValuePairs) value).mapping)
            {
                {
                    TextEncodedStringNullTerminated next = new TextEncodedStringNullTerminated(identifier, frameBody, pair.getKey());
                    buffer.write(next.writeByteArray());
                    localSize += next.getSize();
                }
                {
                    TextEncodedStringNullTerminated next = new TextEncodedStringNullTerminated(identifier, frameBody, pair.getValue());
                    buffer.write(next.writeByteArray());
                    localSize += next.getSize();
                }
            }
        }
        catch (IOException ioe)
        {
            //This should never happen because the write is internal with the JVM it is not to a file
            logger.log(Level.SEVERE, "IOException in MultipleTextEncodedStringNullTerminated when writing byte array", ioe);
            throw new RuntimeException(ioe);
        }

        //Update size member variable
        size = localSize;

        logger.finer("Written PairTextEncodedStringNullTerminated");
        return buffer.toByteArray();
    }


    public static class ValuePairs
    {
        private List<Pair> mapping = new ArrayList<Pair>();

        public ValuePairs()
        {
            super();
        }


        public void add(String key, String value)
        {
            mapping.add(new Pair(key,value));
        }



        public List<Pair> getMapping()
        {
            return mapping;
        }


        public int getNumberOfValues()
        {
            return mapping.size();
        }


        public String toString()
        {
            StringBuffer sb = new StringBuffer();
            for(Pair next:mapping)
            {
                sb.append(next.getKey()+':'+next.getValue()+',');
            }
            return sb.toString();
        }


        public int getNumberOfPairs()
        {
            return mapping.size();
        }

        public boolean equals(Object obj)
        {
            if (obj == this)
            {
                return true;
            }

            if (!(obj instanceof ValuePairs))
            {
                return false;
            }

            ValuePairs that = (ValuePairs) obj;

            return EqualsUtil.areEqual(getNumberOfValues(), that.getNumberOfValues());
        }
    }

    public ValuePairs getValue()
    {
        return (ValuePairs) value;
    }
}
