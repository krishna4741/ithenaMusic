
package org.jaudiotagger.tag.id3.valuepair;

import org.jaudiotagger.tag.datatype.AbstractIntStringValuePair;

public class EventTimingTimestampTypes extends AbstractIntStringValuePair
{

    private static EventTimingTimestampTypes eventTimingTimestampTypes;

    public static EventTimingTimestampTypes getInstanceOf()
    {
        if (eventTimingTimestampTypes == null)
        {
            eventTimingTimestampTypes = new EventTimingTimestampTypes();
        }
        return eventTimingTimestampTypes;
    }

    public static final int TIMESTAMP_KEY_FIELD_SIZE = 1;

    private EventTimingTimestampTypes()
    {
        idToValue.put(1, "Absolute time using MPEG [MPEG] frames as unit");
        idToValue.put(2, "Absolute time using milliseconds as unit");

        createMaps();
    }
}
