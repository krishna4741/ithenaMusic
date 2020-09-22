
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.images.Artwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;


public abstract class GenericTag extends AbstractTag
{
    private static EnumSet<FieldKey> supportedKeys;

    static
    {
        supportedKeys = EnumSet.of(FieldKey.ALBUM,FieldKey.ARTIST,FieldKey.TITLE,FieldKey.TRACK,FieldKey.GENRE,FieldKey.COMMENT,FieldKey.YEAR);
    }

    private class GenericTagTextField implements TagTextField
    {


        private String content;


        private final String id;


        public GenericTagTextField(String fieldId, String initialContent)
        {
            this.id = fieldId;
            this.content = initialContent;
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
            return this.content;
        }


        public String getEncoding()
        {
            return "ISO-8859-1";
        }


        public String getId()
        {
            return id;
        }


        public byte[] getRawContent()
        {
            return this.content == null ? new byte[]{} : Utils.getDefaultBytes(this.content, getEncoding());
        }


        public boolean isBinary()
        {
            return false;
        }


        public void isBinary(boolean b)
        {

        }


        public boolean isCommon()
        {
            return true;
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

        }


        public String toString()
        {
            return getContent();
        }
    }


    protected boolean isAllowedEncoding(String enc)
    {
        return true;
    }

    public TagField createField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        if(supportedKeys.contains(genericKey))
        {
            return new GenericTagTextField(genericKey.name(),value);
        }
        else
        {
            throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
        }
    }


    public String getFirst(FieldKey genericKey) throws KeyNotFoundException
    {
        return getValue(genericKey, 0);
    }

    public String getValue(FieldKey genericKey,int index) throws KeyNotFoundException
    {
        if(supportedKeys.contains(genericKey))
        {
            return getItem(genericKey.name(),index);
        }
        else
        {
            throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
        }
    }


    public List<TagField> getFields(FieldKey genericKey) throws KeyNotFoundException
    {
        List<TagField> list = fields.get(genericKey.name());
        if (list == null)
        {
            return new ArrayList<TagField>();
        }
        return list;
    }
    
    public List<String> getAll(FieldKey genericKey) throws KeyNotFoundException
    {
        return super.getAll(genericKey.name());
    }


    public void deleteField(FieldKey genericKey) throws KeyNotFoundException
    {
        if(supportedKeys.contains(genericKey))
        {
            deleteField(genericKey.name());
        }
        else
        {
            throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
        }
    }


    public TagField getFirstField(FieldKey genericKey) throws KeyNotFoundException
    {
        if(supportedKeys.contains(genericKey))
        {
            return getFirstField(genericKey.name());
        }
        else
        {
            throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
        }
    }

    public List<Artwork> getArtworkList()
    {
        return Collections.emptyList();
    }

    public TagField createField(Artwork artwork) throws FieldDataInvalidException
    {
        throw new UnsupportedOperationException(ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg());
    }
}
