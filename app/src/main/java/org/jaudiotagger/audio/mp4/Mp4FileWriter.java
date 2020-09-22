
package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.AudioFileWriter;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;


public class Mp4FileWriter extends AudioFileWriter
{

    private Mp4TagWriter tw = new Mp4TagWriter();


    protected void writeTag(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        tw.write(tag, raf, rafTemp);
    }

    protected void deleteTag(RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException
    {
        tw.delete(raf, rafTemp);
    }
}
