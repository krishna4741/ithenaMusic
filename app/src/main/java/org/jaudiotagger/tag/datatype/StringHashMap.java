
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.reference.Languages;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;



public class StringHashMap extends StringFixedLength implements HashMapInterface<String, String>
{


    Map<String, String> keyToValue = null;


    Map<String, String> valueToKey = null;


    boolean hasEmptyValue = false;


    public StringHashMap(String identifier, AbstractTagFrameBody frameBody, int size)
    {
        super(identifier, frameBody, size);

        if (identifier.equals(DataTypes.OBJ_LANGUAGE))
        {
            valueToKey = Languages.getInstanceOf().getValueToIdMap();
            keyToValue = Languages.getInstanceOf().getIdToValueMap();
        }
        else
        {
            throw new IllegalArgumentException("Hashmap identifier not defined in this class: " + identifier);
        }
    }

    public StringHashMap(StringHashMap copyObject)
    {
        super(copyObject);

        this.hasEmptyValue = copyObject.hasEmptyValue;
        this.keyToValue = copyObject.keyToValue;
        this.valueToKey = copyObject.valueToKey;
    }


    public Map<String, String> getKeyToValue()
    {
        return keyToValue;
    }


    public Map<String, String> getValueToKey()
    {
        return valueToKey;
    }


    public void setValue(Object value)
    {
        if (value instanceof String)
        {
            //Issue #273 temporary hack for MM
            if(value.equals("XXX"))
            {
                this.value=value.toString();
            }
            else
            {
                this.value = ((String) value).toLowerCase();
            }
        }
        else
        {
            this.value = value;
        }
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof StringHashMap))
        {
            return false;
        }

        StringHashMap object = (StringHashMap) obj;

        if (this.hasEmptyValue != object.hasEmptyValue)
        {
            return false;
        }

        if (this.keyToValue == null)
        {
            if (object.keyToValue != null)
            {
                return false;
            }
        }
        else
        {
            if (!this.keyToValue.equals(object.keyToValue))
            {
                return false;
            }
        }

        if (this.keyToValue == null)
        {
            if (object.keyToValue != null)
            {
                return false;
            }
        }
        else
        {
            if (!this.valueToKey.equals(object.valueToKey))
            {
                return false;
            }
        }

        return super.equals(obj);
    }


    public Iterator<String> iterator()
    {
        if (keyToValue == null)
        {
            return null;
        }
        else
        {
            // put them in a treeset first to sort them
            TreeSet<String> treeSet = new TreeSet<String>(keyToValue.values());

            if (hasEmptyValue)
            {
                treeSet.add("");
            }

            return treeSet.iterator();
        }
    }


    public String toString()
    {
        if (value == null)
        {
            return "";
        }
        else if (keyToValue.get(value) == null)
        {
            return "";
        }
        else
        {
            return keyToValue.get(value);
        }
    }


    protected String getTextEncodingCharSet()
    {
        return TextEncoding.CHARSET_ISO_8859_1;
    }
}
