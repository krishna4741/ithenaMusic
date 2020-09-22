
package org.jaudiotagger.audio.mp3;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;


public class LameFrame
{
    public static final int LAME_HEADER_BUFFER_SIZE = 36;
    public static final int ENCODER_SIZE = 9;   //Includes LAME ID
    public static final int LAME_ID_SIZE = 4;
    public static final String LAME_ID = "LAME";
    private String encoder;


    private LameFrame(ByteBuffer lameHeader)
    {
        encoder = Utils.getString(lameHeader, 0, ENCODER_SIZE, TextEncoding.CHARSET_ISO_8859_1);
    }


    public static LameFrame parseLameFrame(ByteBuffer bb)
    {
        ByteBuffer lameHeader = bb.slice();
        String id = Utils.getString(lameHeader, 0, LAME_ID_SIZE, TextEncoding.CHARSET_ISO_8859_1);
        lameHeader.rewind();
        if (id.equals(LAME_ID))
        {
            LameFrame lameFrame = new LameFrame(lameHeader);
            return lameFrame;
        }
        return null;
    }


    public String getEncoder()
    {
        return encoder;
    }
}

