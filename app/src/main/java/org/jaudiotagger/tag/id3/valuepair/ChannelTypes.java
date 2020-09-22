
package org.jaudiotagger.tag.id3.valuepair;

import org.jaudiotagger.tag.datatype.AbstractIntStringValuePair;

public class ChannelTypes extends AbstractIntStringValuePair
{
    private static ChannelTypes channelTypes;

    public static ChannelTypes getInstanceOf()
    {
        if (channelTypes == null)
        {
            channelTypes = new ChannelTypes();
        }
        return channelTypes;
    }

    private ChannelTypes()
    {
        idToValue.put(0x00, "Other");
        idToValue.put(0x01, "Master volume");
        idToValue.put(0x02, "Front right");
        idToValue.put(0x03, "Front left");
        idToValue.put(0x04, "Back right");
        idToValue.put(0x05, "Back left");
        idToValue.put(0x06, "Front centre");
        idToValue.put(0x07, "Back centre");
        idToValue.put(0x08, "Subwoofer");

        createMaps();
    }
}
