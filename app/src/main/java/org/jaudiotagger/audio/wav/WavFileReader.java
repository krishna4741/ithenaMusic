
package org.jaudiotagger.audio.wav;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.generic.GenericTag;
import org.jaudiotagger.audio.wav.util.WavInfoReader;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WavFileReader extends AudioFileReader
{

    private WavInfoReader ir = new WavInfoReader();

    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        return ir.read(raf);
    }

    protected Tag getTag(RandomAccessFile raf) throws CannotReadException
    {           
        return new WavTag();
    }
}