
package org.jaudiotagger.tag.reference;

import org.jaudiotagger.tag.datatype.AbstractIntStringValuePair;


public class PictureTypes extends AbstractIntStringValuePair
{
    private static PictureTypes pictureTypes;

    public static PictureTypes getInstanceOf()
    {
        if (pictureTypes == null)
        {
            pictureTypes = new PictureTypes();
        }
        return pictureTypes;
    }

    public static final int PICTURE_TYPE_FIELD_SIZE = 1;
    public static final String DEFAULT_VALUE = "Cover (front)";
    public static final Integer DEFAULT_ID = 3;

    private PictureTypes()
    {
        idToValue.put(0, "Other");
        idToValue.put(1, "32x32 pixels 'file icon' (PNG only)");
        idToValue.put(2, "Other file icon");
        idToValue.put(3, "Cover (front)");
        idToValue.put(4, "Cover (back)");
        idToValue.put(5, "Leaflet page");
        idToValue.put(6, "Media (e.g. label side of CD)");
        idToValue.put(7, "Lead artist/lead performer/soloist");
        idToValue.put(8, "Artist/performer");
        idToValue.put(9, "Conductor");
        idToValue.put(10, "Band/Orchestra");
        idToValue.put(11, "Composer");
        idToValue.put(12, "Lyricist/text writer");
        idToValue.put(13, "Recording Location");
        idToValue.put(14, "During recording");
        idToValue.put(15, "During performance");
        idToValue.put(16, "Movie/video screen capture");
        idToValue.put(17, "A bright coloured fish");
        idToValue.put(18, "Illustration");
        idToValue.put(19, "Band/artist logotype");
        idToValue.put(20, "Publisher/Studio logotype");

        createMaps();
    }

}
