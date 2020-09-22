
package org.jaudiotagger.tag.datatype;

import java.util.*;


public abstract class AbstractValuePair<I, V>
{
    protected final Map<I, V> idToValue = new LinkedHashMap<I, V>();
    protected final Map<V, I> valueToId = new LinkedHashMap<V, I>();
    protected final List<V> valueList = new ArrayList<V>();

    protected Iterator<I> iterator = idToValue.keySet().iterator();

    protected String value;


    public List<V> getAlphabeticalValueList()
    {
        return valueList;
    }

    public Map<I, V> getIdToValueMap()
    {
        return idToValue;
    }

    public Map<V, I> getValueToIdMap()
    {
        return valueToId;
    }


    public int getSize()
    {
        return valueList.size();
    }
}
