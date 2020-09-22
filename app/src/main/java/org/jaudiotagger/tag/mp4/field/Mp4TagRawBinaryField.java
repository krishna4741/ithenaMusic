package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.mp4.Mp4TagField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class Mp4TagRawBinaryField extends Mp4TagField
{
    protected int dataSize;
    protected byte[] dataBytes;



    public Mp4TagRawBinaryField(Mp4BoxHeader header, ByteBuffer raw) throws UnsupportedEncodingException
    {
        super(header.getId());
        dataSize = header.getDataLength();
        build(raw);
    }

    public Mp4FieldType getFieldType()
    {
        return Mp4FieldType.IMPLICIT;
    }


    protected byte[] getDataBytes() throws UnsupportedEncodingException
    {
        return dataBytes;
    }



    protected void build(ByteBuffer raw)
    {
        //Read the raw data into byte array
        this.dataBytes = new byte[dataSize];
        for (int i = 0; i < dataBytes.length; i++)
        {
            this.dataBytes[i] = raw.get();
        }
    }

    public boolean isBinary()
    {
        return true;
    }

    public boolean isEmpty()
    {
        return this.dataBytes.length == 0;
    }

    public int getDataSize()
    {
        return dataSize;

    }

    public byte[] getData()
    {
        return this.dataBytes;
    }

    public void setData(byte[] d)
    {
        this.dataBytes = d;
    }

    public void copyContent(TagField field)
    {
        throw new UnsupportedOperationException("not done");
    }

    public byte[] getRawContent() throws UnsupportedEncodingException
    {
        logger.fine("Getting Raw data for:" + getId());
        try
        {
            ByteArrayOutputStream outerbaos = new ByteArrayOutputStream();
            outerbaos.write(Utils.getSizeBEInt32(Mp4BoxHeader.HEADER_LENGTH + dataSize));
            outerbaos.write(Utils.getDefaultBytes(getId(), "ISO-8859-1"));
            outerbaos.write(dataBytes);
            return outerbaos.toByteArray();
        }
        catch (IOException ioe)
        {
            //This should never happen as were not actually writing to/from a file
            throw new RuntimeException(ioe);
        }
    }

}
