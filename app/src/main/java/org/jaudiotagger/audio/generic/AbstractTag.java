
package org.jaudiotagger.audio.generic;

import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.images.Artwork;

import java.util.*;


public abstract class AbstractTag implements Tag
{

    protected int commonNumber = 0;


    protected Map<String, List<TagField>> fields = new LinkedHashMap<String, List<TagField>>();


    public void addField(TagField field)
    {
        if (field == null)
        {
            return;
        }
        List<TagField> list = fields.get(field.getId());

        // There was no previous item
        if (list == null)
        {
            list = new ArrayList<TagField>();
            list.add(field);
            fields.put(field.getId(), list);
            if (field.isCommon())
            {
                commonNumber++;
            }
        }
        else
        {
            // We append to existing list
            list.add(field);
        }
    }



    public List<TagField> getFields(String id)
    {
        List<TagField> list = fields.get(id);

        if (list == null)
        {
            return new ArrayList<TagField>();
        }

        return list;
    }



    public List<String> getAll(String id) throws KeyNotFoundException
    {
        List<String>   fields = new ArrayList<String>();
        List<TagField> tagFields = getFields(id);
        for(TagField tagField:tagFields)
        {
            fields.add(tagField.toString());
        }
        return fields;
    }


    public String getItem(String id,int index)
    {
        List<TagField> l = getFields(id);
        return (l.size()>index) ? l.get(index).toString() : "";
    }


    public String getFirst(FieldKey genericKey) throws KeyNotFoundException
    {
        return getValue(genericKey,0);
    }


    public String getFirst(String id)
    {
        List<TagField> l = getFields(id);
        return (l.size() != 0) ? l.get(0).toString() : "";
    }


    public TagField getFirstField(String id)
    {
        List<TagField> l = getFields(id);
        return (l.size() != 0) ? l.get(0) : null;
    }



    public Iterator<TagField> getFields()
    {
        final Iterator<Map.Entry<String, List<TagField>>> it = this.fields.entrySet().iterator();
        return new Iterator<TagField>()
        {
            private Iterator<TagField> fieldsIt;

            private void changeIt()
            {
                if (!it.hasNext())
                {
                    return;
                }

                Map.Entry<String, List<TagField>> e = it.next();
                List<TagField> l = e.getValue();
                fieldsIt = l.iterator();
            }

            public boolean hasNext()
            {
                if (fieldsIt == null)
                {
                    changeIt();
                }
                return it.hasNext() || (fieldsIt != null && fieldsIt.hasNext());
            }

            public TagField next()
            {
                if (!fieldsIt.hasNext())
                {
                    changeIt();
                }

                return fieldsIt.next();
            }

            public void remove()
            {
                fieldsIt.remove();
            }
        };
    }


    public int getFieldCount()
    {
        Iterator it = getFields();
        int count = 0;
        while (it.hasNext())
        {
            count++;
            it.next();
        }
        return count;
    }
        
    public int getFieldCountIncludingSubValues()
    {
        return getFieldCount();
    }


    public boolean hasCommonFields()
    {
        return commonNumber != 0;
    }


    public boolean hasField(String id)
    {
        return getFields(id).size() != 0;
    }

    public boolean hasField(FieldKey fieldKey)
    {
        return hasField(fieldKey.name());
    }


    protected abstract boolean isAllowedEncoding(String enc);


    public boolean isEmpty()
    {
        return fields.size() == 0;
    }


    public void setField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        TagField tagfield = createField(genericKey,value);
        setField(tagfield);
    }

    


    public void addField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        TagField tagfield = createField(genericKey,value);
        addField(tagfield);
    }


    public void setField(TagField field)
    {
        if (field == null)
        {
            return;
        }

        // If there is already an existing field with same id
        // and both are TextFields, we replace the first element
        List<TagField> list = fields.get(field.getId());
        if (list != null)
        {
            list.set(0, field);
            return;
        }

        // Else we put the new field in the fields.
        list = new ArrayList<TagField>();
        list.add(field);
        fields.put(field.getId(), list);
        if (field.isCommon())
        {
            commonNumber++;
        }
    }



    public boolean setEncoding(String enc)
    {
        if (!isAllowedEncoding(enc))
        {
            return false;
        }

        Iterator it = getFields();
        while (it.hasNext())
        {
            TagField field = (TagField) it.next();
            if (field instanceof TagTextField)
            {
                ((TagTextField) field).setEncoding(enc);
            }
        }

        return true;
    }


    public String toString()
    {
        StringBuffer out = new StringBuffer();
        out.append("Tag content:\n");
        Iterator it = getFields();
        while (it.hasNext())
        {
            TagField field = (TagField) it.next();
            out.append("\t");
            out.append(field.getId());
            out.append(":");
            out.append(field.toString());
            out.append("\n");
        }
        return out.toString().substring(0, out.length() - 1);
    }


    public abstract TagField createField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException;


    public abstract TagField getFirstField(FieldKey genericKey) throws KeyNotFoundException;


    public abstract void deleteField(FieldKey fieldKey) throws KeyNotFoundException;



    public void deleteField(String key)
    {
        fields.remove(key);
    }

    public Artwork getFirstArtwork()
    {
        List<Artwork> artwork = getArtworkList();
        if(artwork.size()>0)
        {
            return artwork.get(0);
        }
        return null;
    }


    public void setField(Artwork artwork) throws FieldDataInvalidException
    {
        this.setField(createField(artwork));
    }


    public void addField(Artwork artwork) throws FieldDataInvalidException
    {
       this.addField(createField(artwork));
    }



    public void deleteArtworkField() throws KeyNotFoundException
    {
        this.deleteField(FieldKey.COVER_ART);
    }



}
