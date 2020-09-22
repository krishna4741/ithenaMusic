package org.jaudiotagger.audio.aiff;

import java.util.List;

import org.jaudiotagger.audio.generic.AbstractTag;
import org.jaudiotagger.audio.generic.GenericTag;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.images.Artwork;

public class AiffTag extends GenericTag {

    private class AiffTagTextField implements TagTextField
    {


        private String content;


        private final String id;


        public AiffTagTextField(String fieldId, String initialContent)
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
    } // End of AiffTagTextField
    
    
    public boolean hasField(AiffTagFieldKey fieldKey)
    {
        return hasField(fieldKey.name());
    }
    

    public void setField(AiffTagFieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        TagField tagfield = createField(genericKey,value);
        setField(tagfield);
    }
    
    public TagField createField(AiffTagFieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
            return new AiffTagTextField(genericKey.name(),value);
    }

    public TagField createCompilationField(boolean value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return createField(FieldKey.IS_COMPILATION,String.valueOf(value));
    }

}
