
package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.mp4.Mp4TagField;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


public class Mp4TagTextField extends Mp4TagField implements TagTextField
{
    protected int dataSize;
    protected String content;


    public Mp4TagTextField(String id, ByteBuffer data) throws UnsupportedEncodingException
    {
        super(id, data);
    }


    public Mp4TagTextField(String id, String content)
    {
        super(id);
        this.content = content;
    }

    protected void build(ByteBuffer data) throws UnsupportedEncodingException
    {
        //Data actually contains a 'Data' Box so process data using this
        Mp4BoxHeader header = new Mp4BoxHeader(data);
        Mp4DataBox databox = new Mp4DataBox(header, data);
        dataSize = header.getDataLength();
        content = databox.getContent();
    }

    public void copyContent(TagField field)
    {
        if (field instanceof Mp4TagTextField)
        {
            this.content = ((Mp4TagTextField) field).getContent();
        }
    }

    public String getContent()
    {
        return content;
    }

    protected byte[] getDataBytes() throws UnsupportedEncodingException
    {
        return content.getBytes(getEncoding());
    }

    public Mp4FieldType getFieldType()
    {
        return Mp4FieldType.TEXT;
    }

    public String getEncoding()
    {
        return Mp4BoxHeader.CHARSET_UTF_8;
    }


    public boolean isBinary()
    {
        return false;
    }

    public boolean isEmpty()
    {
        return this.content.trim().equals("");
    }

    public void setContent(String s)
    {
        this.content = s;
    }

    public void setEncoding(String s)
    {

    }

    public String toString()
    {
        return content;
    }
}
