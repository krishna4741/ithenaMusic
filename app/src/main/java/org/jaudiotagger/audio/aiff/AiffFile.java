package org.jaudiotagger.audio.aiff;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

public class AiffFile extends AudioFile {


    public final static SimpleDateFormat ISO_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");


    public AiffFile()   {
        
    }
    
    

    public AiffFile(String filename) throws 
            IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException    {
        this(new File(filename));
    }
    

    public AiffFile(File file) 
            throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException    {
        this (file, true);
    }
    
    public AiffFile(File file, boolean readOnly) 
            throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException{
        RandomAccessFile newFile = null;
        try
        {
            logger.setLevel(Level.FINEST);
            logger.fine("Called AiffFile constructor on " + file.getAbsolutePath());
            this.file = file;

            //Check File accessibility
            newFile = checkFilePermissions(file, readOnly);
            audioHeader = new AiffAudioHeader();
            //readTag();

        }
        finally
        {
            if (newFile != null)
            {
                newFile.close();
            }
        }
    }
    
    public AiffAudioHeader getAiffAudioHeader () {
        return (AiffAudioHeader) audioHeader;
    }

}
