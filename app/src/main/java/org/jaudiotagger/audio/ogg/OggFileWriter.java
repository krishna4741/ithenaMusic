
package org.jaudiotagger.audio.ogg;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.AudioFileWriter;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;


public class OggFileWriter extends AudioFileWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.ogg");

    private OggVorbisTagWriter vtw = new OggVorbisTagWriter();

    protected void writeTag(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotReadException, CannotWriteException, IOException
    {
        vtw.write(tag, raf, rafTemp);
    }

    protected void deleteTag(RandomAccessFile raf, RandomAccessFile tempRaf) throws CannotReadException, CannotWriteException, IOException
    {
        vtw.delete(raf, tempRaf);
    }
}
