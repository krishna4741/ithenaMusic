
package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;


public class Mp4TagTextNumberField extends Mp4TagTextField
{
    public static final int NUMBER_LENGTH = 2;

    //Holds the numbers decoded
    protected List<Short> numbers;


    public Mp4TagTextNumberField(String id, String numberArray)
    {
        super(id, numberArray);
    }

    public Mp4TagTextNumberField(String id, ByteBuffer data) throws UnsupportedEncodingException
    {
        super(id, data);
    }


    protected byte[] getDataBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Short number : numbers)
        {
            try
            {
                baos.write(Utils.getSizeBEInt16(number));
            }
            catch (IOException e)
            {
                //This should never happen because we are not writing to file at this point.
                throw new RuntimeException(e);
            }
        }
        return baos.toByteArray();
    }

    public void copyContent(TagField field)
    {
        if (field instanceof Mp4TagTextNumberField)
        {
            this.content = ((Mp4TagTextNumberField) field).getContent();
            this.numbers = ((Mp4TagTextNumberField) field).getNumbers();
        }
    }


    public Mp4FieldType getFieldType()
    {
        return Mp4FieldType.IMPLICIT;
    }

    protected void build(ByteBuffer data) throws UnsupportedEncodingException
    {
        //Data actually contains a 'Data' Box so process data using this
        Mp4BoxHeader header = new Mp4BoxHeader(data);
        Mp4DataBox databox = new Mp4DataBox(header, data);
        dataSize = header.getDataLength();
        content = databox.getContent();
        numbers = databox.getNumbers();
    }


    public List<Short> getNumbers()
    {
        return numbers;
    }
}
