

package org.jaudiotagger.tag.datatype;

import java.util.Iterator;
import java.util.Map;


public interface HashMapInterface<K, V>
{

    public Map<K, V> getKeyToValue();


    public Map<V, K> getValueToKey();


    public Iterator<V> iterator();
}
