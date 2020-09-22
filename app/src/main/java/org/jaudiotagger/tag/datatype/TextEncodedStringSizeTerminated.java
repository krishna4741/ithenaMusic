package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TextEncodedStringSizeTerminated extends AbstractString
{


    public TextEncodedStringSizeTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }


    public TextEncodedStringSizeTerminated(TextEncodedStringSizeTerminated object)
    {
        super(object);
    }

    public boolean equals(Object obj)
    {
        if(this==obj)
        {
            return true;
        }
        return obj instanceof TextEncodedStringSizeTerminated && super.equals(obj);
    }


    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        logger.finest("Reading from array from offset:" + offset);

        //Get the Specified Decoder
        String charSetName = getTextEncodingCharSet();
        CharsetDecoder decoder = Charset.forName(charSetName).newDecoder();
        decoder.reset();

        //Decode sliced inBuffer
        ByteBuffer inBuffer;
        //#302 [dallen] truncating array manually since the decoder.decode() does not honor the offset in the in buffer
        byte[] truncArr = new byte[arr.length - offset];
        System.arraycopy(arr, offset, truncArr, 0, truncArr.length);
        inBuffer = ByteBuffer.wrap(truncArr);

        CharBuffer outBuffer = CharBuffer.allocate(arr.length - offset);
        CoderResult coderResult = decoder.decode(inBuffer, outBuffer, true);
        if (coderResult.isError())
        {
            logger.warning("Decoding error:" + coderResult.toString());
        }
        decoder.flush(outBuffer);
        outBuffer.flip();

        //If using UTF16 with BOM we then search through the text removing any BOMs that could exist
        //for multiple values, BOM could be Big Endian or Little Endian
        if (charSetName.equals(TextEncoding.CHARSET_UTF_16))
        {
            value = outBuffer.toString().replace("\ufeff","").replace("\ufffe","");
        }
        else
        {
            value = outBuffer.toString();
        }
        //SetSize, important this is correct for finding the next datatype
        setSize(arr.length - offset);
        logger.config("Read SizeTerminatedString:" + value + " size:" + size);

    }


    private ByteBuffer writeString( CharsetEncoder encoder, String next, int i, int noOfValues)
            throws CharacterCodingException
    {

        ByteBuffer bb;
        if(( i + 1) == noOfValues )
        {
            bb = encoder.encode(CharBuffer.wrap(next));
        }
        else
        {
            bb = encoder.encode(CharBuffer.wrap(next + '\0'));
        }
        bb.rewind();
        return bb;
    }


    private ByteBuffer writeStringUTF16LEBOM( String next, int i, int noOfValues)
            throws CharacterCodingException
    {
        CharsetEncoder encoder = Charset.forName(TextEncoding.CHARSET_UTF_16_LE_ENCODING_FORMAT).newEncoder();
        encoder.onMalformedInput(CodingErrorAction.IGNORE);
        encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

        ByteBuffer bb = null;
        //Note remember LE BOM is ff fe but this is handled by encoder Unicode char is fe ff
        if(( i + 1)==noOfValues)
        {
            bb = encoder.encode(CharBuffer.wrap('\ufeff' + next ));
        }
        else
        {
            bb = encoder.encode(CharBuffer.wrap('\ufeff' + next + '\0'));
        }
        bb.rewind();
        return bb;
    }


    private ByteBuffer writeStringUTF16BEBOM( String next, int i, int noOfValues)
            throws CharacterCodingException
    {
        CharsetEncoder encoder = Charset.forName(TextEncoding.CHARSET_UTF_16_BE_ENCODING_FORMAT).newEncoder();
        encoder.onMalformedInput(CodingErrorAction.IGNORE);
        encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

        ByteBuffer bb = null;
        //Add BOM
        if(( i + 1)==noOfValues)
        {
            bb = encoder.encode(CharBuffer.wrap('\ufeff' + next ));
        }
        else
        {
            bb = encoder.encode(CharBuffer.wrap('\ufeff' + next + '\0'));
        }
        bb.rewind();
        return bb;
    }


    private void stripTrailingNull()
    {
        if (TagOptionSingleton.getInstance().isRemoveTrailingTerminatorOnWrite())
        {
            String stringValue = (String) value;
            if (stringValue.length() > 0)
            {
                if (stringValue.charAt(stringValue.length() - 1) == '\0')
                {
                    stringValue = (stringValue).substring(0, stringValue.length() - 1);
                    value = stringValue;
                }
            }
        }
    }


    private void checkTrailingNull( List<String> values, String stringValue)
    {
        if(!TagOptionSingleton.getInstance().isRemoveTrailingTerminatorOnWrite())
        {
            if (stringValue.length() > 0 && stringValue.charAt(stringValue.length() - 1) == '\0')
            {
                String lastVal = values.get(values.size() - 1);
                String newLastVal = lastVal + '\0';
                values.set(values.size() - 1,newLastVal);
            }
        }
    }


    public byte[] writeByteArray()
    {
        byte[] data;
        //Try and write to buffer using the CharSet defined by getTextEncodingCharSet()
        String charSetName   = getTextEncodingCharSet();
        try
        {
            
            stripTrailingNull();

            //Special Handling because there is no UTF16 BOM LE charset
            String stringValue   = (String)value;
            String actualCharSet = null;
            if (charSetName.equals(TextEncoding.CHARSET_UTF_16))
            {
                if(TagOptionSingleton.getInstance().isEncodeUTF16BomAsLittleEndian())
                {
                    actualCharSet = TextEncoding.CHARSET_UTF_16_LE_ENCODING_FORMAT;
                }
                else
                {
                    actualCharSet = TextEncoding.CHARSET_UTF_16_BE_ENCODING_FORMAT;
                }
            }

            //Ensure large enough for any encoding
            ByteBuffer outputBuffer = ByteBuffer.allocate((stringValue.length() + 3)* 3);

            //Ensure each string (if multiple values) is written with BOM by writing separately
            List<String> values = splitByNullSeperator(stringValue);
            checkTrailingNull(values, stringValue);

            //For each value
            for(int i=0;i<values.size();i++)
            {
                String next = values.get(i);
                if(actualCharSet!=null)
                {
                    if (actualCharSet.equals(TextEncoding.CHARSET_UTF_16_LE_ENCODING_FORMAT))
                    {
                        outputBuffer.put(writeStringUTF16LEBOM( next, i, values.size()));
                    }
                    else if (actualCharSet.equals(TextEncoding.CHARSET_UTF_16_BE_ENCODING_FORMAT))
                    {
                        outputBuffer.put(writeStringUTF16BEBOM( next, i, values.size()));
                    }
                }
                else
                {
                    CharsetEncoder charsetEncoder = Charset.forName(charSetName).newEncoder();
                    charsetEncoder.onMalformedInput(CodingErrorAction.IGNORE);
                    charsetEncoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
                    outputBuffer.put(writeString( charsetEncoder, next, i, values.size()));
                }
            }
            outputBuffer.flip();
            data = new byte[outputBuffer.limit()];
            outputBuffer.rewind();
            outputBuffer.get(data, 0, outputBuffer.limit());
            setSize(data.length);
        }
        //https://bitbucket.org/ijabz/jaudiotagger/issue/1/encoding-metadata-to-utf-16-can-fail-if
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage()+":"+charSetName+":"+value);
            throw new RuntimeException(ce);
        }
        return data;
    }


    protected String getTextEncodingCharSet()
    {
        byte textEncoding = this.getBody().getTextEncoding();
        String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
        logger.finest("text encoding:" + textEncoding + " charset:" + charSetName);
        return charSetName;
    }


    public static List<String> splitByNullSeperator(String value)
    {
        String[] valuesarray = value.split("\\u0000");
        List<String> values = Arrays.asList(valuesarray);
        //Read only list so if empty have to create new list
        if (values.size() == 0)
        {
            values = new ArrayList<String>(1);
            values.add("");
        }
        return values;
    }



    public void addValue(String value)
    {
        setValue(this.value + "\u0000" + value);
    }


    public int getNumberOfValues()
    {
        return splitByNullSeperator(((String) value)).size();
    }


    public String getValueAtIndex(int index)
    {
        //Split String into separate components
        List values = splitByNullSeperator((String) value);
        return (String) values.get(index);
    }


    public List<String> getValues()
    {
        return splitByNullSeperator((String) value);
    }


    public String getValueWithoutTrailingNull()
    {
        List<String> values = splitByNullSeperator((String) value);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<values.size();i++)
        {
            if(i!=0)
            {
                sb.append("\u0000");
            }
            sb.append(values.get(i));
        }
        return sb.toString();
    }
}
