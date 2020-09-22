
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.datatype.AbstractDataType;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.util.ArrayList;
import java.util.Iterator;


public abstract class AbstractTagFrameBody extends AbstractTagItem
{
    public void createStructure()
    {
    }


    private AbstractTagFrame header;


    protected ArrayList<AbstractDataType> objectList = new ArrayList<AbstractDataType>();


    public final byte getTextEncoding()
    {
        AbstractDataType o = getObject(DataTypes.OBJ_TEXT_ENCODING);

        if (o != null)
        {
            Long encoding = (Long) (o.getValue());
            return encoding.byteValue();
        }
        else
        {
            return TextEncoding.ISO_8859_1;
        }
    }


    public final void setTextEncoding(byte textEncoding)
    {
        //Number HashMap actually converts this byte to a long
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
    }



    protected AbstractTagFrameBody()
    {
        setupObjectList();
    }


    protected AbstractTagFrameBody(AbstractTagFrameBody copyObject)
    {
        AbstractDataType newObject;
        for (int i = 0; i < copyObject.objectList.size(); i++)
        {
            newObject = (AbstractDataType) ID3Tags.copyObject(copyObject.objectList.get(i));
            newObject.setBody(this);
            this.objectList.add(newObject);
        }
    }



    public String getUserFriendlyValue()
    {
        return toString();
    }


    public String getBriefDescription()
    {
        String str = "";
        for (AbstractDataType object : objectList)
        {
            if ((object.toString() != null) && (object.toString().length() > 0))
            {
                str += (object.getIdentifier() + "=\"" + object.toString() + "\"; ");
            }
        }
        return str;
    }



    public final String getLongDescription()
    {
        String str = "";
        for (AbstractDataType object : objectList)
        {
            if ((object.toString() != null) && (object.toString().length() > 0))
            {
                str += (object.getIdentifier() + " = " + object.toString() + "\n");
            }
        }
        return str;
    }


    public final void setObjectValue(String identifier, Object value)
    {
        AbstractDataType object;
        Iterator<AbstractDataType> iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = iterator.next();
            if (object.getIdentifier().equals(identifier))
            {
                object.setValue(value);
            }
        }
    }


    public final Object getObjectValue(String identifier)
    {
        return getObject(identifier).getValue();
    }


    public final AbstractDataType getObject(String identifier)
    {
        AbstractDataType object;
        Iterator<AbstractDataType> iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = iterator.next();
            if (object.getIdentifier().equals(identifier))
            {
                return object;
            }
        }
        return null;
    }


    public int getSize()
    {
        int size = 0;
        AbstractDataType object;
        Iterator<AbstractDataType> iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = iterator.next();
            size += object.getSize();
        }
        return size;
    }


    public boolean isSubsetOf(Object obj)
    {
        if (!(obj instanceof AbstractTagFrameBody))
        {
            return false;
        }
        ArrayList<AbstractDataType> superset = ((AbstractTagFrameBody) obj).objectList;
        for (AbstractDataType anObjectList : objectList)
        {
            if (anObjectList.getValue() != null)
            {
                if (!superset.contains(anObjectList))
                {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean equals(Object obj)
    {
        if (!(obj instanceof AbstractTagFrameBody))
        {
            return false;
        }
        AbstractTagFrameBody object = (AbstractTagFrameBody) obj;
        boolean check =this.objectList.equals(object.objectList) && super.equals(obj);
        return check;
    }


    public Iterator iterator()
    {
        return objectList.iterator();
    }



    public String toString()
    {
        return getBriefDescription();
    }



    protected abstract void setupObjectList();


    public AbstractTagFrame getHeader()
    {
        return header;
    }


    public void setHeader(AbstractTagFrame header)
    {
        this.header = header;
    }
}
