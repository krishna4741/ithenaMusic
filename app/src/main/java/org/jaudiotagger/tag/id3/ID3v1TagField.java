package org.jaudiotagger.tag.id3;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;

import java.io.UnsupportedEncodingException;


public class ID3v1TagField implements TagTextField
{


    private boolean common;


    private String content;


    private String id;


    public ID3v1TagField(byte[] raw) throws UnsupportedEncodingException
    {
        String field = new String(raw, "ISO-8859-1");

        int i = field.indexOf("=");
        if (i == -1)
        {
            //Beware that ogg ID, must be capitalized and contain no space..
            this.id = "ERRONEOUS";
            this.content = field;
        }
        else
        {
            this.id = field.substring(0, i).toUpperCase();
            if (field.length() > i)
            {
                this.content = field.substring(i + 1);
            }
            else
            {
                //We have "XXXXXX=" with nothing after the "="
                this.content = "";
            }
        }
        checkCommon();
    }


    public ID3v1TagField(String fieldId, String fieldContent)
    {
        this.id = fieldId.toUpperCase();
        this.content = fieldContent;
        checkCommon();
    }


    private void checkCommon()
    {
        this.common = id.equals(ID3v1FieldKey.TITLE.name()) || id.equals(ID3v1FieldKey.ALBUM.name()) || id.equals(ID3v1FieldKey.ARTIST.name()) || id.equals(ID3v1FieldKey.GENRE.name()) || id.equals(ID3v1FieldKey.YEAR.name()) || id.equals(ID3v1FieldKey.COMMENT.name()) || id.equals(ID3v1FieldKey.TRACK.name());
    }


    protected void copy(byte[] src, byte[] dst, int dstOffset)
    {
        //        for (int i = 0; i < src.length; i++)
        //            dst[i + dstOffset] = src[i];

        System.arraycopy(src, 0, dst, dstOffset, src.length);
    }


    public void copyContent(TagField field)
    {
        if (field instanceof TagTextField)
        {
            this.content = ((TagTextField) field).getContent();
        }
    }


    public String getContent()
    {
        return content;
    }


    public String getEncoding()
    {
        return "ISO-8859-1";
    }


    public String getId()
    {
        return this.id;
    }


    public byte[] getRawContent() throws UnsupportedEncodingException
    {
        byte[] size = new byte[4];
        byte[] idBytes = this.id.getBytes("ISO-8859-1");
        byte[] contentBytes = Utils.getDefaultBytes(this.content, "ISO-8859-1");
        byte[] b = new byte[4 + idBytes.length + 1 + contentBytes.length];

        int length = idBytes.length + 1 + contentBytes.length;
        size[3] = (byte) ((length & 0xFF000000) >> 24);
        size[2] = (byte) ((length & 0x00FF0000) >> 16);
        size[1] = (byte) ((length & 0x0000FF00) >> 8);
        size[0] = (byte) (length & 0x000000FF);

        int offset = 0;
        copy(size, b, offset);
        offset += 4;
        copy(idBytes, b, offset);
        offset += idBytes.length;
        b[offset] = (byte) 0x3D;
        offset++;// "="
        copy(contentBytes, b, offset);

        return b;
    }


    public boolean isBinary()
    {
        return false;
    }


    public void isBinary(boolean b)
    {
        //Do nothing, always false
    }


    public boolean isCommon()
    {
        return common;
    }


    public boolean isEmpty()
    {
        return this.content.equals("");
    }


    public void setContent(String s)
    {
        this.content = s;
    }


    public void setEncoding(String s)
    {
        //Do nothing, encoding is always ISO-8859-1 for this tag
    }

    public String toString()
    {
        return getContent();
    }
}
