package org.jaudiotagger.audio.mp3;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileWriter;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.io.RandomAccessFile;


public class MP3FileWriter extends AudioFileWriter
{
    public void deleteTag(AudioFile f) throws CannotWriteException
    {
        //Because audio file is an instanceof MP3File this directs it to save
        //taking into account if the tag has been sent to null in which case it will be deleted
        f.commit();
    }

    public void writeFile(AudioFile f) throws CannotWriteException
    {
        //Because audio file is an instanceof MP3File this directs it to save
        f.commit();
    }


    @Override
    public synchronized void delete(AudioFile af) throws CannotReadException, CannotWriteException
    {
        ((MP3File)af).setID3v1Tag(null);
        ((MP3File)af).setID3v2Tag(null);                
        af.commit();
    }

    protected void writeTag(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        throw new RuntimeException("MP3FileReaderwriteTag should not be called");
    }

    protected void deleteTag(RandomAccessFile raf, RandomAccessFile tempRaf) throws CannotWriteException, IOException
    {
        throw new RuntimeException("MP3FileReader.getEncodingInfo should be called");
    }
}


