
package org.jaudiotagger.tag.datatype;

import java.util.Collections;

public class AbstractStringStringValuePair extends AbstractValuePair<String, String>
{
    protected String lkey = null;


    public String getIdForValue(String value)
    {
        return valueToId.get(value);
    }


    public String getValueForId(String id)
    {
        return idToValue.get(id);
    }

    protected void createMaps()
    {
        iterator = idToValue.keySet().iterator();
        while (iterator.hasNext())
        {
            lkey = iterator.next();
            value = idToValue.get(lkey);
            valueToId.put(value, lkey);
        }

        //Value List
        iterator = idToValue.keySet().iterator();
        while (iterator.hasNext())
        {
            valueList.add(idToValue.get(iterator.next()));
        }
        //Sort alphabetically
        Collections.sort(valueList);
    }
}
