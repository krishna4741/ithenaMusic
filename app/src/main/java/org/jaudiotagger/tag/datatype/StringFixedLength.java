
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;



public class StringFixedLength extends AbstractString
{

    public StringFixedLength(String identifier, AbstractTagFrameBody frameBody, int size)
    {
        super(identifier, frameBody);
        if (size < 0)
        {
            throw new IllegalArgumentException("size is less than zero: " + size);
        }
        setSize(size);
    }

    public StringFixedLength(StringFixedLength copyObject)
    {
        super(copyObject);
        this.size = copyObject.size;
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof StringFixedLength))
        {
            return false;
        }
        StringFixedLength object = (StringFixedLength) obj;
        return this.size == object.size && super.equals(obj);
    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        logger.config("Reading from array from offset:" + offset);
        try
        {
            String charSetName = getTextEncodingCharSet();
            CharsetDecoder decoder = Charset.forName(charSetName).newDecoder();

            //Decode buffer if runs into problems should through exception which we
            //catch and then set value to empty string.
            logger.finest("Array length is:" + arr.length + "offset is:" + offset + "Size is:" + size);


            if (arr.length - offset < size)
            {
                throw new InvalidDataTypeException("byte array is to small to retrieve string of declared length:" + size);
            }
            String str = decoder.decode(ByteBuffer.wrap(arr, offset, size)).toString();
            if (str == null)
            {
                throw new NullPointerException("String is null");
            }
            value = str;
        }
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
            value = "";
        }
        logger.config("Read StringFixedLength:" + value);
    }


    public byte[] writeByteArray()
    {
        ByteBuffer dataBuffer;
        byte[] data;

        //Create with a series of empty of spaces to try and ensure integrity of field
        if (value == null)
        {
            logger.warning("Value of StringFixedlength Field is null using default value instead");
            data = new byte[size];
            for (int i = 0; i < size; i++)
            {
                data[i] = ' ';
            }
            return data;
        }

        try
        {
            String charSetName = getTextEncodingCharSet();
            if (charSetName.equals(TextEncoding.CHARSET_UTF_16))
            {
                charSetName = TextEncoding.CHARSET_UTF_16_LE_ENCODING_FORMAT;
                CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();


                //Note remember LE BOM is ff fe but tis is handled by encoder Unicode char is fe ff
                dataBuffer = encoder.encode(CharBuffer.wrap('\ufeff' + (String) value));
            }
            else
            {
                CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();

                dataBuffer = encoder.encode(CharBuffer.wrap((String) value));
            }
        }
        catch (CharacterCodingException ce)
        {
            logger.warning("There was a problem writing the following StringFixedlength Field:" + value + ":" + ce.getMessage() + "using default value instead");
            data = new byte[size];
            for (int i = 0; i < size; i++)
            {
                data[i] = ' ';
            }
            return data;
        }

        // We must return the defined size.
        // To check now because size is in bytes not chars
        if (dataBuffer != null)
        {
            //Everything ok
            if (dataBuffer.limit() == size)
            {
                data = new byte[dataBuffer.limit()];
                dataBuffer.get(data, 0, dataBuffer.limit());
                return data;
            }
            //There is more data available than allowed for this field strip
            else if (dataBuffer.limit() > size)
            {
                logger.warning("There was a problem writing the following StringFixedlength Field:" + value + " when converted to bytes has length of:" + dataBuffer.limit() + " but field was defined with length of:" + size + " too long so stripping extra length");
                data = new byte[size];
                dataBuffer.get(data, 0, size);
                return data;
            }
            //There is not enough data
            else
            {
                logger.warning("There was a problem writing the following StringFixedlength Field:" + value + " when converted to bytes has length of:" + dataBuffer.limit() + " but field was defined with length of:" + size + " too short so padding with spaces to make up extra length");

                data = new byte[size];
                dataBuffer.get(data, 0, dataBuffer.limit());

                for (int i = dataBuffer.limit(); i < size; i++)
                {
                    data[i] = ' ';
                }
                return data;
            }
        }
        else
        {
            logger.warning("There was a serious problem writing the following StringFixedlength Field:" + value + ":" + "using default value instead");
            data = new byte[size];
            for (int i = 0; i < size; i++)
            {
                data[i] = ' ';
            }
            return data;
        }
    }


    protected String getTextEncodingCharSet()
    {
        byte textEncoding = this.getBody().getTextEncoding();
        String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
        logger.finest("text encoding:" + textEncoding + " charset:" + charSetName);
        return charSetName;
    }
}
