
package org.jaudiotagger.tag.id3.valuepair;

import org.jaudiotagger.tag.datatype.AbstractIntStringValuePair;



public class TextEncoding extends AbstractIntStringValuePair
{

    //Supported Java charsets
    public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    public static final String CHARSET_UTF_16 = "UTF-16";        //Want to use x-UTF-16LE-BOM but not always available
    public static final String CHARSET_UTF_16BE = "UTF-16BE";
    public static final String CHARSET_UTF_8 = "UTF-8";

    //Need both depending on whether want to use BigEndian or Little Endian
    public static final String CHARSET_UTF_16_LE_ENCODING_FORMAT = "UTF-16LE";
    public static final String CHARSET_UTF_16_BE_ENCODING_FORMAT = "UTF-16BE";

    //Supported ID3 charset ids
    public static final byte ISO_8859_1 = 0;
    public static final byte UTF_16 = 1;               //We use UTF-16 with LE byte-ordering and byte order mark buy default
                                                       //but can also use BOM with BE byte ordering
    public static final byte UTF_16BE = 2;
    public static final byte UTF_8 = 3;

    //The number of bytes used to hold the text encoding field size
    public static final int TEXT_ENCODING_FIELD_SIZE = 1;

    private static TextEncoding textEncodings;

    public static TextEncoding getInstanceOf()
    {
        if (textEncodings == null)
        {
            textEncodings = new TextEncoding();
        }
        return textEncodings;
    }

    private TextEncoding()
    {
        idToValue.put((int) ISO_8859_1, CHARSET_ISO_8859_1);
        idToValue.put((int) UTF_16, CHARSET_UTF_16);
        idToValue.put((int) UTF_16BE, CHARSET_UTF_16BE);
        idToValue.put((int) UTF_8, CHARSET_UTF_8);

        createMaps();

    }
}
