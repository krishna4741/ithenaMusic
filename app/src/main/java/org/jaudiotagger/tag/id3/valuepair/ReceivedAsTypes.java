
package org.jaudiotagger.tag.id3.valuepair;

import org.jaudiotagger.tag.datatype.AbstractIntStringValuePair;


public class ReceivedAsTypes extends AbstractIntStringValuePair
{
    //The number of bytes used to hold the text encoding field size
    public static final int RECEIVED_AS_FIELD_SIZE = 1;

    private static ReceivedAsTypes receivedAsTypes;

    public static ReceivedAsTypes getInstanceOf()
    {
        if (receivedAsTypes == null)
        {
            receivedAsTypes = new ReceivedAsTypes();
        }
        return receivedAsTypes;
    }

    private ReceivedAsTypes()
    {
        idToValue.put(0x00, "Other");
        idToValue.put(0x01, "Standard CD album with other songs");
        idToValue.put(0x02, "Compressed audio on CD");
        idToValue.put(0x03, "File over the Internet");
        idToValue.put(0x04, "Stream over the Internet");
        idToValue.put(0x05, "As note sheets");
        idToValue.put(0x06, "As note sheets in a book with other sheets");
        idToValue.put(0x07, "Music on other media");
        idToValue.put(0x08, "Non-musical merchandise");
        createMaps();
    }
}
