
package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.ogg.util.VorbisHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import static org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey.*;

import java.io.UnsupportedEncodingException;


public class VorbisCommentTagField implements TagTextField
{


    private boolean common;


    private String content;


    private String id;


    private static final String ERRONEOUS_ID = "ERRONEOUS";


    public VorbisCommentTagField(byte[] raw) throws UnsupportedEncodingException
    {
        String field = new String(raw, "UTF-8");
        int i = field.indexOf("=");
        if (i == -1)
        {
            //Beware that ogg ID, must be capitalized and contain no space..
            this.id = ERRONEOUS_ID;
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


    public VorbisCommentTagField(String fieldId, String fieldContent)
    {
        this.id = fieldId.toUpperCase();
        this.content = fieldContent;
        checkCommon();
    }


    private void checkCommon()
    {
        this.common = id.equals(TITLE.getFieldName()) || id.equals(ALBUM.getFieldName()) || id.equals(ARTIST.getFieldName())
                || id.equals(GENRE.getFieldName()) || id.equals(TRACKNUMBER.getFieldName()) || id.equals(DATE.getFieldName())
        || id.equals(DESCRIPTION.getFieldName()) || id.equals(COMMENT.getFieldName());

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


    protected byte[] getBytes(String s, String encoding) throws UnsupportedEncodingException
    {
        return s.getBytes(encoding);
    }


    public String getContent()
    {
        return content;
    }


    public String getEncoding()
    {
        return VorbisHeader.CHARSET_UTF_8;
    }


    public String getId()
    {
        return this.id;
    }


    public byte[] getRawContent() throws UnsupportedEncodingException
    {
        byte[] size = new byte[VorbisCommentReader.FIELD_COMMENT_LENGTH_LENGTH];
        byte[] idBytes = Utils.getDefaultBytes(this.id, "ISO-8859-1");
        byte[] contentBytes = getBytes(this.content, "UTF-8");
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
        if (b)
        {
            // Only throw if binary = true requested.
            throw new UnsupportedOperationException("OggTagFields cannot be changed to binary.\n" + "binary data should be stored elsewhere" + " according to Vorbis_I_spec.");
        }
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
        if (s == null || !s.equalsIgnoreCase("UTF-8"))
        {
            throw new UnsupportedOperationException("The encoding of OggTagFields cannot be " + "changed.(specified to be UTF-8)");
        }
    }

    public String toString()
    {
        return getContent();
    }
}