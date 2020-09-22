
package org.jaudiotagger.tag.datatype;

import java.util.Collections;
import java.util.Map;


public class AbstractIntStringValuePair extends AbstractValuePair<Integer, String>
{
    protected Integer key = null;


    public Integer getIdForValue(String value)
    {
        return valueToId.get(value);
    }


    public String getValueForId(int id)
    {
        return idToValue.get(id);
    }

    protected void createMaps()
    {
        //Create the reverse the map
        for (Map.Entry<Integer, String> entry : idToValue.entrySet())
        {
            valueToId.put(entry.getValue(), entry.getKey());
        }

        //Value List sort alphabetically
        valueList.addAll(idToValue.values());
        Collections.sort(valueList);
    }
}
