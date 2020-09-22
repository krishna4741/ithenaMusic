
package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.InvalidTagException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


public class FieldFrameBodyUnsupported extends AbstractLyrics3v2FieldFrameBody
{

    private byte[] value = null;


    public FieldFrameBodyUnsupported()
    {
        //        this.value = new byte[0];
    }

    public FieldFrameBodyUnsupported(FieldFrameBodyUnsupported copyObject)
    {
        super(copyObject);
        this.value = copyObject.value.clone();
    }


    public FieldFrameBodyUnsupported(byte[] value)
    {
        this.value = value;
    }


    public FieldFrameBodyUnsupported(ByteBuffer byteBuffer) throws InvalidTagException
    {

        this.read(byteBuffer);

    }


    public String getIdentifier()
    {
        return "ZZZ";
    }


    public boolean isSubsetOf(Object obj)
    {
        if (!(obj instanceof FieldFrameBodyUnsupported))
        {
            return false;
        }

        FieldFrameBodyUnsupported object = (FieldFrameBodyUnsupported) obj;

        String subset = new String(this.value);
        String superset = new String(object.value);

        return superset.contains(subset) && super.isSubsetOf(obj);

    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof FieldFrameBodyUnsupported))
        {
            return false;
        }

        FieldFrameBodyUnsupported object = (FieldFrameBodyUnsupported) obj;

        return java.util.Arrays.equals(this.value, object.value) && super.equals(obj);

    }


    public void read(ByteBuffer byteBuffer) throws InvalidTagException
    {
        int size;
        byte[] buffer = new byte[5];

        // read the 5 character size
        byteBuffer.get(buffer, 0, 5);
        size = Integer.parseInt(new String(buffer, 0, 5));

        value = new byte[size];

        // read the SIZE length description
        byteBuffer.get(value);
    }


    public String toString()
    {
        return getIdentifier() + " : " + (new String(value));
    }


    public void write(RandomAccessFile file) throws IOException
    {
        int offset = 0;
        String str;
        byte[] buffer = new byte[5];

        str = Integer.toString(value.length);

        for (int i = 0; i < (5 - str.length()); i++)
        {
            buffer[i] = (byte) '0';
        }

        offset += (5 - str.length());

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i + offset] = (byte) str.charAt(i);
        }

        file.write(buffer);

        file.write(value);
    }


    protected void setupObjectList()
    {

    }
}