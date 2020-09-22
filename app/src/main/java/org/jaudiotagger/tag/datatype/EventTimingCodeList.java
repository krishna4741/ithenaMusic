
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.framebody.FrameBodyETCO;


public class EventTimingCodeList extends AbstractDataTypeList<EventTimingCode>
{


    public EventTimingCodeList(final EventTimingCodeList copy)
    {
        super(copy);
    }

    public EventTimingCodeList(final FrameBodyETCO body)
    {
        super(DataTypes.OBJ_TIMED_EVENT_LIST, body);
    }

    @Override
    protected EventTimingCode createListElement()
    {
        return new EventTimingCode(DataTypes.OBJ_TIMED_EVENT, frameBody);
    }
}
