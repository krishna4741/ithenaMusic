
package org.jaudiotagger.audio.ogg;

import org.jaudiotagger.audio.ogg.util.VorbisHeader;
import org.jaudiotagger.audio.ogg.util.VorbisPacketType;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentCreator;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;


public class OggVorbisCommentTagCreator
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.ogg");

    public static final int FIELD_FRAMING_BIT_LENGTH = 1;
    public static final byte FRAMING_BIT_VALID_VALUE = (byte) 0x01;
    private VorbisCommentCreator creator = new VorbisCommentCreator();

    //Creates the ByteBuffer for the ogg tag
    public ByteBuffer convert(Tag tag) throws UnsupportedEncodingException
    {
        ByteBuffer ogg = creator.convert(tag);
        int tagLength = ogg.capacity() + VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH + OggVorbisCommentTagCreator.FIELD_FRAMING_BIT_LENGTH;

        ByteBuffer buf = ByteBuffer.allocate(tagLength);

        //[packet type=comment0x03]['vorbis']
        buf.put((byte) VorbisPacketType.COMMENT_HEADER.getType());
        buf.put(VorbisHeader.CAPTURE_PATTERN_AS_BYTES);

        //The actual tag
        buf.put(ogg);

        //Framing bit = 1
        buf.put(FRAMING_BIT_VALID_VALUE);

        buf.rewind();
        return buf;
    }
}
