package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;


public class MultipleTextEncodedStringNullTerminated extends AbstractDataType
{


    public MultipleTextEncodedStringNullTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
        value = new MultipleTextEncodedStringNullTerminated.Values();
    }

    public MultipleTextEncodedStringNullTerminated(TextEncodedStringSizeTerminated object)
    {
        super(object);
        value = new MultipleTextEncodedStringNullTerminated.Values();
    }

    public MultipleTextEncodedStringNullTerminated(MultipleTextEncodedStringNullTerminated object)
    {
        super(object);        
    }

    public boolean equals(Object obj)
    {
        return obj instanceof MultipleTextEncodedStringNullTerminated && super.equals(obj);
    }


    public int getSize()
    {
        return size;
    }


    public boolean canBeEncoded()
    {
        for (ListIterator<String> li = ((Values) value).getList().listIterator(); li.hasNext();)
        {
            TextEncodedStringNullTerminated next = new TextEncodedStringNullTerminated(identifier, frameBody, li.next());
            if (!next.canBeEncoded())
            {
                return false;
            }
        }
        return true;
    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        logger.finer("Reading MultipleTextEncodedStringNullTerminated from array from offset:" + offset);
        //Continue until unable to read a null terminated String
        while (true)
        {
            try
            {
                //Read String
                TextEncodedStringNullTerminated next = new TextEncodedStringNullTerminated(identifier, frameBody);
                next.readByteArray(arr, offset);

                if (next.getSize() == 0)
                {
                    break;
                }
                else
                {
                    //Add to value
                    ((Values) value).add((String) next.getValue());

                    //Add to size calculation
                    size += next.getSize();

                    //Increment Offset to start of next datatype.
                    offset += next.getSize();
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
        logger.finer("Read  MultipleTextEncodedStringNullTerminated:" + value + " size:" + size);
    }


    public byte[] writeByteArray()
    {
        logger.finer("Writing MultipleTextEncodedStringNullTerminated");

        int localSize = 0;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try
        {
            for (ListIterator<String> li = ((Values) value).getList().listIterator(); li.hasNext();)
            {
                TextEncodedStringNullTerminated next = new TextEncodedStringNullTerminated(identifier, frameBody, li.next());
                buffer.write(next.writeByteArray());
                localSize += next.getSize();
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

        logger.finer("Written MultipleTextEncodedStringNullTerminated");
        return buffer.toByteArray();
    }


    public static class Values
    {
        private List<String> valueList = new ArrayList<String>();

        public Values()
        {

        }


        public void add(String value)
        {
            valueList.add(value);
        }



        public List<String> getList()
        {
            return valueList;
        }


        public int getNumberOfValues()
        {
            return valueList.size();
        }


        public String toString()
        {
            StringBuffer sb = new StringBuffer();
            for (ListIterator<String> li = valueList.listIterator(); li.hasNext();)
            {
                String next = li.next();
                sb.append(next);
                if (li.hasNext())
                {
                    sb.append(",");
                }
            }
            return sb.toString();
        }
    }
}
