
package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.mp4.Mp4TagField;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class Mp4TagBinaryField extends Mp4TagField
{
    protected int dataSize;
    protected byte[] dataBytes;
    protected boolean isBinary = false;


    public Mp4TagBinaryField(String id)
    {
        super(id);
    }


    public Mp4TagBinaryField(String id, byte[] data)
    {
        super(id);
        this.dataBytes = data;
    }


    public Mp4TagBinaryField(String id, ByteBuffer raw) throws UnsupportedEncodingException
    {
        super(id, raw);
    }

    public Mp4FieldType getFieldType()
    {
        //TODO dont know what value this should be do we actually have any binary fields other
        //than cover art
        return Mp4FieldType.IMPLICIT;
    }


    protected byte[] getDataBytes() throws UnsupportedEncodingException
    {
        return dataBytes;
    }

    protected void build(ByteBuffer raw)
    {
        Mp4BoxHeader header = new Mp4BoxHeader(raw);
        dataSize = header.getDataLength();

        //Skip the version and length fields
        raw.position(raw.position() + Mp4DataBox.PRE_DATA_LENGTH);

        //Read the raw data into byte array
        this.dataBytes = new byte[dataSize - Mp4DataBox.PRE_DATA_LENGTH];
        for (int i = 0; i < dataBytes.length; i++)
        {
            this.dataBytes[i] = raw.get();
        }

        //After returning buffers position will be after the end of this atom
    }

    public boolean isBinary()
    {
        return isBinary;
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
        if (field instanceof Mp4TagBinaryField)
        {
            this.dataBytes = ((Mp4TagBinaryField) field).getData();
            this.isBinary = field.isBinary();
        }
    }
}
